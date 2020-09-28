package android.app.mtm.iaware.appmng;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAppCleanCallback extends IInterface {
    void onCleanFinish(AppCleanParam appCleanParam) throws RemoteException;

    public static class Default implements IAppCleanCallback {
        @Override // android.app.mtm.iaware.appmng.IAppCleanCallback
        public void onCleanFinish(AppCleanParam result) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppCleanCallback {
        private static final String DESCRIPTOR = "android.app.mtm.iaware.appmng.IAppCleanCallback";
        static final int TRANSACTION_onCleanFinish = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAppCleanCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppCleanCallback)) {
                return new Proxy(obj);
            }
            return (IAppCleanCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            AppCleanParam _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = AppCleanParam.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onCleanFinish(_arg0);
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static class Proxy implements IAppCleanCallback {
            public static IAppCleanCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.mtm.iaware.appmng.IAppCleanCallback
            public void onCleanFinish(AppCleanParam result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCleanFinish(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAppCleanCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppCleanCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
