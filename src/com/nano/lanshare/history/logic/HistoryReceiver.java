
package com.nano.lanshare.history.logic;

import com.nano.lanshare.Model.TrafficInfo;
import com.nano.lanshare.Model.TrafficStatusManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HistoryReceiver extends BroadcastReceiver {
    public static final String TRAFFIC_ACTION_UPDATE = "com.nano.lanshare.TRAFFIC_UPDATE_ACIONT";
    private TrafficStatusManager mStatusManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TRAFFIC_ACTION_UPDATE.endsWith(intent.getAction())) {
            TrafficInfo info = (TrafficInfo) intent.getSerializableExtra("traffic");
            mStatusManager.insert(info);
        }
    }

}
