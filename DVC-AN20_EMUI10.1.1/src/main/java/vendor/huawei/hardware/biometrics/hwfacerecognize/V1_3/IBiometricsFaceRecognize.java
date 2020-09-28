package vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3;

import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognizeClientCallback;
import vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize;
import vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize;

public interface IBiometricsFaceRecognize extends vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize {
    public static final String kInterfaceName = "vendor.huawei.hardware.biometrics.hwfacerecognize@1.3::IBiometricsFaceRecognize";

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    int sendBigData(int i, int i2, NativeHandle nativeHandle, ArrayList<Byte> arrayList) throws RemoteException;

    int sendData(int i, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2) throws RemoteException;

    int setDataCallback(IBiometricsFaceRecognizeDataCallback iBiometricsFaceRecognizeDataCallback) throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static default IBiometricsFaceRecognize asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof IBiometricsFaceRecognize)) {
            return (IBiometricsFaceRecognize) iface;
        }
        IBiometricsFaceRecognize proxy = new Proxy(binder);
        try {
            Iterator<String> it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                if (it.next().equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    static default IBiometricsFaceRecognize castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static default IBiometricsFaceRecognize getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static default IBiometricsFaceRecognize getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    static default IBiometricsFaceRecognize getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    static default IBiometricsFaceRecognize getService() throws RemoteException {
        return getService("default");
    }

    public static final class Proxy implements IBiometricsFaceRecognize {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.huawei.hardware.biometrics.hwfacerecognize@1.3::IBiometricsFaceRecognize]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public long setNotify(IBiometricsFaceRecognizeClientCallback clientCallback) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeStrongBinder(clientCallback == null ? null : clientCallback.asBinder());
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt64();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public long preEnroll() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt64();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int enroll(byte[] hat, int gid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwBlob _hidl_blob = new HwBlob(69);
            if (hat == null || hat.length != 69) {
                throw new IllegalArgumentException("Array element is not of the expected length");
            }
            _hidl_blob.putInt8Array(0, hat);
            _hidl_request.writeBuffer(_hidl_blob);
            _hidl_request.writeInt32(gid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int postEnroll() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int cancelEnroll() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int cancelAuthenticate() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int cancelRemove() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int remove(int gid, int fid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(gid);
            _hidl_request.writeInt32(fid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public ArrayList<Integer> setActiveGroup(int gid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(gid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32Vector();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int authenticate(long operationId, int gid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt64(operationId);
            _hidl_request.writeInt32(gid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int sendImageData(String data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeString(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int init(String packageName) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeString(packageName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int cancelInit() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int release(String packageName) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeString(packageName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize
        public int cancelRelease() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize
        public int setSecureFaceMode(int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(16, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize
        public int getAngleDim() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(17, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize
        public int setFlag(int flag) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(flag);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(18, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize
        public int prepare(ArrayList<Byte> aaid, ArrayList<Byte> nonce, ArrayList<Byte> extra) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt8Vector(aaid);
            _hidl_request.writeInt8Vector(nonce);
            _hidl_request.writeInt8Vector(extra);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(19, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize
        public void getUvt(IBiometricsFaceRecognize.getUvtCallback _hidl_cb) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(20, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                _hidl_cb.onValues(_hidl_reply.readInt32(), _hidl_reply.readInt32(), _hidl_reply.readInt8Vector(), _hidl_reply.readInt8Vector());
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize
        public int sendCommand(int cmd, ArrayList<Byte> param) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(cmd);
            _hidl_request.writeInt8Vector(param);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(21, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize
        public void getStatus(int statusId, IBiometricsFaceRecognize.getStatusCallback _hidl_cb) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(statusId);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(22, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                _hidl_cb.onValues(_hidl_reply.readInt32(), _hidl_reply.readInt32(), _hidl_reply.readInt32(), _hidl_reply.readInt8Vector());
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize
        public int sendBigData(int dataType, int dataSize, NativeHandle memHdl, ArrayList<Byte> extra) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(dataType);
            _hidl_request.writeInt32(dataSize);
            _hidl_request.writeNativeHandle(memHdl);
            _hidl_request.writeInt8Vector(extra);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(23, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize
        public int sendData(int dataType, ArrayList<Byte> data, ArrayList<Byte> extra) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeInt32(dataType);
            _hidl_request.writeInt8Vector(data);
            _hidl_request.writeInt8Vector(extra);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(24, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize
        public int setDataCallback(IBiometricsFaceRecognizeDataCallback callback) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBiometricsFaceRecognize.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(25, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readInt32();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readStringVector();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> options) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public String interfaceDescriptor() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readString();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<byte[]> _hidl_out_hashchain = new ArrayList<>();
                HwBlob _hidl_blob = _hidl_reply.readBuffer(16);
                int _hidl_vec_size = _hidl_blob.getInt32(8);
                HwBlob childBlob = _hidl_reply.readEmbeddedBuffer((long) (_hidl_vec_size * 32), _hidl_blob.handle(), 0, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    childBlob.copyToInt8Array((long) (_hidl_index_0 * 32), _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                return _hidl_out_hashchain;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public void ping() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                DebugInfo _hidl_out_info = new DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends HwBinder implements IBiometricsFaceRecognize {
        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IBiometricsFaceRecognize.kInterfaceName, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize.kInterfaceName, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> arrayList) {
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return IBiometricsFaceRecognize.kInterfaceName;
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-73, 62, 31, -108, 79, 91, -21, 8, -109, -86, -25, -64, 44, -68, -90, -46, 36, 71, 66, 39, 85, 28, 45, -23, -56, -64, -80, 119, -82, 96, -44, -48}, new byte[]{41, 104, 34, 58, -74, 4, -16, 34, -99, -121, 17, -65, -68, -34, 84, -61, 101, 57, -12, -115, -85, 70, 92, 74, -16, 11, -85, -29, 7, -93, 36, 90}, new byte[]{9, -38, -59, -59, 57, -22, -67, 36, -112, 115, -98, 85, -88, 33, -36, 59, 102, 112, -61, 50, -14, 85, -8, -59, -57, -99, 115, -62, 110, -22, -63, 1}, new byte[]{-110, 116, -69, 10, -65, 40, 124, -41, -69, -96, 88, -10, -77, -18, -63, 6, -116, -8, -41, 36, 118, 13, -14, 12, -6, -62, -43, 29, -118, -115, 97, 41}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0;
            info.arch = 0;
            return info;
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize, vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public IHwInterface queryLocalInterface(String descriptor) {
            if (IBiometricsFaceRecognize.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String serviceName) throws RemoteException {
            registerService(serviceName);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int _hidl_code, HwParcel _hidl_request, final HwParcel _hidl_reply, int _hidl_flags) throws RemoteException {
            boolean _hidl_is_oneway = false;
            boolean _hidl_is_oneway2 = true;
            switch (_hidl_code) {
                case 1:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    long _hidl_out_deviceId = setNotify(IBiometricsFaceRecognizeClientCallback.asInterface(_hidl_request.readStrongBinder()));
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt64(_hidl_out_deviceId);
                    _hidl_reply.send();
                    return;
                case 2:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    long _hidl_out_authChallenge = preEnroll();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt64(_hidl_out_authChallenge);
                    _hidl_reply.send();
                    return;
                case 3:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    byte[] hat = new byte[69];
                    _hidl_request.readBuffer(69).copyToInt8Array(0, hat, 69);
                    int _hidl_out_debugErrno = enroll(hat, _hidl_request.readInt32());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno);
                    _hidl_reply.send();
                    return;
                case 4:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno2 = postEnroll();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno2);
                    _hidl_reply.send();
                    return;
                case 5:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno3 = cancelEnroll();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno3);
                    _hidl_reply.send();
                    return;
                case 6:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno4 = cancelAuthenticate();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno4);
                    _hidl_reply.send();
                    return;
                case 7:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno5 = cancelRemove();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno5);
                    _hidl_reply.send();
                    return;
                case 8:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno6 = remove(_hidl_request.readInt32(), _hidl_request.readInt32());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno6);
                    _hidl_reply.send();
                    return;
                case 9:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    ArrayList<Integer> _hidl_out_faceId = setActiveGroup(_hidl_request.readInt32());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32Vector(_hidl_out_faceId);
                    _hidl_reply.send();
                    return;
                case 10:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno7 = authenticate(_hidl_request.readInt64(), _hidl_request.readInt32());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno7);
                    _hidl_reply.send();
                    return;
                case 11:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_debugErrno8 = sendImageData(_hidl_request.readString());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno8);
                    _hidl_reply.send();
                    return;
                case 12:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result = init(_hidl_request.readString());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result);
                    _hidl_reply.send();
                    return;
                case 13:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result2 = cancelInit();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result2);
                    _hidl_reply.send();
                    return;
                case 14:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result3 = release(_hidl_request.readString());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result3);
                    _hidl_reply.send();
                    return;
                case 15:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_0.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result4 = cancelRelease();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result4);
                    _hidl_reply.send();
                    return;
                case 16:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result5 = setSecureFaceMode(_hidl_request.readInt32());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result5);
                    _hidl_reply.send();
                    return;
                case 17:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result6 = getAngleDim();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result6);
                    _hidl_reply.send();
                    return;
                case 18:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result7 = setFlag(_hidl_request.readInt32());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result7);
                    _hidl_reply.send();
                    return;
                case 19:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result8 = prepare(_hidl_request.readInt8Vector(), _hidl_request.readInt8Vector(), _hidl_request.readInt8Vector());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result8);
                    _hidl_reply.send();
                    return;
                case 20:
                    if ((_hidl_flags & 1) != 0) {
                        _hidl_is_oneway = true;
                    }
                    if (_hidl_is_oneway) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
                    getUvt(new IBiometricsFaceRecognize.getUvtCallback() {
                        /* class vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize.Stub.AnonymousClass1 */

                        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.getUvtCallback
                        public void onValues(int result, int faceid, ArrayList<Byte> uvt, ArrayList<Byte> reserve) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(faceid);
                            _hidl_reply.writeInt8Vector(uvt);
                            _hidl_reply.writeInt8Vector(reserve);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 21:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_1.IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result9 = sendCommand(_hidl_request.readInt32(), _hidl_request.readInt8Vector());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result9);
                    _hidl_reply.send();
                    return;
                case 22:
                    if ((_hidl_flags & 1) != 0) {
                        _hidl_is_oneway = true;
                    }
                    if (_hidl_is_oneway) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize.kInterfaceName);
                    getStatus(_hidl_request.readInt32(), new IBiometricsFaceRecognize.getStatusCallback() {
                        /* class vendor.huawei.hardware.biometrics.hwfacerecognize.V1_3.IBiometricsFaceRecognize.Stub.AnonymousClass2 */

                        @Override // vendor.huawei.hardware.biometrics.hwfacerecognize.V1_2.IBiometricsFaceRecognize.getStatusCallback
                        public void onValues(int result, int status1, int status2, ArrayList<Byte> extra) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(status1);
                            _hidl_reply.writeInt32(status2);
                            _hidl_reply.writeInt8Vector(extra);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 23:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result10 = sendBigData(_hidl_request.readInt32(), _hidl_request.readInt32(), _hidl_request.readNativeHandle(), _hidl_request.readInt8Vector());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result10);
                    _hidl_reply.send();
                    return;
                case 24:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result11 = sendData(_hidl_request.readInt32(), _hidl_request.readInt8Vector(), _hidl_request.readInt8Vector());
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result11);
                    _hidl_reply.send();
                    return;
                case 25:
                    if ((_hidl_flags & 1) == 0) {
                        _hidl_is_oneway2 = false;
                    }
                    if (_hidl_is_oneway2) {
                        _hidl_reply.writeStatus(Integer.MIN_VALUE);
                        _hidl_reply.send();
                        return;
                    }
                    _hidl_request.enforceInterface(IBiometricsFaceRecognize.kInterfaceName);
                    int _hidl_out_result12 = setDataCallback(IBiometricsFaceRecognizeDataCallback.asInterface(_hidl_request.readStrongBinder()));
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result12);
                    _hidl_reply.send();
                    return;
                default:
                    switch (_hidl_code) {
                        case 256067662:
                            if ((_hidl_flags & 1) == 0) {
                                _hidl_is_oneway2 = false;
                            }
                            if (_hidl_is_oneway2) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            ArrayList<String> _hidl_out_descriptors = interfaceChain();
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeStringVector(_hidl_out_descriptors);
                            _hidl_reply.send();
                            return;
                        case 256131655:
                            if ((_hidl_flags & 1) == 0) {
                                _hidl_is_oneway2 = false;
                            }
                            if (_hidl_is_oneway2) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            debug(_hidl_request.readNativeHandle(), _hidl_request.readStringVector());
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.send();
                            return;
                        case 256136003:
                            if ((_hidl_flags & 1) == 0) {
                                _hidl_is_oneway2 = false;
                            }
                            if (_hidl_is_oneway2) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            String _hidl_out_descriptor = interfaceDescriptor();
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeString(_hidl_out_descriptor);
                            _hidl_reply.send();
                            return;
                        case 256398152:
                            if ((_hidl_flags & 1) == 0) {
                                _hidl_is_oneway2 = false;
                            }
                            if (_hidl_is_oneway2) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                            _hidl_reply.writeStatus(0);
                            HwBlob _hidl_blob = new HwBlob(16);
                            int _hidl_vec_size = _hidl_out_hashchain.size();
                            _hidl_blob.putInt32(8, _hidl_vec_size);
                            _hidl_blob.putBool(12, false);
                            HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
                            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                                long _hidl_array_offset_1 = (long) (_hidl_index_0 * 32);
                                byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                                if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                                    throw new IllegalArgumentException("Array element is not of the expected length");
                                }
                                childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                            }
                            _hidl_blob.putBlob(0, childBlob);
                            _hidl_reply.writeBuffer(_hidl_blob);
                            _hidl_reply.send();
                            return;
                        case 256462420:
                            if ((_hidl_flags & 1) != 0) {
                                _hidl_is_oneway = true;
                            }
                            if (!_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            setHALInstrumentation();
                            return;
                        case 256660548:
                            if ((_hidl_flags & 1) != 0) {
                                _hidl_is_oneway = true;
                            }
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            return;
                        case 256921159:
                            if ((_hidl_flags & 1) == 0) {
                                _hidl_is_oneway2 = false;
                            }
                            if (_hidl_is_oneway2) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            ping();
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.send();
                            return;
                        case 257049926:
                            if ((_hidl_flags & 1) == 0) {
                                _hidl_is_oneway2 = false;
                            }
                            if (_hidl_is_oneway2) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            DebugInfo _hidl_out_info = getDebugInfo();
                            _hidl_reply.writeStatus(0);
                            _hidl_out_info.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                            return;
                        case 257120595:
                            if ((_hidl_flags & 1) != 0) {
                                _hidl_is_oneway = true;
                            }
                            if (!_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            _hidl_request.enforceInterface(IBase.kInterfaceName);
                            notifySyspropsChanged();
                            return;
                        case 257250372:
                            if ((_hidl_flags & 1) != 0) {
                                _hidl_is_oneway = true;
                            }
                            if (_hidl_is_oneway) {
                                _hidl_reply.writeStatus(Integer.MIN_VALUE);
                                _hidl_reply.send();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
            }
        }
    }
}
