package android.common;

import android.content.Context;
import org.json.JSONObject;

public class HwFlogManagerDummy implements HwFlogManager {
    private static HwFlogManager mHwFlogManager = null;

    private HwFlogManagerDummy() {
    }

    public static HwFlogManager getDefault() {
        if (mHwFlogManager == null) {
            mHwFlogManager = new HwFlogManagerDummy();
        }
        return mHwFlogManager;
    }

    @Override // android.common.HwFlogManager
    public int slog(int priority, int tag, String msg) {
        return 0;
    }

    @Override // android.common.HwFlogManager
    public int slog(int priority, int tag, String msg, Throwable tr) {
        return 0;
    }

    @Override // android.common.HwFlogManager
    public int slogv(String tag, String msg) {
        return 0;
    }

    @Override // android.common.HwFlogManager
    public int slogd(String tag, String msg) {
        return 0;
    }

    @Override // android.common.HwFlogManager
    public boolean handleLogRequest(String[] args) {
        return false;
    }

    @Override // android.common.HwFlogManager
    public boolean bdReport(Context context, int eventID) {
        return false;
    }

    @Override // android.common.HwFlogManager
    public boolean bdReport(Context context, int eventID, String eventMsg) {
        return false;
    }

    @Override // android.common.HwFlogManager
    public boolean bdReport(Context context, int eventID, JSONObject eventMsg) {
        return false;
    }

    @Override // android.common.HwFlogManager
    public boolean bdReport(Context context, int eventID, JSONObject eventMsg, int priority) {
        return false;
    }
}
