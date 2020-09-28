package com.huawei.okhttp3.internal.cache;

import com.huawei.okio.Buffer;
import com.huawei.okio.ForwardingSink;
import com.huawei.okio.Sink;
import java.io.IOException;

/* access modifiers changed from: package-private */
public class FaultHidingSink extends ForwardingSink {
    private boolean hasErrors;

    FaultHidingSink(Sink delegate) {
        super(delegate);
    }

    @Override // com.huawei.okio.Sink, com.huawei.okio.ForwardingSink
    public void write(Buffer source, long byteCount) throws IOException {
        if (this.hasErrors) {
            source.skip(byteCount);
            return;
        }
        try {
            super.write(source, byteCount);
        } catch (IOException e) {
            this.hasErrors = true;
            onException(e);
        }
    }

    @Override // com.huawei.okio.Sink, com.huawei.okio.ForwardingSink, java.io.Flushable
    public void flush() throws IOException {
        if (!this.hasErrors) {
            try {
                super.flush();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    @Override // java.io.Closeable, com.huawei.okio.Sink, java.lang.AutoCloseable, com.huawei.okio.ForwardingSink
    public void close() throws IOException {
        if (!this.hasErrors) {
            try {
                super.close();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onException(IOException e) {
    }
}
