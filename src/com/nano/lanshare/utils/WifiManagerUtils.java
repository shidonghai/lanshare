
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

}
