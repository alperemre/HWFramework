package android.app.mtm.iaware;

import android.os.Parcel;
import android.os.Parcelable;
import android.rms.iaware.AwareLog;
import android.text.TextUtils;
import java.util.Arrays;

public class HwAppStartupSetting implements Parcelable, Cloneable {
    public static final int AS_MAX_NUM = 4;
    public static final int AS_MODIFIER_CUST = 2;
    public static final int AS_MODIFIER_DEF = 0;
    public static final int AS_MODIFIER_USER = 1;
    public static final int AS_OFF = 0;
    public static final int AS_ON = 1;
    public static final int AS_TP_ALV = 3;
    public static final int AS_TP_ASS = 2;
    public static final int AS_TP_MDF = 0;
    public static final int AS_TP_SHW = 0;
    public static final int AS_TP_SLF = 1;
    public static final int AS_TP_SMT = 0;
    public static final int AS_UNSET = -1;
    public static final Parcelable.Creator<HwAppStartupSetting> CREATOR = new Parcelable.Creator<HwAppStartupSetting>() {
        /* class android.app.mtm.iaware.HwAppStartupSetting.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public HwAppStartupSetting createFromParcel(Parcel source) {
            return new HwAppStartupSetting(source);
        }

        @Override // android.os.Parcelable.Creator
        public HwAppStartupSetting[] newArray(int size) {
            return new HwAppStartupSetting[size];
        }
    };
    private static final String TAG = "HwAppStartupSetting";
    private int[] mModifier;
    private String mPackageName;
    private int[] mPolicy;
    private int[] mShow;

    public HwAppStartupSetting(String packageName, int[] policy, int[] modifier, int[] show) {
        this.mPolicy = new int[]{-1, -1, -1, -1};
        this.mShow = new int[]{-1, -1, -1, -1};
        this.mModifier = new int[]{-1, -1, -1, -1};
        this.mPackageName = packageName;
        if (policy != null && 4 == policy.length) {
            System.arraycopy(policy, 0, this.mPolicy, 0, 4);
        }
        if (show != null && 4 == show.length) {
            System.arraycopy(show, 0, this.mShow, 0, 4);
        }
        if (modifier != null && 4 == modifier.length) {
            System.arraycopy(modifier, 0, this.mModifier, 0, 4);
        }
    }

    public HwAppStartupSetting(HwAppStartupSetting setting) {
        this.mPolicy = new int[]{-1, -1, -1, -1};
        this.mShow = new int[]{-1, -1, -1, -1};
        this.mModifier = new int[]{-1, -1, -1, -1};
        if (setting != null) {
            this.mPackageName = setting.mPackageName;
            int[] iArr = setting.mPolicy;
            if (4 == iArr.length) {
                System.arraycopy(iArr, 0, this.mPolicy, 0, 4);
            }
            int[] iArr2 = setting.mShow;
            if (4 == iArr2.length) {
                System.arraycopy(iArr2, 0, this.mShow, 0, 4);
            }
            int[] iArr3 = setting.mModifier;
            if (4 == iArr3.length) {
                System.arraycopy(iArr3, 0, this.mModifier, 0, 4);
            }
        }
    }

    public HwAppStartupSetting copyValidInfo(HwAppStartupSetting setting) {
        if (setting != null && setting.isValid(true)) {
            this.mPackageName = setting.mPackageName;
            for (int i = 0; i < 4; i++) {
                if (-1 != setting.getPolicy(i)) {
                    this.mPolicy[i] = setting.getPolicy(i);
                }
                if (-1 != setting.getShow(i)) {
                    this.mShow[i] = setting.getShow(i);
                }
                if (-1 != setting.getModifier(i)) {
                    this.mModifier[i] = setting.getModifier(i);
                }
            }
        }
        return this;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getPolicy(int type) {
        if (type < 0) {
            return -1;
        }
        int[] iArr = this.mPolicy;
        if (type >= iArr.length) {
            return -1;
        }
        return iArr[type];
    }

    public int getModifier(int type) {
        if (type < 0) {
            return -1;
        }
        int[] iArr = this.mModifier;
        if (type >= iArr.length) {
            return -1;
        }
        return iArr[type];
    }

    public int getShow(int type) {
        if (type < 0) {
            return -1;
        }
        int[] iArr = this.mShow;
        if (type >= iArr.length) {
            return -1;
        }
        return iArr[type];
    }

    public boolean valid() {
        return isValid(false);
    }

    private boolean isValid(boolean checkUnset) {
        int[] iArr;
        int[] iArr2;
        if (TextUtils.isEmpty(this.mPackageName) || (iArr = this.mPolicy) == null || 4 != iArr.length || (iArr2 = this.mShow) == null || 4 != iArr2.length) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            int[] iArr3 = this.mPolicy;
            if (!(iArr3[i] == 0 || iArr3[i] == 1 || (checkUnset && iArr3[i] == -1))) {
                return false;
            }
        }
        for (int i2 = 0; i2 < 4; i2++) {
            int[] iArr4 = this.mShow;
            if (!(iArr4[i2] == 0 || iArr4[i2] == 1 || (checkUnset && iArr4[i2] == -1))) {
                return false;
            }
        }
        for (int i3 = 0; i3 < 4; i3++) {
            int[] iArr5 = this.mModifier;
            if (!(iArr5[i3] == 0 || 1 == iArr5[i3] || 2 == iArr5[i3] || (checkUnset && iArr5[i3] == -1))) {
                return false;
            }
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeIntArray(this.mPolicy);
        dest.writeIntArray(this.mShow);
        dest.writeIntArray(this.mModifier);
    }

    private HwAppStartupSetting(Parcel source) {
        this.mPolicy = new int[]{-1, -1, -1, -1};
        this.mShow = new int[]{-1, -1, -1, -1};
        this.mModifier = new int[]{-1, -1, -1, -1};
        this.mPackageName = source.readString();
        source.readIntArray(this.mPolicy);
        source.readIntArray(this.mShow);
        source.readIntArray(this.mModifier);
    }

    public String toString() {
        return '{' + this.mPackageName + " policy:" + Arrays.toString(this.mPolicy) + " modifier:" + Arrays.toString(this.mModifier) + " show:" + Arrays.toString(this.mShow) + '}';
    }

    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        try {
            return (HwAppStartupSetting) super.clone();
        } catch (CloneNotSupportedException e) {
            AwareLog.e(TAG, "clone catch exception!");
            return null;
        }
    }
}
