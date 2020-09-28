package android.pc;

import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.pc.IHwPCManager;
import com.huawei.android.os.ServiceManagerEx;

public class HwPCManagerImpl extends DefaultHwPCManager {
    private static volatile HwPCManagerImpl mInstance = null;
    private IHwPCManager mService = null;

    private HwPCManagerImpl() {
        IBinder iBinder = ServiceManagerEx.getService("hwPcManager");
        if (iBinder != null) {
            this.mService = IHwPCManager.Stub.asInterface(iBinder);
        }
    }

    public static synchronized HwPCManagerImpl getDefault() {
        HwPCManagerImpl hwPCManagerImpl;
        synchronized (HwPCManagerImpl.class) {
            if (mInstance == null || mInstance.getService() == null) {
                mInstance = new HwPCManagerImpl();
            }
            hwPCManagerImpl = mInstance;
        }
        return hwPCManagerImpl;
    }

    public IHwPCManager getService() {
        return this.mService;
    }

    public void execVoiceCmd(Message message) throws RemoteException {
        IHwPCManager iHwPCManager = this.mService;
        if (iHwPCManager != null) {
            iHwPCManager.execVoiceCmd(message);
        }
    }
}
