package huawei.android.view.inputmethod;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.IHwSecImmHelper;
import android.widget.TextView;
import com.android.internal.view.IInputMethodClient;
import com.android.internal.view.IInputMethodManager;

public class HwSecImmHelper implements IHwSecImmHelper {
    private static final boolean DEBUG = false;
    private static final String DESCRIPTOR = "android.view.inputmethod.InputMethodManager";
    public static final String SECURE_IME_ID = "com.huawei.secime/.SoftKeyboard";
    public static final String SECURE_IME_NAME = "com.huawei.secime.SoftKeyboard";
    public static final int SECURE_IME_NO_HIDE_FLAG = 4096;
    public static final String SECURE_IME_PACKAGENAME = "com.huawei.secime";
    private static final String TAG = "HwSecImmHelper";
    private static final int TRANSACTION_isUseSecureIME = 1001;
    private IInputMethodManager mSecurityInputMethodService = getSecurityInputMethodService();
    final IInputMethodManager mService;

    public HwSecImmHelper(IInputMethodManager service) {
        this.mService = service;
    }

    private IInputMethodManager getSecurityInputMethodService() {
        if (this.mSecurityInputMethodService == null) {
            this.mSecurityInputMethodService = IInputMethodManager.Stub.asInterface(ServiceManager.getService("input_method_secure"));
        }
        if (this.mSecurityInputMethodService == null) {
            Log.i(TAG, "mSecurityInputMethodService is null");
        }
        return this.mSecurityInputMethodService;
    }

    public boolean showSoftInput(View view, int flags, ResultReceiver resultReceiver, IInputMethodClient.Stub mClient) {
        try {
            return this.mService.showSoftInput(mClient, flags, resultReceiver);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags, ResultReceiver resultReceiver, View mServedView, IInputMethodClient.Stub mClient) {
        if (isUseSecureIME()) {
            if (isPasswordInputType(mServedView)) {
                try {
                    this.mService.hideSoftInput(mClient, 4096, resultReceiver);
                    return this.mSecurityInputMethodService.hideSoftInput(mClient, flags, resultReceiver);
                } catch (RemoteException e) {
                }
            } else {
                try {
                    this.mSecurityInputMethodService.hideSoftInput(mClient, flags, resultReceiver);
                } catch (RemoteException e2) {
                }
            }
        }
        try {
            return this.mService.hideSoftInput(mClient, flags, resultReceiver);
        } catch (RemoteException e3) {
            return false;
        }
    }

    public boolean isUseSecureIME() {
        boolean isEnabled = false;
        try {
            if (getSecurityInputMethodService() != null) {
                isEnabled = isUseSecureIMEInner();
            }
            return isEnabled;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isUseSecureIMEInner() {
        IBinder b = this.mService.asBinder();
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        boolean result = false;
        boolean result2 = false;
        if (b != null) {
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                b.transact(1001, data, reply, 0);
                reply.readException();
                if (reply.readInt() != 0) {
                    result = true;
                }
                result2 = result;
            } catch (RemoteException localRemoteException) {
                localRemoteException.printStackTrace();
            } catch (Throwable th) {
                reply.recycle();
                data.recycle();
                throw th;
            }
        }
        reply.recycle();
        data.recycle();
        return result2;
    }

    public boolean isPasswordInputType(View view) {
        boolean isPwType;
        if (view == null) {
            return false;
        }
        if (view instanceof TextView) {
            isPwType = isPasswordInputType(((TextView) view).getInputType(), false);
        } else {
            EditorInfo tba = new EditorInfo();
            tba.packageName = view.getContext().getPackageName();
            tba.fieldId = view.getId();
            try {
                view.onCreateInputConnection(tba);
            } catch (RuntimeException e) {
                Log.e(TAG, "Avoiding individual browser to crashes because webview null pointer");
            }
            isPwType = isPasswordInputType(tba.inputType, false);
        }
        return isPwType;
    }

    public static boolean isPasswordInputType(int inputType, boolean debug) {
        int variation = inputType & 4095;
        boolean isPassword = variation == 129 || variation == 145 || variation == 225 || variation == 18 || variation == 128 || variation == 144 || variation == 224;
        if (debug && inputType != 0) {
            Log.d(TAG, "isPasswordInputType = " + isPassword + ", inputType = " + inputType);
        }
        return isPassword;
    }
}
