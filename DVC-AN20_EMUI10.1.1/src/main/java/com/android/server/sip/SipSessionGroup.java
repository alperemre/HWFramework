package com.android.server.sip;

import android.net.sip.ISipSession;
import android.net.sip.ISipSessionListener;
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.net.sip.SipSessionAdapter;
import android.telephony.Rlog;
import android.text.TextUtils;
import gov.nist.javax.sip.clientauthutils.AccountManager;
import gov.nist.javax.sip.clientauthutils.UserCredentials;
import gov.nist.javax.sip.header.ProxyAuthenticate;
import gov.nist.javax.sip.header.StatusLine;
import gov.nist.javax.sip.header.WWWAuthenticate;
import gov.nist.javax.sip.header.extensions.ReferredByHeader;
import gov.nist.javax.sip.header.extensions.ReplacesHeader;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.ObjectInUseException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.HeaderAddress;
import javax.sip.header.ReferToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;

/* access modifiers changed from: package-private */
public class SipSessionGroup implements SipListener {
    private static final String ANONYMOUS = "anonymous";
    private static final int CANCEL_CALL_TIMER = 3;
    private static final boolean DBG = false;
    private static final boolean DBG_PING = false;
    private static final EventObject DEREGISTER = new EventObject("Deregister");
    private static final EventObject END_CALL = new EventObject("End call");
    private static final int END_CALL_TIMER = 3;
    private static final int EXPIRY_TIME = 3600;
    private static final int INCALL_KEEPALIVE_INTERVAL = 10;
    private static final int KEEPALIVE_TIMEOUT = 5;
    private static final String TAG = "SipSession";
    private static final String THREAD_POOL_SIZE = "1";
    private static final long WAKE_LOCK_HOLDING_TIME = 500;
    private SipSessionImpl mCallReceiverSession;
    private String mExternalIp;
    private int mExternalPort;
    private String mLocalIp;
    private final SipProfile mLocalProfile;
    private final String mPassword;
    private Map<String, SipSessionImpl> mSessionMap = new HashMap();
    private SipHelper mSipHelper;
    private SipStack mSipStack;
    private SipWakeLock mWakeLock;
    private SipWakeupTimer mWakeupTimer;

    /* access modifiers changed from: package-private */
    public interface KeepAliveProcessCallback {
        void onError(int i, String str);

        void onResponse(boolean z);
    }

    public SipSessionGroup(SipProfile profile, String password, SipWakeupTimer timer, SipWakeLock wakeLock) throws SipException {
        this.mLocalProfile = profile;
        this.mPassword = password;
        this.mWakeupTimer = timer;
        this.mWakeLock = wakeLock;
        reset();
    }

    /* access modifiers changed from: package-private */
    public void setWakeupTimer(SipWakeupTimer timer) {
        this.mWakeupTimer = timer;
    }

