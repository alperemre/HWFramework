package com.huawei.okhttp3.internal.ws;

import com.huawei.okhttp3.Call;
import com.huawei.okhttp3.Callback;
import com.huawei.okhttp3.EventListener;
import com.huawei.okhttp3.OkHttpClient;
import com.huawei.okhttp3.Protocol;
import com.huawei.okhttp3.Request;
import com.huawei.okhttp3.Response;
import com.huawei.okhttp3.WebSocket;
import com.huawei.okhttp3.WebSocketListener;
import com.huawei.okhttp3.internal.Internal;
import com.huawei.okhttp3.internal.Util;
import com.huawei.okhttp3.internal.connection.StreamAllocation;
import com.huawei.okhttp3.internal.ws.WebSocketReader;
import com.huawei.okio.BufferedSink;
import com.huawei.okio.BufferedSource;
import com.huawei.okio.ByteString;
import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000;
    private static final long MAX_QUEUE_SIZE = 16777216;
    private static final List<Protocol> ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    private boolean awaitingPong;
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue = new ArrayDeque<>();
    private final Request originalRequest;
    private final long pingIntervalMillis;
    private final ArrayDeque<ByteString> pongQueue = new ArrayDeque<>();
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode = -1;
    private String receivedCloseReason;
    private int receivedPingCount;
    private int receivedPongCount;
    private int sentPingCount;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;

    final class CancelRunnable implements Runnable {
        CancelRunnable() {
        }

        public void run() {
            RealWebSocket.this.cancel();
        }
    }

    static final class Close {
        final long cancelAfterCloseMillis;
        final int code;
        final ByteString reason;

        Close(int code2, ByteString reason2, long cancelAfterCloseMillis2) {
            this.code = code2;
            this.reason = reason2;
            this.cancelAfterCloseMillis = cancelAfterCloseMillis2;
        }
    }

    static final class Message {
        final ByteString data;
        final int formatOpcode;

        Message(int formatOpcode2, ByteString data2) {
            this.formatOpcode = formatOpcode2;
            this.data = data2;
        }
    }

    private final class PingRunnable implements Runnable {
        PingRunnable() {
        }

        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }

    public static abstract class Streams implements Closeable {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;

        public Streams(boolean client2, BufferedSource source2, BufferedSink sink2) {
            this.client = client2;
            this.source = source2;
            this.sink = sink2;
        }
    }

    public RealWebSocket(Request request, WebSocketListener listener2, Random random2, long pingIntervalMillis2) {
        if ("GET".equals(request.method())) {
            this.originalRequest = request;
            this.listener = listener2;
            this.random = random2;
            this.pingIntervalMillis = pingIntervalMillis2;
            byte[] nonce = new byte[16];
            random2.nextBytes(nonce);
            this.key = ByteString.of(nonce).base64();
            this.writerRunnable = new Runnable() {
                public void run() {
                    do {
                        try {
                        } catch (IOException e) {
                            RealWebSocket.this.failWebSocket(e, null);
                            return;
                        }
                    } while (RealWebSocket.this.writeOneFrame());
                }
            };
            return;
        }
        throw new IllegalArgumentException("Request must be GET: " + request.method());
    }

    public Request request() {
        return this.originalRequest;
    }

    public synchronized long queueSize() {
        return this.queueSize;
    }

    public void cancel() {
        this.call.cancel();
    }

    public void connect(OkHttpClient client) {
        OkHttpClient client2 = client.newBuilder().eventListener(EventListener.NONE).protocols(ONLY_HTTP1).build();
        final Request request = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        this.call = Internal.instance.newWebSocketCall(client2, request);
        this.call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) {
                try {
                    RealWebSocket.this.checkResponse(response);
                    StreamAllocation streamAllocation = Internal.instance.streamAllocation(call);
                    streamAllocation.noNewStreams();
                    Streams streams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
                    try {
                        RealWebSocket.this.listener.onOpen(RealWebSocket.this, response);
                        RealWebSocket.this.initReaderAndWriter("OkHttp WebSocket " + request.url().redact(), streams);
                        streamAllocation.connection().socket().setSoTimeout(0);
                        RealWebSocket.this.loopReader();
                    } catch (Exception e) {
                        RealWebSocket.this.failWebSocket(e, null);
                    }
                } catch (ProtocolException e2) {
                    RealWebSocket.this.failWebSocket(e2, response);
                    Util.closeQuietly((Closeable) response);
                }
            }

            public void onFailure(Call call, IOException e) {
                RealWebSocket.this.failWebSocket(e, null);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void checkResponse(Response response) throws ProtocolException {
        if (response.code() == 101) {
            String headerConnection = response.header("Connection");
            if ("Upgrade".equalsIgnoreCase(headerConnection)) {
                String headerUpgrade = response.header("Upgrade");
                if ("websocket".equalsIgnoreCase(headerUpgrade)) {
                    String headerAccept = response.header("Sec-WebSocket-Accept");
                    String acceptExpected = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
                    if (!acceptExpected.equals(headerAccept)) {
                        throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + acceptExpected + "' but was '" + headerAccept + "'");
                    }
                    return;
                }
                throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + headerUpgrade + "'");
            }
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + headerConnection + "'");
        }
        throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
    }

    public void initReaderAndWriter(String name, Streams streams2) throws IOException {
        synchronized (this) {
            this.streams = streams2;
            this.writer = new WebSocketWriter(streams2.client, streams2.sink, this.random);
            this.executor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(name, false));
            if (this.pingIntervalMillis != 0) {
                this.executor.scheduleAtFixedRate(new PingRunnable(), this.pingIntervalMillis, this.pingIntervalMillis, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                runWriter();
            }
        }
        this.reader = new WebSocketReader(streams2.client, streams2.source, this);
    }

    public void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            this.reader.processNextFrame();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean processNextFrame() throws IOException {
        boolean z = false;
        try {
            this.reader.processNextFrame();
            if (this.receivedCloseCode == -1) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            failWebSocket(e, null);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void awaitTermination(int timeout, TimeUnit timeUnit) throws InterruptedException {
        this.executor.awaitTermination((long) timeout, timeUnit);
    }

    /* access modifiers changed from: package-private */
    public void tearDown() throws InterruptedException {
        if (this.cancelFuture != null) {
            this.cancelFuture.cancel(false);
        }
        this.executor.shutdown();
        this.executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    /* access modifiers changed from: package-private */
    public synchronized int sentPingCount() {
        return this.sentPingCount;
    }

    /* access modifiers changed from: package-private */
    public synchronized int receivedPingCount() {
        return this.receivedPingCount;
    }

    /* access modifiers changed from: package-private */
    public synchronized int receivedPongCount() {
        return this.receivedPongCount;
    }

    public void onReadMessage(String text) throws IOException {
        this.listener.onMessage((WebSocket) this, text);
    }

    public void onReadMessage(ByteString bytes) throws IOException {
        this.listener.onMessage((WebSocket) this, bytes);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0023, code lost:
        return;
     */
    public synchronized void onReadPing(ByteString payload) {
        if (!this.failed) {
            if (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty()) {
                this.pongQueue.add(payload);
                runWriter();
                this.receivedPingCount++;
            }
        }
    }

    public synchronized void onReadPong(ByteString buffer) {
        this.receivedPongCount++;
        this.awaitingPong = false;
    }

    public void onReadClose(int code, String reason) {
        if (code != -1) {
            Streams toClose = null;
            synchronized (this) {
                if (this.receivedCloseCode == -1) {
                    this.receivedCloseCode = code;
                    this.receivedCloseReason = reason;
                    if (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
                        toClose = this.streams;
                        this.streams = null;
                        if (this.cancelFuture != null) {
                            this.cancelFuture.cancel(false);
                        }
                        this.executor.shutdown();
                    }
                } else {
                    throw new IllegalStateException("already closed");
                }
            }
            try {
                this.listener.onClosing(this, code, reason);
                if (toClose != null) {
                    this.listener.onClosed(this, code, reason);
                }
            } finally {
                Util.closeQuietly((Closeable) toClose);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean send(String text) {
        if (text != null) {
            return send(ByteString.encodeUtf8(text), 1);
        }
        throw new NullPointerException("text == null");
    }

    public boolean send(ByteString bytes) {
        if (bytes != null) {
            return send(bytes, 2);
        }
        throw new NullPointerException("bytes == null");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
        return false;
     */
    private synchronized boolean send(ByteString data, int formatOpcode) {
        if (!this.failed) {
            if (!this.enqueuedClose) {
                if (this.queueSize + ((long) data.size()) > MAX_QUEUE_SIZE) {
                    close(1001, null);
                    return false;
                }
                this.queueSize += (long) data.size();
                this.messageAndCloseQueue.add(new Message(formatOpcode, data));
                runWriter();
                return true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean pong(ByteString payload) {
        if (!this.failed) {
            if (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty()) {
                this.pongQueue.add(payload);
                runWriter();
                return true;
            }
        }
        return false;
    }

    public boolean close(int code, String reason) {
        return close(code, reason, CANCEL_AFTER_CLOSE_MILLIS);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean close(int code, String reason, long cancelAfterCloseMillis) {
        WebSocketProtocol.validateCloseCode(code);
        ByteString reasonBytes = null;
        if (reason != null) {
            reasonBytes = ByteString.encodeUtf8(reason);
            if (((long) reasonBytes.size()) > 123) {
                throw new IllegalArgumentException("reason.size() > 123: " + reason);
            }
        }
        if (!this.failed) {
            if (!this.enqueuedClose) {
                this.enqueuedClose = true;
                this.messageAndCloseQueue.add(new Close(code, reasonBytes, cancelAfterCloseMillis));
                runWriter();
                return true;
            }
        }
        return false;
    }

    private void runWriter() {
        if (this.executor != null) {
            this.executor.execute(this.writerRunnable);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.huawei.okhttp3.internal.ws.RealWebSocket$Close} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: com.huawei.okhttp3.internal.ws.RealWebSocket$Message} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: com.huawei.okhttp3.internal.ws.RealWebSocket$Close} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: com.huawei.okhttp3.internal.ws.RealWebSocket$Message} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: com.huawei.okhttp3.internal.ws.RealWebSocket$Close} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        r5 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0053, code lost:
        if (r5 == null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r4.writePong(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0059, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005d, code lost:
        if ((r0 instanceof com.huawei.okhttp3.internal.ws.RealWebSocket.Message) == false) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005f, code lost:
        r6 = r0.data;
        r7 = com.huawei.okio.Okio.buffer(r4.newMessageSink(r0.formatOpcode, (long) r6.size()));
        r7.write(r6);
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007c, code lost:
        monitor-enter(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r12.queueSize -= (long) r6.size();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0087, code lost:
        monitor-exit(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x008e, code lost:
        if ((r0 instanceof com.huawei.okhttp3.internal.ws.RealWebSocket.Close) == false) goto L_0x00a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0090, code lost:
        r6 = (com.huawei.okhttp3.internal.ws.RealWebSocket.Close) r0;
        r4.writeClose(r6.code, r6.reason);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x009a, code lost:
        if (r3 == null) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x009c, code lost:
        r12.listener.onClosed(r12, r1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00a2, code lost:
        com.huawei.okhttp3.internal.Util.closeQuietly((java.io.Closeable) r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a6, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00ac, code lost:
        throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ad, code lost:
        com.huawei.okhttp3.internal.Util.closeQuietly((java.io.Closeable) r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b0, code lost:
        throw r6;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    public boolean writeOneFrame() throws IOException {
        Message message = null;
        int receivedCloseCode2 = -1;
        String receivedCloseReason2 = null;
        Streams streamsToClose = null;
        synchronized (this) {
            if (this.failed) {
                return false;
            }
            WebSocketWriter writer2 = this.writer;
            ByteString pong = this.pongQueue.poll();
            if (pong == null) {
                message = this.messageAndCloseQueue.poll();
                if (message instanceof Close) {
                    receivedCloseCode2 = this.receivedCloseCode;
                    receivedCloseReason2 = this.receivedCloseReason;
                    if (receivedCloseCode2 != -1) {
                        streamsToClose = this.streams;
                        this.streams = null;
                        this.executor.shutdown();
                    } else {
                        this.cancelFuture = this.executor.schedule(new CancelRunnable(), message.cancelAfterCloseMillis, TimeUnit.MILLISECONDS);
                    }
                } else if (message == null) {
                    return false;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        if (r1 == -1) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001e, code lost:
        failWebSocket(new java.net.SocketTimeoutException("sent ping but didn't receive pong within " + r7.pingIntervalMillis + "ms (after " + (r1 - 1) + " successful ping/pongs)"), null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r0.writePing(com.huawei.okio.ByteString.EMPTY);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        failWebSocket(r2, null);
     */
    public void writePingFrame() {
        synchronized (this) {
            if (!this.failed) {
                WebSocketWriter writer2 = this.writer;
                int failedPing = this.awaitingPong ? this.sentPingCount : -1;
                this.sentPingCount++;
                this.awaitingPong = true;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r3.listener.onFailure(r3, r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002e, code lost:
        com.huawei.okhttp3.internal.Util.closeQuietly((java.io.Closeable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
        throw r1;
     */
    public void failWebSocket(Exception e, @Nullable Response response) {
        synchronized (this) {
            if (!this.failed) {
                this.failed = true;
                Streams streamsToClose = this.streams;
                this.streams = null;
                if (this.cancelFuture != null) {
                    this.cancelFuture.cancel(false);
                }
                if (this.executor != null) {
                    this.executor.shutdown();
                }
            }
        }
    }
}
