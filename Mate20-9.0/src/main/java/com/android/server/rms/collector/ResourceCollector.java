package com.android.server.rms.collector;

public final class ResourceCollector {
    private static final String TAG = "RMS.ResourceCollector";

    public static final native String getBuddyInfo();

    public static native int getMemInfo(long[] jArr);

    public static native long getPssFast(int i, long[] jArr, long[] jArr2);

    public static final native int killProcessGroupForQuickKill(int i, int i2);

    private ResourceCollector() {
    }
}