    /* access modifiers changed from: package-private */
    public synchronized void reset() throws SipException {
        Properties properties = new Properties();
        String protocol = this.mLocalProfile.getProtocol();
        int port = this.mLocalProfile.getPort();
        String server = this.mLocalProfile.getProxyAddress();
        if (!TextUtils.isEmpty(server)) {
            properties.setProperty("javax.sip.OUTBOUND_PROXY", server + ':' + port + '/' + protocol);
        } else {
            server = this.mLocalProfile.getSipDomain();
        }
        if (server.startsWith("[") && server.endsWith("]")) {
            server = server.substring(1, server.length() - 1);
        }
        String local = null;
        try {
            InetAddress[] allByName = InetAddress.getAllByName(server);
            int length = allByName.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                InetAddress remote = allByName[i];
                DatagramSocket socket = new DatagramSocket();
                socket.connect(remote, port);
                if (socket.isConnected()) {
                    local = socket.getLocalAddress().getHostAddress();
                    port = socket.getLocalPort();
                    socket.close();
                    break;
                }
                socket.close();
                i++;
            }
        } catch (Exception e) {
        }
        if (local != null) {
            close();
            this.mLocalIp = local;
            properties.setProperty("javax.sip.STACK_NAME", getStackName());
            properties.setProperty("gov.nist.javax.sip.THREAD_POOL_SIZE", THREAD_POOL_SIZE);
            this.mSipStack = SipFactory.getInstance().createSipStack(properties);
            try {
                SipProvider provider = this.mSipStack.createSipProvider(this.mSipStack.createListeningPoint(local, port, protocol));
                provider.addSipListener(this);
                this.mSipHelper = new SipHelper(this.mSipStack, provider);
                this.mSipStack.start();
            } catch (SipException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new SipException("failed to initialize SIP stack", e3);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void onConnectivityChanged() {
        for (SipSessionImpl s : (SipSessionImpl[]) this.mSessionMap.values().toArray(new SipSessionImpl[this.mSessionMap.size()])) {
            s.onError(-10, "data connection lost");
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void resetExternalAddress() {
        this.mExternalIp = null;
        this.mExternalPort = 0;
    }

    public SipProfile getLocalProfile() {
        return this.mLocalProfile;
    }

    public String getLocalProfileUri() {
        return this.mLocalProfile.getUriString();
    }

    private String getStackName() {
        return "stack" + System.currentTimeMillis();
    }

    public synchronized void close() {
        onConnectivityChanged();
        this.mSessionMap.clear();
        closeToNotReceiveCalls();
        if (this.mSipStack != null) {
            this.mSipStack.stop();
            this.mSipStack = null;
            this.mSipHelper = null;
        }
        resetExternalAddress();
    }

    public synchronized boolean isClosed() {
        return this.mSipStack == null;
    }

    public synchronized void openToReceiveCalls(ISipSessionListener listener) {
        if (this.mCallReceiverSession == null) {
            this.mCallReceiverSession = new SipSessionCallReceiverImpl(listener);
        } else {
            this.mCallReceiverSession.setListener(listener);
        }
    }

    public synchronized void closeToNotReceiveCalls() {
        this.mCallReceiverSession = null;
    }

    public ISipSession createSession(ISipSessionListener listener) {
        if (isClosed()) {
            return null;
        }
        return new SipSessionImpl(listener);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean containsSession(String callId) {
        return this.mSessionMap.containsKey(callId);
    }

    private synchronized SipSessionImpl getSipSession(EventObject event) {
        SipSessionImpl session;
        session = this.mSessionMap.get(SipHelper.getCallId(event));
        if (session != null && isLoggable(session)) {
            for (String str : this.mSessionMap.keySet()) {
            }
        }
        return session != null ? session : this.mCallReceiverSession;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized void addSipSession(SipSessionImpl newSession) {
        removeSipSession(newSession);
        this.mSessionMap.put(newSession.getCallId(), newSession);
        if (isLoggable(newSession)) {
            for (String str : this.mSessionMap.keySet()) {
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized void removeSipSession(SipSessionImpl session) {
        if (session != this.mCallReceiverSession) {
            String key = session.getCallId();
            SipSessionImpl s = this.mSessionMap.remove(key);
            if (!(s == null || s == session)) {
                this.mSessionMap.put(key, s);
                for (Map.Entry<String, SipSessionImpl> entry : this.mSessionMap.entrySet()) {
                    if (entry.getValue() == s) {
                        this.mSessionMap.remove(entry.getKey());
                    }
                }
            }
            if (s != null && isLoggable(s)) {
                for (String str : this.mSessionMap.keySet()) {
                }
            }
        }
    }

    public void processRequest(RequestEvent event) {
        if (isRequestEvent("INVITE", event)) {
            this.mWakeLock.acquire(WAKE_LOCK_HOLDING_TIME);
        }
        process(event);
    }

    public void processResponse(ResponseEvent event) {
        process(event);
    }

    public void processIOException(IOExceptionEvent event) {
        process(event);
    }

    public void processTimeout(TimeoutEvent event) {
        process(event);
    }

    public void processTransactionTerminated(TransactionTerminatedEvent event) {
        process(event);
    }

    public void processDialogTerminated(DialogTerminatedEvent event) {
        process(event);
    }

    private synchronized void process(EventObject event) {
        SipSessionImpl session = getSipSession(event);
        try {
            boolean isLoggable = isLoggable(session, event);
            boolean processed = session != null && session.process(event);
            if (isLoggable && processed) {
                log("process: event new state after: " + SipSession.State.toString(session.mState));
            }
        } catch (Throwable e) {
            loge("process: error event=" + event, getRootCause(e));
            session.onError((SipSessionImpl) e);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String extractContent(Message message) {
        byte[] bytes = message.getRawContent();
        if (bytes == null) {
            return null;
        }
        try {
            if (message instanceof SIPMessage) {
                return ((SIPMessage) message).getMessageContent();
            }
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void extractExternalAddress(ResponseEvent evt) {
        ViaHeader viaHeader = evt.getResponse().getHeader("Via");
        if (viaHeader != null) {
            int rport = viaHeader.getRPort();
            String externalIp = viaHeader.getReceived();
            if (rport > 0 && externalIp != null) {
                this.mExternalIp = externalIp;
                this.mExternalPort = rport;
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Throwable getRootCause(Throwable exception) {
        Throwable cause = exception.getCause();
        while (cause != null) {
            exception = cause;
            cause = exception.getCause();
        }
        return exception;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private SipSessionImpl createNewSession(RequestEvent event, ISipSessionListener listener, ServerTransaction transaction, int newState) throws SipException {
        SipSessionImpl newSession = new SipSessionImpl(listener);
        newSession.mServerTransaction = transaction;
        newSession.mState = newState;
        newSession.mDialog = newSession.mServerTransaction.getDialog();
        newSession.mInviteReceived = event;
        newSession.mPeerProfile = createPeerProfile(event.getRequest().getHeader("From"));
        newSession.mPeerSessionDescription = extractContent(event.getRequest());
        return newSession;
    }

    private class SipSessionCallReceiverImpl extends SipSessionImpl {
        private static final boolean SSCRI_DBG = true;
        private static final String SSCRI_TAG = "SipSessionCallReceiverImpl";

        public SipSessionCallReceiverImpl(ISipSessionListener listener) {
            super(listener);
        }

        private int processInviteWithReplaces(RequestEvent event, ReplacesHeader replaces) {
            ReferredByHeader referredBy;
            SipSessionImpl session = (SipSessionImpl) SipSessionGroup.this.mSessionMap.get(replaces.getCallId());
            if (session == null) {
                return 481;
            }
            Dialog dialog = session.mDialog;
            if (dialog == null) {
                return 603;
            }
            if (!dialog.getLocalTag().equals(replaces.getToTag()) || !dialog.getRemoteTag().equals(replaces.getFromTag()) || (referredBy = event.getRequest().getHeader("Referred-By")) == null || !dialog.getRemoteParty().equals(referredBy.getAddress())) {
                return 481;
            }
            return 200;
        }

        private void processNewInviteRequest(RequestEvent event) throws SipException {
            ReplacesHeader replaces = (ReplacesHeader) event.getRequest().getHeader("Replaces");
            SipSessionImpl newSession = null;
            if (replaces != null) {
                int response = processInviteWithReplaces(event, replaces);
                log("processNewInviteRequest: " + replaces + " response=" + response);
                if (response == 200) {
                    newSession = SipSessionGroup.this.createNewSession(event, ((SipSessionImpl) SipSessionGroup.this.mSessionMap.get(replaces.getCallId())).mProxy.getListener(), SipSessionGroup.this.mSipHelper.getServerTransaction(event), 3);
                    newSession.mProxy.onCallTransferring(newSession, newSession.mPeerSessionDescription);
                } else {
                    SipSessionGroup.this.mSipHelper.sendResponse(event, response);
                }
            } else {
                newSession = SipSessionGroup.this.createNewSession(event, this.mProxy, SipSessionGroup.this.mSipHelper.sendRinging(event, generateTag()), 3);
                this.mProxy.onRinging(newSession, newSession.mPeerProfile, newSession.mPeerSessionDescription);
            }
            if (newSession != null) {
                SipSessionGroup.this.addSipSession(newSession);
            }
        }

        @Override // com.android.server.sip.SipSessionGroup.SipSessionImpl
        public boolean process(EventObject evt) throws SipException {
            if (SipSessionGroup.isLoggable(this, evt)) {
                log("process: " + this + ": " + SipSession.State.toString(this.mState) + ": processing " + SipSessionGroup.logEvt(evt));
            }
            if (SipSessionGroup.isRequestEvent("INVITE", evt)) {
                processNewInviteRequest((RequestEvent) evt);
                return SSCRI_DBG;
            } else if (!SipSessionGroup.isRequestEvent("OPTIONS", evt)) {
                return false;
            } else {
                SipSessionGroup.this.mSipHelper.sendResponse((RequestEvent) evt, 200);
                return SSCRI_DBG;
            }
        }

        private void log(String s) {
            Rlog.d(SSCRI_TAG, s);
        }
    }

    /* access modifiers changed from: package-private */
    public class SipSessionImpl extends ISipSession.Stub {
        private static final boolean SSI_DBG = true;
        private static final String SSI_TAG = "SipSessionImpl";
        int mAuthenticationRetryCount;
        ClientTransaction mClientTransaction;
        Dialog mDialog;
        boolean mInCall;
        RequestEvent mInviteReceived;
        SipProfile mPeerProfile;
        String mPeerSessionDescription;
        SipSessionListenerProxy mProxy = new SipSessionListenerProxy();
        SipSessionImpl mReferSession;
        ReferredByHeader mReferredBy;
        String mReplaces;
        ServerTransaction mServerTransaction;
        SessionTimer mSessionTimer;
        private SipKeepAlive mSipKeepAlive;
        private SipSessionImpl mSipSessionImpl;
        int mState = 0;

        /* access modifiers changed from: package-private */
        public class SessionTimer {
            private boolean mRunning = SipSessionImpl.SSI_DBG;

            SessionTimer() {
            }

            /* access modifiers changed from: package-private */
            public void start(final int timeout) {
                new Thread(new Runnable() {
                    /* class com.android.server.sip.SipSessionGroup.SipSessionImpl.SessionTimer.AnonymousClass1 */

                    public void run() {
                        SessionTimer.this.sleep(timeout);
                        if (SessionTimer.this.mRunning) {
                            SessionTimer.this.timeout();
                        }
                    }
                }, "SipSessionTimerThread").start();
            }

            /* access modifiers changed from: package-private */
            public synchronized void cancel() {
                this.mRunning = false;
                notify();
            }

            /* access modifiers changed from: private */
            /* access modifiers changed from: public */
            private void timeout() {
                synchronized (SipSessionGroup.this) {
                    SipSessionImpl.this.onError(-5, "Session timed out!");
                }
            }

            /* access modifiers changed from: private */
            /* access modifiers changed from: public */
            private synchronized void sleep(int timeout) {
                try {
                    wait((long) (timeout * 1000));
                } catch (InterruptedException e) {
                    SipSessionGroup.this.loge("session timer interrupted!", e);
                }
                return;
            }
        }

        public SipSessionImpl(ISipSessionListener listener) {
            setListener(listener);
        }

        /* access modifiers changed from: package-private */
        public SipSessionImpl duplicate() {
            return new SipSessionImpl(this.mProxy.getListener());
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void reset() {
            this.mInCall = false;
            SipSessionGroup.this.removeSipSession(this);
            this.mPeerProfile = null;
            this.mState = 0;
            this.mInviteReceived = null;
            this.mPeerSessionDescription = null;
            this.mAuthenticationRetryCount = 0;
            this.mReferSession = null;
            this.mReferredBy = null;
            this.mReplaces = null;
            Dialog dialog = this.mDialog;
            if (dialog != null) {
                dialog.delete();
            }
            this.mDialog = null;
            try {
                if (this.mServerTransaction != null) {
                    this.mServerTransaction.terminate();
                }
            } catch (ObjectInUseException e) {
            }
            this.mServerTransaction = null;
            try {
                if (this.mClientTransaction != null) {
                    this.mClientTransaction.terminate();
                }
            } catch (ObjectInUseException e2) {
            }
            this.mClientTransaction = null;
            cancelSessionTimer();
            SipSessionImpl sipSessionImpl = this.mSipSessionImpl;
            if (sipSessionImpl != null) {
                sipSessionImpl.stopKeepAliveProcess();
                this.mSipSessionImpl = null;
            }
        }

        @Override // android.net.sip.ISipSession
        public boolean isInCall() {
            return this.mInCall;
        }

        @Override // android.net.sip.ISipSession
        public String getLocalIp() {
            return SipSessionGroup.this.mLocalIp;
        }

        @Override // android.net.sip.ISipSession
        public SipProfile getLocalProfile() {
            return SipSessionGroup.this.mLocalProfile;
        }

        @Override // android.net.sip.ISipSession
        public SipProfile getPeerProfile() {
            return this.mPeerProfile;
        }

        @Override // android.net.sip.ISipSession
        public String getCallId() {
            return SipHelper.getCallId(getTransaction());
        }

        private Transaction getTransaction() {
            ClientTransaction clientTransaction = this.mClientTransaction;
            if (clientTransaction != null) {
                return clientTransaction;
            }
            ServerTransaction serverTransaction = this.mServerTransaction;
            if (serverTransaction != null) {
                return serverTransaction;
            }
            return null;
        }

        @Override // android.net.sip.ISipSession
        public int getState() {
            return this.mState;
        }

        @Override // android.net.sip.ISipSession
        public void setListener(ISipSessionListener listener) {
            ISipSessionListener iSipSessionListener;
            SipSessionListenerProxy sipSessionListenerProxy = this.mProxy;
            if (listener instanceof SipSessionListenerProxy) {
                iSipSessionListener = ((SipSessionListenerProxy) listener).getListener();
            } else {
                iSipSessionListener = listener;
            }
            sipSessionListenerProxy.setListener(iSipSessionListener);
        }

        private void doCommandAsync(final EventObject command) {
            new Thread(new Runnable() {
                /* class com.android.server.sip.SipSessionGroup.SipSessionImpl.AnonymousClass1 */

                public void run() {
                    try {
                        SipSessionImpl.this.processCommand(command);
                    } catch (Throwable e) {
                        SipSessionGroup sipSessionGroup = SipSessionGroup.this;
                        sipSessionGroup.loge("command error: " + command + ": " + SipSessionGroup.this.mLocalProfile.getUriString(), SipSessionGroup.this.getRootCause(e));
                        SipSessionImpl.this.onError((SipSessionImpl) e);
                    }
                }
            }, "SipSessionAsyncCmdThread").start();
        }

        @Override // android.net.sip.ISipSession
        public void makeCall(SipProfile peerProfile, String sessionDescription, int timeout) {
            doCommandAsync(new MakeCallCommand(peerProfile, sessionDescription, timeout));
        }

        @Override // android.net.sip.ISipSession
        public void answerCall(String sessionDescription, int timeout) {
            synchronized (SipSessionGroup.this) {
                if (this.mPeerProfile != null) {
                    doCommandAsync(new MakeCallCommand(this.mPeerProfile, sessionDescription, timeout));
                }
            }
        }

        @Override // android.net.sip.ISipSession
        public void endCall() {
            doCommandAsync(SipSessionGroup.END_CALL);
        }

        @Override // android.net.sip.ISipSession
        public void changeCall(String sessionDescription, int timeout) {
            synchronized (SipSessionGroup.this) {
                if (this.mPeerProfile != null) {
                    doCommandAsync(new MakeCallCommand(this.mPeerProfile, sessionDescription, timeout));
                }
            }
        }

        @Override // android.net.sip.ISipSession
        public void register(int duration) {
            doCommandAsync(new RegisterCommand(duration));
        }

        @Override // android.net.sip.ISipSession
        public void unregister() {
            doCommandAsync(SipSessionGroup.DEREGISTER);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void processCommand(EventObject command) throws SipException {
            if (SipSessionGroup.isLoggable(command)) {
                log("process cmd: " + command);
            }
            if (!process(command)) {
                onError(-9, "cannot initiate a new transaction to execute: " + command);
            }
        }

        /* access modifiers changed from: protected */
        public String generateTag() {
            return String.valueOf((long) (Math.random() * 4.294967296E9d));
        }

        public String toString() {
            try {
                String s = super.toString();
                return s.substring(s.indexOf("@")) + ":" + SipSession.State.toString(this.mState);
            } catch (Throwable th) {
                return super.toString();
            }
        }

        public boolean process(EventObject evt) throws SipException {
            boolean processed;
            if (SipSessionGroup.isLoggable(this, evt)) {
                log(" ~~~~~   " + this + ": " + SipSession.State.toString(this.mState) + ": processing " + SipSessionGroup.logEvt(evt));
            }
            synchronized (SipSessionGroup.this) {
                boolean z = false;
                if (SipSessionGroup.this.isClosed()) {
                    return false;
                }
                if (this.mSipKeepAlive != null && this.mSipKeepAlive.process(evt)) {
                    return SSI_DBG;
                }
                Dialog dialog = null;
                if (evt instanceof RequestEvent) {
                    dialog = ((RequestEvent) evt).getDialog();
                } else if (evt instanceof ResponseEvent) {
                    dialog = ((ResponseEvent) evt).getDialog();
                    SipSessionGroup.this.extractExternalAddress((ResponseEvent) evt);
                }
                if (dialog != null) {
                    this.mDialog = dialog;
                }
                switch (this.mState) {
                    case 0:
                        processed = readyForCall(evt);
                        break;
                    case 1:
                    case 2:
                        processed = registeringToReady(evt);
                        break;
                    case 3:
                        processed = incomingCall(evt);
                        break;
                    case SipSession.State.INCOMING_CALL_ANSWERING:
                        processed = incomingCallToInCall(evt);
                        break;
                    case 5:
                    case SipSession.State.OUTGOING_CALL_RING_BACK:
                        processed = outgoingCall(evt);
                        break;
                    case SipSession.State.OUTGOING_CALL_CANCELING:
                        processed = outgoingCallToReady(evt);
                        break;
                    case SipSession.State.IN_CALL:
                        processed = inCall(evt);
                        break;
                    case SipSession.State.PINGING:
                    default:
                        processed = false;
                        break;
                    case 10:
                        processed = endingCall(evt);
                        break;
                }
                if (processed || processExceptions(evt)) {
                    z = true;
                }
                return z;
            }
        }

        private boolean processExceptions(EventObject evt) throws SipException {
            if (SipSessionGroup.isRequestEvent("BYE", evt)) {
                SipSessionGroup.this.mSipHelper.sendResponse((RequestEvent) evt, 200);
                endCallNormally();
                return SSI_DBG;
            } else if (SipSessionGroup.isRequestEvent("CANCEL", evt)) {
                SipSessionGroup.this.mSipHelper.sendResponse((RequestEvent) evt, 481);
                return SSI_DBG;
            } else if (evt instanceof TransactionTerminatedEvent) {
                if (!isCurrentTransaction((TransactionTerminatedEvent) evt)) {
                    return false;
                }
                if (evt instanceof TimeoutEvent) {
                    processTimeout((TimeoutEvent) evt);
                } else {
                    processTransactionTerminated((TransactionTerminatedEvent) evt);
                }
                return SSI_DBG;
            } else if (SipSessionGroup.isRequestEvent("OPTIONS", evt)) {
                SipSessionGroup.this.mSipHelper.sendResponse((RequestEvent) evt, 200);
                return SSI_DBG;
            } else if (!(evt instanceof DialogTerminatedEvent)) {
                return false;
            } else {
                processDialogTerminated((DialogTerminatedEvent) evt);
                return SSI_DBG;
            }
        }

        private void processDialogTerminated(DialogTerminatedEvent event) {
            if (this.mDialog == event.getDialog()) {
                onError((Throwable) new SipException("dialog terminated"));
                return;
            }
            log("not the current dialog; current=" + this.mDialog + ", terminated=" + event.getDialog());
        }

        private boolean isCurrentTransaction(TransactionTerminatedEvent event) {
            Transaction current;
            Transaction target;
            if (event.isServerTransaction()) {
                current = this.mServerTransaction;
            } else {
                current = this.mClientTransaction;
            }
            if (event.isServerTransaction()) {
                target = event.getServerTransaction();
            } else {
                target = event.getClientTransaction();
            }
            if (current != target && this.mState != 9) {
                log("not the current transaction; current=" + toString(current) + ", target=" + toString(target));
                return false;
            } else if (current == null) {
                return SSI_DBG;
            } else {
                log("transaction terminated: " + toString(current));
                return SSI_DBG;
            }
        }

        private String toString(Transaction transaction) {
            if (transaction == null) {
                return "null";
            }
            Request request = transaction.getRequest();
            Dialog dialog = transaction.getDialog();
            Object[] objArr = new Object[4];
            objArr[0] = request.getMethod();
            objArr[1] = Long.valueOf(request.getHeader("CSeq").getSeqNumber());
            objArr[2] = transaction.getState();
            objArr[3] = dialog == null ? "-" : dialog.getState();
            return String.format("req=%s,%s,s=%s,ds=%s,", objArr);
        }

        private void processTransactionTerminated(TransactionTerminatedEvent event) {
            int i = this.mState;
            if (i == 0 || i == 8) {
                log("Transaction terminated; do nothing");
                return;
            }
            log("Transaction terminated early: " + this);
            onError(-3, "transaction terminated");
        }

        private void processTimeout(TimeoutEvent event) {
            log("processing Timeout...");
            int i = this.mState;
            if (i == 1 || i == 2) {
                reset();
                this.mProxy.onRegistrationTimeout(this);
            } else if (i == 3 || i == 4 || i == 5 || i == 7) {
                onError(-5, event.toString());
            } else {
                log("   do nothing");
            }
        }

        private int getExpiryTime(Response response) {
            int time = -1;
            ContactHeader contact = response.getHeader("Contact");
            if (contact != null) {
                time = contact.getExpires();
            }
            ExpiresHeader expires = response.getHeader("Expires");
            if (expires != null && (time < 0 || time > expires.getExpires())) {
                time = expires.getExpires();
            }
            if (time <= 0) {
                time = SipSessionGroup.EXPIRY_TIME;
            }
            ExpiresHeader expires2 = response.getHeader("Min-Expires");
            if (expires2 != null && time < expires2.getExpires()) {
                time = expires2.getExpires();
            }
            log("Expiry time = " + time);
            return time;
        }

        private boolean registeringToReady(EventObject evt) throws SipException {
            int i;
            if (!SipSessionGroup.expectResponse("REGISTER", evt)) {
                return false;
            }
            ResponseEvent event = (ResponseEvent) evt;
            Response response = event.getResponse();
            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                if (this.mState == 1) {
                    i = getExpiryTime(((ResponseEvent) evt).getResponse());
                } else {
                    i = -1;
                }
                onRegistrationDone(i);
                return SSI_DBG;
            } else if (statusCode == 401 || statusCode == 407) {
                handleAuthentication(event);
                return SSI_DBG;
            } else if (statusCode < 500) {
                return false;
            } else {
                onRegistrationFailed(response);
                return SSI_DBG;
            }
        }

        private boolean handleAuthentication(ResponseEvent event) throws SipException {
            Response response = event.getResponse();
            if (getNonceFromResponse(response) == null) {
                onError(-2, "server does not provide challenge");
                return false;
            } else if (this.mAuthenticationRetryCount < 2) {
                this.mClientTransaction = SipSessionGroup.this.mSipHelper.handleChallenge(event, getAccountManager());
                this.mDialog = this.mClientTransaction.getDialog();
                this.mAuthenticationRetryCount++;
                if (SipSessionGroup.isLoggable(this, event)) {
                    log("   authentication retry count=" + this.mAuthenticationRetryCount);
                }
                return SSI_DBG;
            } else {
                if (crossDomainAuthenticationRequired(response)) {
                    onError(-11, getRealmFromResponse(response));
                } else {
                    onError(-8, "incorrect username or password");
                }
                return false;
            }
        }

        private boolean crossDomainAuthenticationRequired(Response response) {
            String realm = getRealmFromResponse(response);
            if (realm == null) {
                realm = "";
            }
            return SipSessionGroup.this.mLocalProfile.getSipDomain().trim().equals(realm.trim()) ^ SSI_DBG;
        }

        private AccountManager getAccountManager() {
            return new AccountManager() {
                /* class com.android.server.sip.SipSessionGroup.SipSessionImpl.AnonymousClass2 */

                public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
                    return new UserCredentials() {
                        /* class com.android.server.sip.SipSessionGroup.SipSessionImpl.AnonymousClass2.AnonymousClass1 */

                        public String getUserName() {
                            String username = SipSessionGroup.this.mLocalProfile.getAuthUserName();
                            if (!TextUtils.isEmpty(username)) {
                                return username;
                            }
                            return SipSessionGroup.this.mLocalProfile.getUserName();
                        }

                        public String getPassword() {
                            return SipSessionGroup.this.mPassword;
                        }

                        public String getSipDomain() {
                            return SipSessionGroup.this.mLocalProfile.getSipDomain();
                        }
                    };
                }
            };
        }

        private String getRealmFromResponse(Response response) {
            WWWAuthenticate wwwAuth = response.getHeader("WWW-Authenticate");
            if (wwwAuth != null) {
                return wwwAuth.getRealm();
            }
            ProxyAuthenticate proxyAuth = response.getHeader("Proxy-Authenticate");
            if (proxyAuth == null) {
                return null;
            }
            return proxyAuth.getRealm();
        }

        private String getNonceFromResponse(Response response) {
            WWWAuthenticate wwwAuth = response.getHeader("WWW-Authenticate");
            if (wwwAuth != null) {
                return wwwAuth.getNonce();
            }
            ProxyAuthenticate proxyAuth = response.getHeader("Proxy-Authenticate");
            if (proxyAuth == null) {
                return null;
            }
            return proxyAuth.getNonce();
        }

        private String getResponseString(int statusCode) {
            StatusLine statusLine = new StatusLine();
            statusLine.setStatusCode(statusCode);
            statusLine.setReasonPhrase(SIPResponse.getReasonPhrase(statusCode));
            return statusLine.encode();
        }

        private boolean readyForCall(EventObject evt) throws SipException {
            if (evt instanceof MakeCallCommand) {
                this.mState = 5;
                MakeCallCommand cmd = (MakeCallCommand) evt;
                this.mPeerProfile = cmd.getPeerProfile();
                if (this.mReferSession != null) {
                    SipSessionGroup.this.mSipHelper.sendReferNotify(this.mReferSession.mDialog, getResponseString(100));
                }
                this.mClientTransaction = SipSessionGroup.this.mSipHelper.sendInvite(SipSessionGroup.this.mLocalProfile, this.mPeerProfile, cmd.getSessionDescription(), generateTag(), this.mReferredBy, this.mReplaces);
                this.mDialog = this.mClientTransaction.getDialog();
                SipSessionGroup.this.addSipSession(this);
                startSessionTimer(cmd.getTimeout());
                this.mProxy.onCalling(this);
                return SSI_DBG;
            } else if (evt instanceof RegisterCommand) {
                this.mState = 1;
                this.mClientTransaction = SipSessionGroup.this.mSipHelper.sendRegister(SipSessionGroup.this.mLocalProfile, generateTag(), ((RegisterCommand) evt).getDuration());
                this.mDialog = this.mClientTransaction.getDialog();
                SipSessionGroup.this.addSipSession(this);
                this.mProxy.onRegistering(this);
                return SSI_DBG;
            } else if (SipSessionGroup.DEREGISTER != evt) {
                return false;
            } else {
                this.mState = 2;
                this.mClientTransaction = SipSessionGroup.this.mSipHelper.sendRegister(SipSessionGroup.this.mLocalProfile, generateTag(), 0);
                this.mDialog = this.mClientTransaction.getDialog();
                SipSessionGroup.this.addSipSession(this);
                this.mProxy.onRegistering(this);
                return SSI_DBG;
            }
        }

        private boolean incomingCall(EventObject evt) throws SipException {
            if (evt instanceof MakeCallCommand) {
                this.mState = 4;
                this.mServerTransaction = SipSessionGroup.this.mSipHelper.sendInviteOk(this.mInviteReceived, SipSessionGroup.this.mLocalProfile, ((MakeCallCommand) evt).getSessionDescription(), this.mServerTransaction, SipSessionGroup.this.mExternalIp, SipSessionGroup.this.mExternalPort);
                startSessionTimer(((MakeCallCommand) evt).getTimeout());
                return SSI_DBG;
            } else if (SipSessionGroup.END_CALL == evt) {
                SipSessionGroup.this.mSipHelper.sendInviteBusyHere(this.mInviteReceived, this.mServerTransaction);
                endCallNormally();
                return SSI_DBG;
            } else if (!SipSessionGroup.isRequestEvent("CANCEL", evt)) {
                return false;
            } else {
                SipSessionGroup.this.mSipHelper.sendResponse((RequestEvent) evt, 200);
                SipSessionGroup.this.mSipHelper.sendInviteRequestTerminated(this.mInviteReceived.getRequest(), this.mServerTransaction);
                endCallNormally();
                return SSI_DBG;
            }
        }

        private boolean incomingCallToInCall(EventObject evt) {
            if (SipSessionGroup.isRequestEvent("ACK", evt)) {
                String sdp = SipSessionGroup.this.extractContent(((RequestEvent) evt).getRequest());
                if (sdp != null) {
                    this.mPeerSessionDescription = sdp;
                }
                if (this.mPeerSessionDescription == null) {
                    onError(-4, "peer sdp is empty");
                } else {
                    establishCall(false);
                }
                return SSI_DBG;
            } else if (SipSessionGroup.isRequestEvent("CANCEL", evt)) {
                return SSI_DBG;
            } else {
                return false;
            }
        }

        private boolean outgoingCall(EventObject evt) throws SipException {
            if (SipSessionGroup.expectResponse("INVITE", evt)) {
                ResponseEvent event = (ResponseEvent) evt;
                Response response = event.getResponse();
                int statusCode = response.getStatusCode();
                if (statusCode == 200) {
                    if (this.mReferSession != null) {
                        SipSessionGroup.this.mSipHelper.sendReferNotify(this.mReferSession.mDialog, getResponseString(200));
                        this.mReferSession = null;
                    }
                    SipSessionGroup.this.mSipHelper.sendInviteAck(event, this.mDialog);
                    this.mPeerSessionDescription = SipSessionGroup.this.extractContent(response);
                    establishCall(SSI_DBG);
                    return SSI_DBG;
                } else if (statusCode == 401 || statusCode == 407) {
                    if (handleAuthentication(event)) {
                        SipSessionGroup.this.addSipSession(this);
                    }
                    return SSI_DBG;
                } else if (statusCode == 491) {
                    return SSI_DBG;
                } else {
                    switch (statusCode) {
                        case 180:
                        case 181:
                        case 182:
                        case 183:
                            if (this.mState == 5) {
                                this.mState = 6;
                                cancelSessionTimer();
                                this.mProxy.onRingingBack(this);
                            }
                            return SSI_DBG;
                        default:
                            if (this.mReferSession != null) {
                                SipSessionGroup.this.mSipHelper.sendReferNotify(this.mReferSession.mDialog, getResponseString(503));
                            }
                            if (statusCode >= 400) {
                                onError(response);
                                return SSI_DBG;
                            } else if (statusCode >= 300) {
                                return false;
                            } else {
                                return SSI_DBG;
                            }
                    }
                }
            } else if (SipSessionGroup.END_CALL == evt) {
                this.mState = 7;
                SipSessionGroup.this.mSipHelper.sendCancel(this.mClientTransaction);
                startSessionTimer(3);
                return SSI_DBG;
            } else if (!SipSessionGroup.isRequestEvent("INVITE", evt)) {
                return false;
            } else {
                RequestEvent event2 = (RequestEvent) evt;
                SipSessionGroup.this.mSipHelper.sendInviteBusyHere(event2, event2.getServerTransaction());
                return SSI_DBG;
            }
        }

        private boolean outgoingCallToReady(EventObject evt) throws SipException {
            if (evt instanceof ResponseEvent) {
                Response response = ((ResponseEvent) evt).getResponse();
                int statusCode = response.getStatusCode();
                if (SipSessionGroup.expectResponse("CANCEL", evt)) {
                    if (statusCode == 200) {
                        return SSI_DBG;
                    }
                } else if (!SipSessionGroup.expectResponse("INVITE", evt)) {
                    return false;
                } else {
                    if (statusCode == 200) {
                        outgoingCall(evt);
                        return SSI_DBG;
                    } else if (statusCode == 487) {
                        endCallNormally();
                        return SSI_DBG;
                    }
                }
                if (statusCode >= 400) {
                    onError(response);
                    return SSI_DBG;
                }
            } else if (evt instanceof TransactionTerminatedEvent) {
                onError((Throwable) new SipException("timed out"));
            }
            return false;
        }

        private boolean processReferRequest(RequestEvent event) throws SipException {
            try {
                ReferToHeader referto = event.getRequest().getHeader("Refer-To");
                SipURI uri = referto.getAddress().getURI();
                String replacesHeader = uri.getHeader("Replaces");
                if (uri.getUser() == null) {
                    SipSessionGroup.this.mSipHelper.sendResponse(event, 400);
                    return false;
                }
                SipSessionGroup.this.mSipHelper.sendResponse(event, 202);
                SipSessionImpl newSession = SipSessionGroup.this.createNewSession(event, this.mProxy.getListener(), SipSessionGroup.this.mSipHelper.getServerTransaction(event), 0);
                newSession.mReferSession = this;
                newSession.mReferredBy = event.getRequest().getHeader("Referred-By");
                newSession.mReplaces = replacesHeader;
                newSession.mPeerProfile = SipSessionGroup.createPeerProfile(referto);
                newSession.mProxy.onCallTransferring(newSession, null);
                return SSI_DBG;
            } catch (IllegalArgumentException e) {
                throw new SipException("createPeerProfile()", e);
            }
        }

        private boolean inCall(EventObject evt) throws SipException {
            if (SipSessionGroup.END_CALL == evt) {
                this.mState = 10;
                SipSessionGroup.this.mSipHelper.sendBye(this.mDialog);
                this.mProxy.onCallEnded(this);
                startSessionTimer(3);
                return SSI_DBG;
            } else if (SipSessionGroup.isRequestEvent("INVITE", evt)) {
                this.mState = 3;
                RequestEvent event = (RequestEvent) evt;
                this.mInviteReceived = event;
                this.mPeerSessionDescription = SipSessionGroup.this.extractContent(event.getRequest());
                this.mServerTransaction = null;
                this.mProxy.onRinging(this, this.mPeerProfile, this.mPeerSessionDescription);
                return SSI_DBG;
            } else if (SipSessionGroup.isRequestEvent("BYE", evt)) {
                SipSessionGroup.this.mSipHelper.sendResponse((RequestEvent) evt, 200);
                endCallNormally();
                return SSI_DBG;
            } else if (SipSessionGroup.isRequestEvent("REFER", evt)) {
                return processReferRequest((RequestEvent) evt);
            } else {
                if (evt instanceof MakeCallCommand) {
                    this.mState = 5;
                    this.mClientTransaction = SipSessionGroup.this.mSipHelper.sendReinvite(this.mDialog, ((MakeCallCommand) evt).getSessionDescription());
                    startSessionTimer(((MakeCallCommand) evt).getTimeout());
                    return SSI_DBG;
                } else if (!(evt instanceof ResponseEvent) || !SipSessionGroup.expectResponse("NOTIFY", evt)) {
                    return false;
                } else {
                    return SSI_DBG;
                }
            }
        }

        private boolean endingCall(EventObject evt) throws SipException {
            if (!SipSessionGroup.expectResponse("BYE", evt)) {
                return false;
            }
            ResponseEvent event = (ResponseEvent) evt;
            int statusCode = event.getResponse().getStatusCode();
            if ((statusCode == 401 || statusCode == 407) && handleAuthentication(event)) {
                return SSI_DBG;
            }
            cancelSessionTimer();
            reset();
            return SSI_DBG;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void startSessionTimer(int timeout) {
            if (timeout > 0) {
                this.mSessionTimer = new SessionTimer();
                this.mSessionTimer.start(timeout);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void cancelSessionTimer() {
            SessionTimer sessionTimer = this.mSessionTimer;
            if (sessionTimer != null) {
                sessionTimer.cancel();
                this.mSessionTimer = null;
            }
        }

        private String createErrorMessage(Response response) {
            return String.format("%s (%d)", response.getReasonPhrase(), Integer.valueOf(response.getStatusCode()));
        }

        private void enableKeepAlive() {
            SipSessionImpl sipSessionImpl = this.mSipSessionImpl;
            if (sipSessionImpl != null) {
                sipSessionImpl.stopKeepAliveProcess();
            } else {
                this.mSipSessionImpl = duplicate();
            }
            try {
                this.mSipSessionImpl.startKeepAliveProcess(10, this.mPeerProfile, null);
            } catch (SipException e) {
                SipSessionGroup.this.loge("keepalive cannot be enabled; ignored", e);
                this.mSipSessionImpl.stopKeepAliveProcess();
            }
        }

        private void establishCall(boolean enableKeepAlive) {
            this.mState = 8;
            cancelSessionTimer();
            if (!this.mInCall && enableKeepAlive) {
                enableKeepAlive();
            }
            this.mInCall = SSI_DBG;
            this.mProxy.onCallEstablished(this, this.mPeerSessionDescription);
        }

        private void endCallNormally() {
            reset();
            this.mProxy.onCallEnded(this);
        }

        private void endCallOnError(int errorCode, String message) {
            reset();
            this.mProxy.onError(this, errorCode, message);
        }

        private void endCallOnBusy() {
            reset();
            this.mProxy.onCallBusy(this);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void onError(int errorCode, String message) {
            cancelSessionTimer();
            int i = this.mState;
            if (i == 1 || i == 2) {
                onRegistrationFailed(errorCode, message);
            } else {
                endCallOnError(errorCode, message);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void onError(Throwable exception) {
            Throwable exception2 = SipSessionGroup.this.getRootCause(exception);
            onError(getErrorCode(exception2), exception2.toString());
        }

        private void onError(Response response) {
            int statusCode = response.getStatusCode();
            if (this.mInCall || statusCode != 486) {
                onError(getErrorCode(statusCode), createErrorMessage(response));
            } else {
                endCallOnBusy();
            }
        }

        private int getErrorCode(int responseStatusCode) {
            if (responseStatusCode == 403 || responseStatusCode == 404 || responseStatusCode == 406) {
                return -7;
            }
            if (responseStatusCode == 408) {
                return -5;
            }
            if (responseStatusCode == 410) {
                return -7;
            }
            if (responseStatusCode == 414) {
                return -6;
            }
            if (responseStatusCode == 480 || responseStatusCode == 488) {
                return -7;
            }
            if (responseStatusCode == 484 || responseStatusCode == 485) {
                return -6;
            }
            if (responseStatusCode < 500) {
                return -4;
            }
            return -2;
        }

        private int getErrorCode(Throwable exception) {
            exception.getMessage();
            if (exception instanceof UnknownHostException) {
                return -12;
            }
            if (exception instanceof IOException) {
                return -1;
            }
            return -4;
        }

        private void onRegistrationDone(int duration) {
            reset();
            this.mProxy.onRegistrationDone(this, duration);
        }

        private void onRegistrationFailed(int errorCode, String message) {
            reset();
            this.mProxy.onRegistrationFailed(this, errorCode, message);
        }

        private void onRegistrationFailed(Response response) {
            onRegistrationFailed(getErrorCode(response.getStatusCode()), createErrorMessage(response));
        }

        public void startKeepAliveProcess(int interval, KeepAliveProcessCallback callback) throws SipException {
            synchronized (SipSessionGroup.this) {
                startKeepAliveProcess(interval, SipSessionGroup.this.mLocalProfile, callback);
            }
        }

        public void startKeepAliveProcess(int interval, SipProfile peerProfile, KeepAliveProcessCallback callback) throws SipException {
            synchronized (SipSessionGroup.this) {
                if (this.mSipKeepAlive == null) {
                    this.mPeerProfile = peerProfile;
                    this.mSipKeepAlive = new SipKeepAlive();
                    this.mProxy.setListener(this.mSipKeepAlive);
                    this.mSipKeepAlive.start(interval, callback);
                } else {
                    throw new SipException("Cannot create more than one keepalive process in a SipSession");
                }
            }
        }

        public void stopKeepAliveProcess() {
            synchronized (SipSessionGroup.this) {
                if (this.mSipKeepAlive != null) {
                    this.mSipKeepAlive.stop();
                    this.mSipKeepAlive = null;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public class SipKeepAlive extends SipSessionAdapter implements Runnable {
            private static final boolean SKA_DBG = true;
            private static final String SKA_TAG = "SipKeepAlive";
            private KeepAliveProcessCallback mCallback;
            private int mInterval;
            private boolean mPortChanged = false;
            private int mRPort = 0;
            private boolean mRunning = false;

            SipKeepAlive() {
            }

            /* access modifiers changed from: package-private */
            public void start(int interval, KeepAliveProcessCallback callback) {
                if (!this.mRunning) {
                    this.mRunning = SKA_DBG;
                    this.mInterval = interval;
                    this.mCallback = new KeepAliveProcessCallbackProxy(callback);
                    SipSessionGroup.this.mWakeupTimer.set(interval * 1000, this);
                    log("start keepalive:" + SipSessionGroup.this.mLocalProfile.getUriString());
                    run();
                }
            }

            /* access modifiers changed from: package-private */
            public boolean process(EventObject evt) {
                if (!this.mRunning || SipSessionImpl.this.mState != 9 || !(evt instanceof ResponseEvent) || !parseOptionsResult(evt)) {
                    return false;
                }
                if (this.mPortChanged) {
                    SipSessionGroup.this.resetExternalAddress();
                    stop();
                } else {
                    SipSessionImpl.this.cancelSessionTimer();
                    SipSessionGroup.this.removeSipSession(SipSessionImpl.this);
                }
                this.mCallback.onResponse(this.mPortChanged);
                return SKA_DBG;
            }

            @Override // android.net.sip.SipSessionAdapter, android.net.sip.ISipSessionListener
            public void onError(ISipSession session, int errorCode, String message) {
                stop();
                this.mCallback.onError(errorCode, message);
            }

            public void run() {
                synchronized (SipSessionGroup.this) {
                    if (this.mRunning) {
                        try {
                            sendKeepAlive();
                        } catch (Throwable t) {
                            SipSessionGroup sipSessionGroup = SipSessionGroup.this;
                            sipSessionGroup.loge("keepalive error: " + SipSessionGroup.this.mLocalProfile.getUriString(), SipSessionGroup.this.getRootCause(t));
                            if (this.mRunning) {
                                SipSessionImpl.this.onError((SipSessionImpl) t);
                            }
                        }
                    }
                }
            }

            /* access modifiers changed from: package-private */
            public void stop() {
                synchronized (SipSessionGroup.this) {
                    log("stop keepalive:" + SipSessionGroup.this.mLocalProfile.getUriString() + ",RPort=" + this.mRPort);
                    this.mRunning = false;
                    SipSessionGroup.this.mWakeupTimer.cancel(this);
                    SipSessionImpl.this.reset();
                }
            }

            private void sendKeepAlive() throws SipException {
                synchronized (SipSessionGroup.this) {
                    SipSessionImpl.this.mState = 9;
                    SipSessionImpl.this.mClientTransaction = SipSessionGroup.this.mSipHelper.sendOptions(SipSessionGroup.this.mLocalProfile, SipSessionImpl.this.mPeerProfile, SipSessionImpl.this.generateTag());
                    SipSessionImpl.this.mDialog = SipSessionImpl.this.mClientTransaction.getDialog();
                    SipSessionGroup.this.addSipSession(SipSessionImpl.this);
                    SipSessionImpl.this.startSessionTimer(5);
                }
            }

            private boolean parseOptionsResult(EventObject evt) {
                if (!SipSessionGroup.expectResponse("OPTIONS", evt)) {
                    return false;
                }
                int rPort = getRPortFromResponse(((ResponseEvent) evt).getResponse());
                if (rPort != -1) {
                    if (this.mRPort == 0) {
                        this.mRPort = rPort;
                    }
                    int i = this.mRPort;
                    if (i != rPort) {
                        this.mPortChanged = SKA_DBG;
                        log(String.format("rport is changed: %d <> %d", Integer.valueOf(i), Integer.valueOf(rPort)));
                        this.mRPort = rPort;
                    } else {
                        log("rport is the same: " + rPort);
                    }
                } else {
                    log("peer did not respond rport");
                }
                return SKA_DBG;
            }

            private int getRPortFromResponse(Response response) {
                ViaHeader viaHeader = response.getHeader("Via");
                if (viaHeader == null) {
                    return -1;
                }
                return viaHeader.getRPort();
            }

            private void log(String s) {
                Rlog.d(SKA_TAG, s);
            }
        }

        private void log(String s) {
            Rlog.d(SSI_TAG, s);
        }
    }

    /* access modifiers changed from: private */
    public static boolean isRequestEvent(String method, EventObject event) {
        try {
            if (event instanceof RequestEvent) {
                return method.equals(((RequestEvent) event).getRequest().getMethod());
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static String getCseqMethod(Message message) {
        return message.getHeader("CSeq").getMethod();
    }

    /* access modifiers changed from: private */
    public static boolean expectResponse(String expectedMethod, EventObject evt) {
        if (evt instanceof ResponseEvent) {
            return expectedMethod.equalsIgnoreCase(getCseqMethod(((ResponseEvent) evt).getResponse()));
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static SipProfile createPeerProfile(HeaderAddress header) throws SipException {
        try {
            Address address = header.getAddress();
            SipURI uri = address.getURI();
            String username = uri.getUser();
            if (username == null) {
                username = ANONYMOUS;
            }
            int port = uri.getPort();
            SipProfile.Builder builder = new SipProfile.Builder(username, uri.getHost()).setDisplayName(address.getDisplayName());
            if (port > 0) {
                builder.setPort(port);
            }
            return builder.build();
        } catch (IllegalArgumentException e) {
            throw new SipException("createPeerProfile()", e);
        } catch (ParseException e2) {
            throw new SipException("createPeerProfile()", e2);
        }
    }

    private static boolean isLoggable(SipSessionImpl s) {
        return (s == null || s.mState != 9) ? false : false;
    }

    /* access modifiers changed from: private */
    public static boolean isLoggable(EventObject evt) {
        return isLoggable(null, evt);
    }

    /* access modifiers changed from: private */
    public static boolean isLoggable(SipSessionImpl s, EventObject evt) {
        if (isLoggable(s) && evt != null) {
            return evt instanceof ResponseEvent ? "OPTIONS".equals(((ResponseEvent) evt).getResponse().getHeader("CSeq")) ? false : false : (!(evt instanceof RequestEvent) || !isRequestEvent("OPTIONS", evt)) ? false : false;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static String logEvt(EventObject evt) {
        if (evt instanceof RequestEvent) {
            return ((RequestEvent) evt).getRequest().toString();
        }
        if (evt instanceof ResponseEvent) {
            return ((ResponseEvent) evt).getResponse().toString();
        }
        return evt.toString();
    }

    /* access modifiers changed from: private */
    public class RegisterCommand extends EventObject {
        private int mDuration;

        public RegisterCommand(int duration) {
            super(SipSessionGroup.this);
            this.mDuration = duration;
        }

        public int getDuration() {
            return this.mDuration;
        }
    }

    /* access modifiers changed from: private */
    public class MakeCallCommand extends EventObject {
        private String mSessionDescription;
        private int mTimeout;

        public MakeCallCommand(SipProfile peerProfile, String sessionDescription, int timeout) {
            super(peerProfile);
            this.mSessionDescription = sessionDescription;
            this.mTimeout = timeout;
        }

        public SipProfile getPeerProfile() {
            return (SipProfile) getSource();
        }

        public String getSessionDescription() {
            return this.mSessionDescription;
        }

        public int getTimeout() {
            return this.mTimeout;
        }
    }

    /* access modifiers changed from: package-private */
    public static class KeepAliveProcessCallbackProxy implements KeepAliveProcessCallback {
        private static final String KAPCP_TAG = "KeepAliveProcessCallbackProxy";
        private KeepAliveProcessCallback mCallback;

        KeepAliveProcessCallbackProxy(KeepAliveProcessCallback callback) {
            this.mCallback = callback;
        }

        private void proxy(Runnable runnable) {
            new Thread(runnable, "SIP-KeepAliveProcessCallbackThread").start();
        }

        @Override // com.android.server.sip.SipSessionGroup.KeepAliveProcessCallback
        public void onResponse(final boolean portChanged) {
            if (this.mCallback != null) {
                proxy(new Runnable() {
                    /* class com.android.server.sip.SipSessionGroup.KeepAliveProcessCallbackProxy.AnonymousClass1 */

                    public void run() {
                        try {
                            KeepAliveProcessCallbackProxy.this.mCallback.onResponse(portChanged);
                        } catch (Throwable t) {
                            KeepAliveProcessCallbackProxy.this.loge("onResponse", t);
                        }
                    }
                });
            }
        }

        @Override // com.android.server.sip.SipSessionGroup.KeepAliveProcessCallback
        public void onError(final int errorCode, final String description) {
            if (this.mCallback != null) {
                proxy(new Runnable() {
                    /* class com.android.server.sip.SipSessionGroup.KeepAliveProcessCallbackProxy.AnonymousClass2 */

                    public void run() {
                        try {
                            KeepAliveProcessCallbackProxy.this.mCallback.onError(errorCode, description);
                        } catch (Throwable t) {
                            KeepAliveProcessCallbackProxy.this.loge("onError", t);
                        }
                    }
                });
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void loge(String s, Throwable t) {
            Rlog.e(KAPCP_TAG, s, t);
        }
    }

    private void log(String s) {
        Rlog.d(TAG, s);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loge(String s, Throwable t) {
        Rlog.e(TAG, s, t);
    }
}
