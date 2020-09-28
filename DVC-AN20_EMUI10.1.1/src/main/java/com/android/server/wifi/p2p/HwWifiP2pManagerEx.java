package com.android.server.wifi.p2p;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManagerUtils;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import com.huawei.android.net.wifi.HwHiLogEx;
import com.huawei.android.net.wifi.HwWifiAdapterEx;
import com.huawei.utils.reflect.EasyInvokeFactory;

public class HwWifiP2pManagerEx {
    public static final int ADD_P2P_VALID_DEVICE = 141264;
    public static final int BASE = 139264;
    public static final int BEAM_CONNECT = 141267;
    public static final int CLEAR_P2P_VALID_DEVICE = 141266;
    public static final int CREATE_GROUP_PSK = 141268;
    public static final int DISABLE_P2P_RANDOM_MAC = 141272;
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_FREQUENCY = "freq";
    public static final String EXTRA_INTERFACE_NAME = "p2pInterfaceName";
    public static final String EXTRA_LINKSPEED = "linkSpeed";
    public static final String EXTRA_P2P_CONFIG_INFO = "p2pconfigInfo";
    public static final String EXTRA_WFD_INFO = "exinfo";
    public static final String EXTRA_WIFI_P2P_CONNECT_STATE = "extraState";
    public static final String GROUP_CREATED_ACTION = "android.net.wifi.p2p.GROUP_CREATED";
    public static final String MACICLINK_INTERFACE_CREATED_ACTION = "android.net.wifi.p2p.INTERFACE_CREATED";
    public static final int MAGICLINK_CONNECT = 141269;
    public static final int MAGICLINK_CREATE_GROUP = 141270;
    public static final String MAGICLINK_P2P_CONFIG_INFO = "android.net.wifi.p2p.CONFIG_INFO";
    public static final String MAGICLINK_PERMISSION = "com.huawei.instantshare.permission.ACCESS_INSTANTSHARE";
    public static final int MAGICLINK_REMOVE_GC_GROUP = 141271;
    public static final String NETWORK_CONNECTED_ACTION = "android.net.wifi.p2p.NETWORK_CONNECTED_ACTION";
    public static final String NETWORK_DISCONNECTED_ACTION = "android.net.wifi.p2p.NETWORK_DISCONNECTED_ACTION";
    public static final int REMOVE_P2P_VALID_DEVICE = 141265;
    public static final int SET_HWSINKCONFIG = 141273;
    public static final String STA_FREQUENCY_CREATED_ACTION = "android.net.wifi.p2p.STA_FREQUENCY_CREATED";
    private static final String TAG = "HwWifiP2pManagerEx";
    public static final String WFD_HW_DEVICE_EX_INFO = "com.huawei.net.wifi.p2p.peers.hw.extend.info";
    public static final String WFD_LINKSPEED_INFO = "com.huawei.net.wifi.p2p.LINK_SPEED";
    public static final String WFD_PERMISSION = "com.huawei.wfd.permission.ACCESS_HW_P2P_WFD";
    public static final String WIFI_P2P_CONNECT_STATE_CHANGED_ACTION = "android.net.wifi.p2p.CONNECT_STATE_CHANGE";
    public static final int WIFI_P2P_STATE_CONNECTED = 2;
    public static final int WIFI_P2P_STATE_CONNECTIING = 1;
    public static final int WIFI_P2P_STATE_CONNECTION_FAIL = 3;
    public static final String WIFI_P2P_VALID_DEVICE = "avlidDevice";
    private static HwWifiP2pManagerEx mInstance = new HwWifiP2pManagerEx();
    private static WifiP2pManagerUtils wifiP2pManagerUtils = EasyInvokeFactory.getInvokeUtils(WifiP2pManagerUtils.class);

    public static HwWifiP2pManagerEx getDefault() {
        return mInstance;
    }

    public void addP2PValidDevice(WifiP2pManager.Channel c, String deviceAddress, WifiP2pManager.ActionListener listener) {
        checkChannel(c);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("avlidDevice", deviceAddress);
        msg.what = ADD_P2P_VALID_DEVICE;
        msg.setData(bundle);
    }

    private void checkChannel(WifiP2pManager.Channel c) {
        if (c == null) {
            throw new IllegalArgumentException("Channel needs to be initialized");
        }
    }

