package com.android.internal.telephony;

public interface HwTelephonyProperties {
    public static final String DISABLED_APN_TYPES = "ro.hwpp.disabled_apn_type";
    public static final String ENABLE_TCP_UDP_SUM = "ro.hwpp_enable_tcp_udp_sum";
    public static final String GSM_ONLY_NOT_ALLOW_PS = "gsm.data.gsm_only_not_allow_ps";
    public static final String MIP_ERR_ENABLE = "ro.config.hw_mip_error_dialog";
    public static final String PROPERTY_ALLOW_DATA_ONLY_PS = "ro.hwpp.allow_data_onlycs";
    public static final String PROPERTY_CG_STANDBY_MODE = "persist.radio.cg_standby_mode";
    public static final String PROPERTY_COMMRIL_MODE = "persist.radio.commril_mode";
    public static final String PROPERTY_DEFAULT_SUBSCRIPTION = "persist.radio.default_sub";
    public static final String PROPERTY_DISABLE_LOCATION_QUERY_SCREENOFF = "ro.config.updatelocation";
    public static final String PROPERTY_FDN_ACTIVATED_SUB1 = "gsm.hw.fdn.activated1";
    public static final String PROPERTY_FDN_ACTIVATED_SUB2 = "gsm.hw.fdn.activated2";
    public static final String PROPERTY_FDN_PS_FLAG_EXISTS_SUB1 = "gsm.hw.fdn.ps.flag.exists1";
    public static final String PROPERTY_FDN_PS_FLAG_EXISTS_SUB2 = "gsm.hw.fdn.ps.flag.exists2";
    public static final String PROPERTY_FULL_NETWORK_SUPPORT = "ro.config.full_network_support";
    public static final String PROPERTY_GLOBAL_CUST_ECCLIST = "gsm.hw.cust.ecclist";
    public static final String PROPERTY_GLOBAL_VERSION_IS_VMN_SHORT_CODE = "gsm.hw.matchnum.vmn_shortcode";
    public static final String PROPERTY_GLOBAL_VERSION_NUM_MATCH = "gsm.hw.matchnum";
    public static final String PROPERTY_GLOBAL_VERSION_NUM_MATCH_ROAMING = "gsm.hw.matchnum.roaming";
    public static final String PROPERTY_GLOBAL_VERSION_NUM_MATCH_SHORT = "gsm.hw.matchnum.short";
    public static final String PROPERTY_GLOBAL_VERSION_NUM_MATCH_SHORT_ROAMING = "gsm.hw.matchnum.short.roaming";
    public static final String PROPERTY_GLOBAL_VERSION_SMS_7BIT = "gsm.sms.7bit.enabled";
    public static final String PROPERTY_GLOBAL_VERSION_SMS_CODING = "gsm.sms.coding.national";
    public static final String PROPERTY_GSM_SIM_UPDATE_NITZ = "gsm.sim.updatenitz";
    public static final String PROPERTY_MAX_SMS_MESSAGE_SIZE = "gsm.sms.max.message.size";
    public static final String PROPERTY_MMS_TRANSACTION = "mms.transaction";
    public static final String PROPERTY_NATIONAL_MODE = "persist.radio.hw.ctmode";
    public static final String PROPERTY_NETWORK_COUNTRY_ISO = "gsm.hw.operator.iso-country";
    public static final String PROPERTY_NETWORK_ISROAMING = "gsm.hw.operator.isroaming";
    public static final String PROPERTY_NETWORK_OPERATOR = "gsm.hw.operator.numeric";
    public static final String PROPERTY_NETWORK_OPERATOR_OLD = "gsm.hw.operator.numeric.old";
    public static final String PROPERTY_SET_UICC_BY_RADIOPOWER = "ro.hwpp.set_uicc_by_radiopower";
    public static final String PROPERTY_SHOW_4G_PLUS_ICON = "ro.config.hw_show_4G_Plus_icon";
    public static final String PROPERTY_SMS_To_MMS_TEXTTHRESHOLD = "gsm.sms.to.mms.textthreshold";
    public static final String PROP_PRE_POST_PAY = "ro.config.hw_is_pre_post_pay";
}
