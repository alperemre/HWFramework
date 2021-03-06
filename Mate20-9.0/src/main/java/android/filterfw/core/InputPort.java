package android.filterfw.core;

public abstract class InputPort extends FilterPort {
    protected OutputPort mSourcePort;

    public abstract void transfer(FilterContext filterContext);

    public InputPort(Filter filter, String name) {
        super(filter, name);
    }

    public void setSourcePort(OutputPort source) {
        if (this.mSourcePort == null) {
            this.mSourcePort = source;
            return;
        }
        throw new RuntimeException(this + " already connected to " + this.mSourcePort + "!");
    }

    public boolean isConnected() {
        return this.mSourcePort != null;
    }

    public void open() {
        super.open();
        if (this.mSourcePort != null && !this.mSourcePort.isOpen()) {
            this.mSourcePort.open();
        }
    }

    public void close() {
        if (this.mSourcePort != null && this.mSourcePort.isOpen()) {
            this.mSourcePort.close();
        }
        super.close();
    }

    public OutputPort getSourcePort() {
        return this.mSourcePort;
    }

    public Filter getSourceFilter() {
        if (this.mSourcePort == null) {
            return null;
        }
        return this.mSourcePort.getFilter();
    }

    public FrameFormat getSourceFormat() {
        return this.mSourcePort != null ? this.mSourcePort.getPortFormat() : getPortFormat();
    }

    public Object getTarget() {
        return null;
    }

    public boolean filterMustClose() {
        return !isOpen() && isBlocking() && !hasFrame();
    }

    public boolean isReady() {
        return hasFrame() || !isBlocking();
    }

    public boolean acceptsFrame() {
        return !hasFrame();
    }
}
