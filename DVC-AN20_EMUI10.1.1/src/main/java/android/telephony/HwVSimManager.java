package android.telephony;

import android.os.Bundle;
import android.os.RemoteException;
import com.android.internal.telephony.HuaweiTelephonyConfigs;
import com.android.internal.telephony.HwTelephonyProperties;
import com.android.internal.telephony.IHwVSim;
import com.huawei.android.os.ServiceManagerEx;
import com.huawei.android.os.SystemPropertiesEx;
import com.huawei.android.telephony.RlogEx;
import com.huawei.android.telephony.SubscriptionManagerEx;
import com.huawei.android.telephony.TelephonyManagerEx;

public class HwVSimManager {
    private static final boolean IS_DUAL_CMCC_UNICOM_DEVICE = SystemPropertiesEx.getBoolean("ro.hwpp.dualcu", false);
    public static final int MAX_VSIM_MODEM_COUNT_DUAL_SIM = 2;
    public static final int MAX_VSIM_MODEM_COUNT_TRI_SIM = 3;
    public static final int NETWORK_TYPE_1xRTT = 7;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_EHRPD = 14;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_GSM = 16;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_HSPAP = 15;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_TDS = 17;
    public static final int NETWORK_TYPE_TDS_HSDPA = 18;
    public static final int NETWORK_TYPE_TDS_HSUPA = 19;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    private static final String PROPERTY_VSIM_DSDS_VERSION = "ro.radio.vsim_dsds_version";
    private static final String PROP_OVERSEAS_MODE = "persist.radio.overseas_mode";
    private static final String TAG = "HwVSimManager";
    private static final int VSIM_CAPABILITY_DUAL_CARDS = 2;
    private static final int VSIM_CMCC_DEVICE = 2;
    private static final boolean VSIM_DBG = false;
    private static final int VSIM_DSDS_VERSION_DEFAULT = 0;
    private static final int VSIM_DSDS_VERSION_ONE = 1;
    private static final int VSIM_DSDS_VERSION_PROP = SystemPropertiesEx.getInt(PROPERTY_VSIM_DSDS_VERSION, 0);
    private static final int VSIM_DUAL_CMCC_UNICOM_DEVICE = 4;
    private static final int VSIM_ENABLE_RESULT_FAIL = 3;
    private static final int VSIM_FULLNETWORK_DEVICE = 5;
    private static final int VSIM_MODEM_COUNT = SystemPropertiesEx.getInt("ro.radio.vsim_modem_count", 3);
    private static final int VSIM_MODEM_COUNT_DEFAULT = 3;
    private static final int VSIM_MODEM_COUNT_DUAL_NEW = 5;
    public static final int VSIM_OP_ENABLEVSIM = 1;
    public static final int VSIM_OP_ENABLEVSIM_FORHASH = 3;
    public static final int VSIM_OP_SETAPN = 2;
    public static final int VSIM_OP_SETAPN_FORHASH = 4;
    private static final int VSIM_TELECOM_DEVICE = 1;
    private static final int VSIM_UNICOM_DEVICE = 3;
    private static final int VSIM_UNKNOWN_DEVICE = -1;
    private static final boolean VSIM_VDBG = false;
    public static final int VSIM_WORKMODE_HIGH_SPEED = 2;
    public static final int VSIM_WORKMODE_RESERVE_SUB1 = 0;
    public static final int VSIM_WORKMODE_RESERVE_SUB2 = 1;
    private static HwVSimManager sInstance = new HwVSimManager();
    private static final boolean sIsPlatformSupportVSim = SystemPropertiesEx.getBoolean("ro.radio.vsim_support", false);

    private HwVSimManager() {
    }

    public static HwVSimManager getDefault() {
        return sInstance;
    }

    private IHwVSim getIHwVSim() throws RemoteException {
        IHwVSim iHwVSim = IHwVSim.Stub.asInterface(ServiceManagerEx.getService("ihwvsim"));
        if (iHwVSim != null) {
            return iHwVSim;
        }
        throw new RemoteException("getIHwVSim null");
    }

    public boolean isPlatformSupportVsim() {
        return sIsPlatformSupportVSim;
    }

    public int maxVSimModemCount() {
        return 3;
    }

