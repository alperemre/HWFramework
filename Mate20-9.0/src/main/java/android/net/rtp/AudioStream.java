package android.net.rtp;

import java.net.InetAddress;
import java.net.SocketException;

public class AudioStream extends RtpStream {
    private AudioCodec mCodec;
    private int mDtmfType = -1;
    private AudioGroup mGroup;

    public AudioStream(InetAddress address) throws SocketException {
        super(address);
    }

    public final boolean isBusy() {
        return this.mGroup != null;
    }

    public AudioGroup getGroup() {
        return this.mGroup;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        return;
     */
    public void join(AudioGroup group) {
        synchronized (this) {
            if (this.mGroup != group) {
                if (this.mGroup != null) {
                    this.mGroup.remove(this);
                    this.mGroup = null;
                }
                if (group != null) {
                    group.add(this);
                    this.mGroup = group;
                }
            }
        }
    }

    public AudioCodec getCodec() {
        return this.mCodec;
    }

    public void setCodec(AudioCodec codec) {
        if (isBusy()) {
            throw new IllegalStateException("Busy");
        } else if (codec.type != this.mDtmfType) {
            this.mCodec = codec;
        } else {
            throw new IllegalArgumentException("The type is used by DTMF");
        }
    }

    public int getDtmfType() {
        return this.mDtmfType;
    }

    public void setDtmfType(int type) {
        if (!isBusy()) {
            if (type != -1) {
                if (type < 96 || type > 127) {
                    throw new IllegalArgumentException("Invalid type");
                } else if (this.mCodec != null && type == this.mCodec.type) {
                    throw new IllegalArgumentException("The type is used by codec");
                }
            }
            this.mDtmfType = type;
            return;
        }
        throw new IllegalStateException("Busy");
    }
}