    public void removeP2PValidDevice(WifiP2pManager.Channel c, String deviceAddress, WifiP2pManager.ActionListener listener) {
        checkChannel(c);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("avlidDevice", deviceAddress);
        msg.what = REMOVE_P2P_VALID_DEVICE;
        msg.setData(bundle);
    }

    public void clearP2PValidDevice(WifiP2pManager.Channel c, WifiP2pManager.ActionListener listener) {
        checkChannel(c);
        Message.obtain().what = CLEAR_P2P_VALID_DEVICE;
    }

    public void beam_connect(WifiP2pManager.Channel c, WifiP2pConfig config, WifiP2pManager.ActionListener listener) {
        sendMessageToChanel(c, BEAM_CONNECT, 0, wifiP2pManagerUtils.putListener(c, listener), config);
    }

    public void createGroupWifiRepeater(WifiP2pManager.Channel c, WifiConfiguration wifiConfig, WifiP2pManager.ActionListener listener) {
        sendMessageToChanel(c, CREATE_GROUP_PSK, 0, wifiP2pManagerUtils.putListener(c, listener), wifiConfig);
    }

    public void magiclinkConnect(WifiP2pManager.Channel c, String config, WifiP2pManager.ActionListener listener) {
        HwHiLogEx.d(TAG, false, "enter magiclinkConnect", new Object[0]);
        Bundle bd = new Bundle();
        bd.putString("cfg", config);
        sendMessageToChanel(c, MAGICLINK_CONNECT, 0, wifiP2pManagerUtils.putListener(c, listener), bd);
    }

    public void magiclinkCreateGroup(WifiP2pManager.Channel c, String frequency, WifiP2pManager.ActionListener listener) {
        HwHiLogEx.d(TAG, false, "enter magiclinkCreateGroup temporary", new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FREQUENCY, frequency);
        sendMessageToChanel(c, MAGICLINK_CREATE_GROUP, -1, wifiP2pManagerUtils.putListener(c, listener), bundle);
    }

    public void magiclinkRemoveGcGroup(WifiP2pManager.Channel c, String iface, WifiP2pManager.ActionListener listener) {
        HwHiLogEx.d(TAG, false, "enter magiclinkRemoveGcGroup", new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putString("iface", iface);
        sendMessageToChanel(c, MAGICLINK_REMOVE_GC_GROUP, 0, wifiP2pManagerUtils.putListener(c, listener), bundle);
    }

    public void discoverPeers(WifiP2pManager.Channel c, int channelId, WifiP2pManager.ActionListener listener) {
        HwHiLogEx.d(TAG, false, "discoverPeers, pid:%{public}d, tid:%{public}d, uid:%{public}d, channelId %{public}d", new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), Integer.valueOf(Process.myUid()), Integer.valueOf(channelId)});
        sendMessageToChanel(c, 139265, channelId, wifiP2pManagerUtils.putListener(c, listener));
    }

    public void setHwSinkConfig(WifiP2pManager.Channel channel, String sinkConfig, WifiP2pManager.ActionListener listener) {
        HwHiLogEx.d(TAG, false, "setHwSinkConfig", new Object[0]);
        if (sinkConfig == null || listener == null || channel == null) {
            HwHiLogEx.d(TAG, false, "setHwSinkConfig invalid params.", new Object[0]);
            return;
        }
        String[] tokens = sinkConfig.split(",");
        if (tokens == null || tokens.length != 4) {
            HwHiLogEx.d(TAG, false, "invalid sinkconfig", new Object[0]);
            listener.onFailure(0);
        } else if (tokens[0].length() % 2 != 0) {
            listener.onFailure(0);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("sinkConfig", sinkConfig);
            sendMessageToChanel(channel, SET_HWSINKCONFIG, 0, wifiP2pManagerUtils.putListener(channel, listener), bundle);
            HwHiLogEx.d(TAG, false, "setHwSinkConfig End", new Object[0]);
        }
    }

    public void disableP2pRandomMac(WifiP2pManager.Channel channel) {
        sendMessageToChanel(channel, DISABLE_P2P_RANDOM_MAC, 0, 0);
    }

    private void sendMessageToChanel(WifiP2pManager.Channel c, int what, int arg1, int arg2, Object obj) {
        checkChannel(c);
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        HwWifiAdapterEx.sendMessage(c, msg);
    }

    private void sendMessageToChanel(WifiP2pManager.Channel c, int what, int arg1, int arg2) {
        checkChannel(c);
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        HwWifiAdapterEx.sendMessage(c, msg);
    }
}