    public boolean hasIccCardForVSim(int slotId) {
        if (maxVSimModemCount() == 3 && slotId == 2) {
            return hasVSimIccCard();
        }
        return (slotId == 0 || slotId == 1) ? false : false;
    }

    public boolean hasVSimIccCard() {
        try {
            return getIHwVSim().hasVSimIccCard();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int getSimStateForVSim(int slotIdx) {
        if (maxVSimModemCount() == 3 && slotIdx == 2) {
            return getVSimState();
        }
        if (SubscriptionManagerEx.getSubId(slotIdx) == null) {
            return 1;
        }
        String prop = TelephonyManagerEx.getTelephonyProperty(slotIdx, "gsm.sim.state", "");
        if ("ABSENT".equals(prop)) {
            return 1;
        }
        if ("PIN_REQUIRED".equals(prop)) {
            return 2;
        }
        if ("PUK_REQUIRED".equals(prop)) {
            return 3;
        }
        if ("NETWORK_LOCKED".equals(prop)) {
            return 4;
        }
        if ("READY".equals(prop)) {
            return 5;
        }
        if ("CARD_IO_ERROR".equals(prop)) {
            return 8;
        }
        return 0;
    }

    public int getVSimState() {
        String prop = SystemPropertiesEx.get("gsm.vsim.state");
        if ("ABSENT".equals(prop)) {
            return 1;
        }
        if ("PIN_REQUIRED".equals(prop)) {
            return 2;
        }
        if ("PUK_REQUIRED".equals(prop)) {
            return 3;
        }
        if ("NETWORK_LOCKED".equals(prop)) {
            return 4;
        }
        if ("READY".equals(prop)) {
            return 5;
        }
        if ("CARD_IO_ERROR".equals(prop)) {
            return 8;
        }
        return 0;
    }

    public int getVSimSubId() {
        try {
            return getIHwVSim().getVSimSubId();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public boolean isVSimEnabled() {
        try {
            return getIHwVSim().isVSimEnabled();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public boolean isVSimInProcess() {
        try {
            return getIHwVSim().isVSimInProcess();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public boolean isVSimOn() {
        try {
            return getIHwVSim().isVSimOn();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int enableVSim(int operation, String imsi, int cardtype, int apntype, String acqorder, String challenge) {
        RlogEx.i(TAG, "enableVSim, operation = " + operation + ", cardtype = " + cardtype + ", apntype = " + apntype + ", acqorder: " + acqorder);
        int result = 3;
        try {
            result = getIHwVSim().enableVSim(operation, imsi, cardtype, apntype, acqorder, challenge);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
        }
        RlogEx.i(TAG, "enableVSim finish, result is " + result);
        return result;
    }

    public boolean disableVSim() {
        RlogEx.i(TAG, "disableVSim");
        boolean result = false;
        try {
            result = getIHwVSim().disableVSim();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
        }
        RlogEx.i(TAG, "disableVSim finish, result is " + result);
        return result;
    }

    public boolean hasHardIccCardForVSim(int subId) {
        try {
            return getIHwVSim().hasHardIccCardForVSim(subId);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int getSimMode(int subId) {
        try {
            return getIHwVSim().getSimMode(subId);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public void recoverSimMode() {
        try {
            getIHwVSim().recoverSimMode();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
        }
    }

    public String getRegPlmn(int subId) {
        try {
            return getIHwVSim().getRegPlmn(subId);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return null;
        }
    }

    public String getTrafficData() {
        try {
            return getIHwVSim().getTrafficData();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return null;
        }
    }

    public Boolean clearTrafficData() {
        try {
            return Boolean.valueOf(getIHwVSim().clearTrafficData());
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public boolean dsFlowCfg(int repFlag, int threshold, int totalThreshold, int oper) {
        RlogEx.i(TAG, "dsFlowCfg, repFlag = " + repFlag + ", threshold = " + threshold + ", totalThreshold = " + totalThreshold + ", oper = " + oper);
        try {
            return getIHwVSim().dsFlowCfg(repFlag, threshold, totalThreshold, oper);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int getSimStateViaSysinfoEx(int subId) {
        try {
            return getIHwVSim().getSimStateViaSysinfoEx(subId);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public int getCpserr(int subId) {
        try {
            return getIHwVSim().getCpserr(subId);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return 0;
        }
    }

    public int scanVsimAvailableNetworks(int subId, int type) {
        try {
            return getIHwVSim().scanVsimAvailableNetworks(subId, type);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public int getVsimAvailableNetworks(int subId, int type) {
        return scanVsimAvailableNetworks(subId, type);
    }

    public boolean setUserReservedSubId(int subId) {
        RlogEx.i(TAG, "setUserReservedSubId subId = " + subId);
        try {
            return getIHwVSim().setUserReservedSubId(subId);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int getUserReservedSubId() {
        try {
            return getIHwVSim().getUserReservedSubId();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public String getDevSubMode(int subscription) {
        try {
            return getIHwVSim().getDevSubMode(subscription);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return null;
        }
    }

    public String getDevSubMode() {
        if (TelephonyManagerEx.isMultiSimEnabled()) {
            return getDevSubMode(SubscriptionManagerEx.getPhoneId(SubscriptionManager.getDefaultSubscriptionId()));
        }
        RlogEx.e(TAG, "getDevSubMode not support");
        return null;
    }

    public String getPreferredNetworkTypeForVSim(int subscription) {
        try {
            return getIHwVSim().getPreferredNetworkTypeForVSim(subscription);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return null;
        }
    }

    public String getPreferredNetworkTypeForVSim() {
        if (TelephonyManagerEx.isMultiSimEnabled()) {
            int subId = getVSimSubId();
            if (subId != -1) {
                return getPreferredNetworkTypeForVSim(subId);
            }
            RlogEx.e(TAG, "getPreferredNetworkTypeForVSim vsim not enabled");
            return null;
        }
        RlogEx.e(TAG, "getPreferredNetworkTypeForVSim not support");
        return null;
    }

    public int getVSimCurCardType() {
        try {
            return getIHwVSim().getVSimCurCardType();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public int getOperatorWithDeviceCustomed() {
        if (isFullNetworkSupported()) {
            return 5;
        }
        if (IS_DUAL_CMCC_UNICOM_DEVICE) {
            return 4;
        }
        if (HuaweiTelephonyConfigs.isChinaMobile()) {
            return 2;
        }
        if (HuaweiTelephonyConfigs.isChinaTelecom()) {
            return 1;
        }
        if (HuaweiTelephonyConfigs.isChinaUnicom()) {
            return 3;
        }
        return -1;
    }

    private boolean isFullNetworkSupported() {
        return SystemPropertiesEx.getBoolean(HwTelephonyProperties.PROPERTY_FULL_NETWORK_SUPPORT, false);
    }

    public int getDeviceNetworkCountryIso() {
        return SystemPropertiesEx.getBoolean(PROP_OVERSEAS_MODE, false) ? -1 : 1;
    }

    public String getVSimNetworkOperator() {
        return SystemPropertiesEx.get("gsm.operator.numeric.vsim");
    }

    public String getVSimNetworkCountryIso() {
        return SystemPropertiesEx.get("gsm.operator.iso-country.vsim");
    }

    public String getVSimNetworkOperatorName() {
        return SystemPropertiesEx.get("gsm.operator.alpha.vsim");
    }

    public int getVSimNetworkType() {
        try {
            return getIHwVSim().getVSimNetworkType();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return 0;
        }
    }

    public String getVSimNetworkTypeName() {
        switch (getVSimNetworkType()) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case NETWORK_TYPE_HSUPA /*{ENCODED_INT: 9}*/:
                return "HSUPA";
            case 10:
                return "HSPA";
            case NETWORK_TYPE_IDEN /*{ENCODED_INT: 11}*/:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case NETWORK_TYPE_LTE /*{ENCODED_INT: 13}*/:
                return "LTE";
            case NETWORK_TYPE_EHRPD /*{ENCODED_INT: 14}*/:
                return "CDMA - eHRPD";
            case NETWORK_TYPE_HSPAP /*{ENCODED_INT: 15}*/:
                return "HSPA+";
            case 16:
                return "GSM";
            case NETWORK_TYPE_TDS /*{ENCODED_INT: 17}*/:
                return "TD-SCDMA";
            case NETWORK_TYPE_TDS_HSDPA /*{ENCODED_INT: 18}*/:
                return "TDS_HSDPA";
            case NETWORK_TYPE_TDS_HSUPA /*{ENCODED_INT: 19}*/:
                return "TDS_HSUPA";
            default:
                return "UNKNOWN";
        }
    }

    public String getVSimSubscriberId() {
        try {
            return getIHwVSim().getVSimSubscriberId();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return null;
        }
    }

    public boolean setVSimULOnlyMode(boolean isULOnly) {
        try {
            return getIHwVSim().setVSimULOnlyMode(isULOnly);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public boolean getVSimULOnlyMode() {
        try {
            return getIHwVSim().getVSimULOnlyMode();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int getVSimPlatformCapability() {
        if (!isPlatformSupportVsim()) {
            return 0;
        }
        if (isVSimDsdsVersionOne()) {
            return 2;
        }
        int i = VSIM_MODEM_COUNT;
        if (i == 2) {
            return 5;
        }
        return i;
    }

    public int getVSimOccupiedSubId() {
        try {
            return getIHwVSim().getVSimOccupiedSubId();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public int getPlatformSupportVSimVer(int key) {
        try {
            return getIHwVSim().getPlatformSupportVSimVer(key);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return -1;
        }
    }

    public boolean switchVSimWorkMode(int workMode) {
        try {
            return getIHwVSim().switchVSimWorkMode(workMode);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int enableVSim(String imsi, int cardtype, int apntype, String acqorder, String challenge) {
        return enableVSim(1, imsi, cardtype, apntype, acqorder, challenge);
    }

    public int setApn(int cardtype, int apntype, String challenge) {
        return enableVSim(2, null, cardtype, apntype, null, challenge);
    }

    public int enableVSim(String imsi, int cardtype, int apntype, String acqorder, String tapath, int vsimloc, String challenge) {
        return enableVSim(1, imsi, cardtype, apntype, acqorder, tapath, vsimloc, challenge);
    }

    public int setApn(String imsi, int cardtype, int apntype, String tapath, String challenge) {
        return enableVSim(2, imsi, cardtype, apntype, null, tapath, -1, challenge);
    }

    public int enableVSim(int operation, String imsi, int cardtype, int apntype, String acqorder, String tapath, int vsimloc, String challenge) {
        String str;
        int result;
        RlogEx.i(TAG, "enableVSim, operation = " + operation + ", cardtype = " + cardtype + ", apntype = " + apntype + ", acqorder = " + acqorder + ", vsimloc = " + vsimloc + ",challenge = " + challenge);
        try {
            IHwVSim iHwVSim = getIHwVSim();
            str = TAG;
            try {
                result = iHwVSim.enableVSimV2(operation, imsi, cardtype, apntype, acqorder, tapath, vsimloc, challenge);
            } catch (RemoteException e) {
                ex = e;
                printExLogIfSupportVSim(ex);
                result = 3;
                RlogEx.i(str, "enableVSim finish, result is " + result);
                return result;
            }
        } catch (RemoteException e2) {
            ex = e2;
            str = TAG;
            printExLogIfSupportVSim(ex);
            result = 3;
            RlogEx.i(str, "enableVSim finish, result is " + result);
            return result;
        }
        RlogEx.i(str, "enableVSim finish, result is " + result);
        return result;
    }

    public int enableVSim(int operation, Bundle bundle) {
        RlogEx.i(TAG, "enableVSim, operation = " + operation);
        int result = 3;
        try {
            result = getIHwVSim().enableVSimV3(operation, bundle);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
        }
        RlogEx.i(TAG, "enableVSim finish, result is " + result);
        return result;
    }

    public boolean isSupportVSimByOperation(int operation) {
        try {
            return getIHwVSim().isSupportVSimByOperation(operation);
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
            return false;
        }
    }

    public int dialupForVSim() {
        RlogEx.i(TAG, "dialupForVSim");
        int result = -1;
        try {
            result = getIHwVSim().dialupForVSim();
        } catch (RemoteException ex) {
            printExLogIfSupportVSim(ex);
        }
        RlogEx.i(TAG, "dialupForVSim finish, result is " + result);
        return result;
    }

    public static boolean isVSimDsdsVersionOne() {
        return VSIM_DSDS_VERSION_PROP == 1;
    }

    private void printExLogIfSupportVSim(RemoteException ex) {
        if (sIsPlatformSupportVSim) {
            RlogEx.e(TAG, "RemoteException ex = " + ex);
        }
    }
}
