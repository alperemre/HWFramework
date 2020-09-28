package com.huawei.android.hardware.hisifmradio;

import android.os.RemoteException;
import com.huawei.android.hardware.fmradio.IFmEventCallback;
import com.huawei.android.hardware.fmradio.IHwFmService;
import com.huawei.android.os.ServiceManagerEx;
import com.huawei.android.util.SlogEx;

/* access modifiers changed from: package-private */
public class FmReceiverWrapper {
    private static final int CALL_FM_SERVICE_ERR = -1;
    private static final int FM_JNI_FAILURE = -1;
    private static final int FM_JNI_SUCCESS = 0;
    private static final String TAG = "FmReceiverWrapper";
    private static IHwFmService sService;

    private FmReceiverWrapper() {
    }

    private static IHwFmService getService() {
        IHwFmService iHwFmService = sService;
        if (iHwFmService != null) {
            return iHwFmService;
        }
        sService = IHwFmService.Stub.asInterface(ServiceManagerEx.getService("hwfm_service"));
        return sService;
    }

    static int acquireFdNative(String path) {
        try {
            return getService().acquireFd(path);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int audioControlNative(int fd, int control, int field) {
        try {
            return getService().audioControl(fd, control, field);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int cancelSearchNative(int fd) {
        try {
            return getService().cancelSearch(fd);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int closeFdNative(int fd) {
        try {
            return getService().closeFd(fd);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getFreqNative(int fd) {
        try {
            return getService().getFreq(fd);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int setFreqNative(int fd, int freq) {
        try {
            return getService().setFreq(fd, freq);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getControlNative(int fd, int id) {
        try {
            return getService().getControl(fd, id);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int setControlNative(int fd, int id, int value) {
        try {
            return getService().setControl(fd, id, value);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int startSearchNative(int fd, int dir) {
        try {
            return getService().startSearch(fd, dir);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getBufferNative(int fd, byte[] buff, int index) {
        try {
            return getService().getBuffer(fd, buff, index);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getRSSINative(int fd) {
        try {
            return getService().getRSSI(fd);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int setBandNative(int fd, int low, int high) {
        try {
            return getService().setBand(fd, low, high);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getLowerBandNative(int fd) {
        try {
            return getService().getLowerBand(fd);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getUpperBandNative(int fd) {
        try {
            return getService().getUpperBand(fd);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int setMonoStereoNative(int fd, int val) {
        try {
            return getService().setMonoStereo(fd, val);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int getRawRdsNative(int fd, byte[] buff, int count) {
        try {
            return getService().getRawRds(fd, buff, count);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static void setNotchFilterNative(boolean value) {
        try {
            getService().setNotchFilter(value);
        } catch (RemoteException e) {
            SlogEx.e(TAG, "setNotchFilterNative caught exception");
        }
    }

    static int getAudioQuiltyNative(int fd, int value) {
        try {
            return getService().getAudioQuilty(fd, value);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int setFmSnrThreshNative(int fd, int value) {
        try {
            return getService().setFmSnrThresh(fd, value);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static int setFmRssiThreshNative(int fd, int value) {
        try {
            return getService().setFmRssiThresh(fd, value);
        } catch (RemoteException e) {
            return -1;
        }
    }

    static void setFmDeviceConnectionState(int state) {
        try {
            getService().setFmDeviceConnectionState(state);
        } catch (RemoteException e) {
            SlogEx.e(TAG, "setFmDeviceConnectionState caught exception");
        }
    }

    static void startListner(int fd, IFmEventCallback cb) {
        try {
            getService().startListner(fd, cb);
        } catch (RemoteException e) {
            SlogEx.e(TAG, "startListner caught exception");
        }
    }

    static void stopListner() {
        try {
            getService().stopListner();
        } catch (RemoteException e) {
            SlogEx.e(TAG, "stopListner caught exception");
        }
    }
}
