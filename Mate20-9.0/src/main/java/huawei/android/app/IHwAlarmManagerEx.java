package huawei.android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IHwAlarmManagerEx extends IInterface {

    public static abstract class Stub extends Binder implements IHwAlarmManagerEx {
        private static final String DESCRIPTOR = "huawei.android.app.IHwAlarmManagerEx";
        static final int TRANSACTION_removeAllAdjustAlarms = 4;
        static final int TRANSACTION_removeAllPendingAlarms = 2;
        static final int TRANSACTION_setAlarmsAdjust = 3;
        static final int TRANSACTION_setAlarmsPending = 1;

        private static class Proxy implements IHwAlarmManagerEx {
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

            public void setAlarmsPending(List<String> pkgList, List<String> actionList, boolean pending, int allAlarms) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(pkgList);
                    _data.writeStringList(actionList);
                    _data.writeInt(pending);
                    _data.writeInt(allAlarms);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAllPendingAlarms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAlarmsAdjust(List<String> pkgList, List<String> actionList, boolean adjust, int type, long interval, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(pkgList);
                    _data.writeStringList(actionList);
                    _data.writeInt(adjust);
                    _data.writeInt(type);
                    _data.writeLong(interval);
                    _data.writeInt(mode);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAllAdjustAlarms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IHwAlarmManagerEx asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IHwAlarmManagerEx)) {
                return new Proxy(obj);
            }
            return (IHwAlarmManagerEx) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            if (i != 1598968902) {
                boolean _arg2 = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        List<String> _arg0 = data.createStringArrayList();
                        List<String> _arg1 = data.createStringArrayList();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setAlarmsPending(_arg0, _arg1, _arg2, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        removeAllPendingAlarms();
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        setAlarmsAdjust(data.createStringArrayList(), data.createStringArrayList(), data.readInt() != 0, data.readInt(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        removeAllAdjustAlarms();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    void removeAllAdjustAlarms() throws RemoteException;

    void removeAllPendingAlarms() throws RemoteException;

    void setAlarmsAdjust(List<String> list, List<String> list2, boolean z, int i, long j, int i2) throws RemoteException;

    void setAlarmsPending(List<String> list, List<String> list2, boolean z, int i) throws RemoteException;
}
