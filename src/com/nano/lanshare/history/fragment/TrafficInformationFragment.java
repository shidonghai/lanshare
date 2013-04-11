
package com.nano.lanshare.history.fragment;

import java.text.DecimalFormat;

import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nano.lanshare.R;

public class TrafficInformationFragment extends FragmentActivity {

    private TextView mSendTraffic;
    private TextView mRecvTraffic;
    private TextView mMobileTraffic;
    private TextView mHotspotsTraffic;
    private TextView mWIFITraffic;

    private Button mBackButton;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.traffic_status);

        initView();
        setData();
    }

    private void initView() {
        mSendTraffic = (TextView) findViewById(R.id.send_traffic);
        mRecvTraffic = (TextView) findViewById(R.id.recv_traffic);
        mMobileTraffic = (TextView) findViewById(R.id.mobile_traffic);
        mHotspotsTraffic = (TextView) findViewById(R.id.hotspots_traffic);
        mWIFITraffic = (TextView) findViewById(R.id.wifi_traffic);
        mBackButton = (Button) findViewById(R.id.btn_back);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        mSendTraffic.setText(fromatBytes(getTotalTxBytes()));
        mRecvTraffic.setText(fromatBytes(getTotalRxBytes()));
        mMobileTraffic.setText(fromatBytes(0));
        mHotspotsTraffic.setText("0.00KB");
        mWIFITraffic.setText(fromatBytes(0));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public long getTotalRxBytes() {
        // 获取总的接受字节数，包含Mobile和WiFi等
        return TrafficStats.getUidRxBytes(Process.myUid()) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats
                        .getUidRxBytes(Process.myUid()));
    }

    public long getTotalTxBytes() {
        // 总的发送字节数，包含Mobile和WiFi等
        return TrafficStats.getUidTxBytes(Process.myUid()) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getUidTxBytes(Process.myUid()));
    }

    public long getMobileRxBytes() {
        // 获取通过Mobile连接收到的字节总数，不包含WiFi
        return TrafficStats.getMobileRxBytes() == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats
                .getMobileRxBytes());
    }

    public long getWIFITotalBytes() {
        return 0;
    }

    public String fromatBytes(long bytes) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        float tb = bytes / 8 / 1024;
        if (tb < 1000) {
            return fnum.format(tb) + "KB";
        }
        tb = bytes / 1024;
        if (tb < 1000) {
            return fnum.format(tb) + "MB";
        }
        tb = bytes / 1024;
        return fnum.format(tb) + "GB";

    }
}
