
package com.nano.lanshare.utils;

import java.lang.reflect.Method;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiManagerUtils {
    public static int getWifiApStatus(WifiManager wifiManager) {
        return 0;
    }

    public static boolean setWifiApEnabled(WifiManager wifiManager, WifiConfiguration cfg,
            boolean flag) {
        try {
            if (wifiManager.isWifiEnabled() && flag) {
                wifiManager.setWifiEnabled(false);
            }
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, Boolean.TYPE);
            Boolean enable = (Boolean) method.invoke(wifiManager, cfg, flag);
            if (!flag) {
                wifiManager.setWifiEnabled(true);
            }
            return enable;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List getWifiApList(WifiManager wifiManager) {
        // wifiManager.s
        return null;
    }

    public static boolean connect2WifiAp(WifiManager wifiManager, String ssid) {
        WifiConfiguration apConfig = new WifiConfiguration();
        apConfig.SSID = "\"" + ssid + "\"";
        apConfig.hiddenSSID = true;
        apConfig.status = WifiConfiguration.Status.ENABLED;
        apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        int wcgID = wifiManager.addNetwork(apConfig);
        boolean flag = wifiManager.enableNetwork(wcgID, true);
        return flag;
    }

}
