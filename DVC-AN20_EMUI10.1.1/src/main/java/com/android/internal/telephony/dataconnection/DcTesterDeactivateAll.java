package com.android.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.Iterator;

public class DcTesterDeactivateAll {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "DcTesterDeacativateAll";
    public static String sActionDcTesterDeactivateAll = "com.android.internal.telephony.dataconnection.action_deactivate_all";
    private DcController mDcc;
    private Phone mPhone;
    protected BroadcastReceiver sIntentReceiver = new BroadcastReceiver() {
        /* class com.android.internal.telephony.dataconnection.DcTesterDeactivateAll.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                String action = intent.getAction();
                DcTesterDeactivateAll.log("sIntentReceiver.onReceive: action=" + action);
                if (action.equals(DcTesterDeactivateAll.sActionDcTesterDeactivateAll) || action.equals(DcTesterDeactivateAll.this.mPhone.getActionDetached())) {
                    DcTesterDeactivateAll.log("Send DEACTIVATE to all Dcc's");
                    if (DcTesterDeactivateAll.this.mDcc != null) {
                        Iterator<DataConnection> it = DcTesterDeactivateAll.this.mDcc.mDcListAll.iterator();
                        while (it.hasNext()) {
                            it.next().tearDownNow();
                        }
                        return;
                    }
                    DcTesterDeactivateAll.log("onReceive: mDcc is null, ignoring");
                    return;
                }
                DcTesterDeactivateAll.log("onReceive: unknown action=" + action);
            }
        }
    };

    DcTesterDeactivateAll(Phone phone, DcController dcc, Handler handler) {
        this.mPhone = phone;
        this.mDcc = dcc;
        if (Build.IS_DEBUGGABLE) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(sActionDcTesterDeactivateAll);
            log("register for intent action=" + sActionDcTesterDeactivateAll);
            filter.addAction(this.mPhone.getActionDetached());
            log("register for intent action=" + this.mPhone.getActionDetached());
            phone.getContext().registerReceiver(this.sIntentReceiver, filter, null, handler);
        }
    }

    /* access modifiers changed from: package-private */
    public void dispose() {
        if (Build.IS_DEBUGGABLE) {
            this.mPhone.getContext().unregisterReceiver(this.sIntentReceiver);
        }
    }

    /* access modifiers changed from: private */
    public static void log(String s) {
        Rlog.i(LOG_TAG, s);
    }
}
