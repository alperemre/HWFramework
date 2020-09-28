package com.huawei.android.hwforcedark;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.WindowManager;

public interface IHwForceDarkManager {
    boolean setAllowedHwForceDark(Context context, Canvas canvas, int i, boolean z, WindowManager.LayoutParams layoutParams);

    int updateHwForceDarkState(Context context, View view, WindowManager.LayoutParams layoutParams);

    int updateHwForceDarkSystemUIVisibility(int i);
}
