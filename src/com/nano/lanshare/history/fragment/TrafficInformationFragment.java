
package com.nano.lanshare.history.fragment;

import java.text.DecimalFormat;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.Model.TrafficInfo;
import com.nano.lanshare.Model.TrafficStatusManager;

public class TrafficInformationFragment extends FragmentActivity implements
        LoaderCallbacks<TrafficInfo> {

    private TextView mSendTraffic;
    private TextView mRecvTraffic;
    private TextView mMobileTraffic;
    private TextView mHotspotsTraffic;
    private TextView mWIFITraffic;
    private LoaderManager mLoaderManager;
    private TrafficStatusManager mTrafficStatusManager;

    private Button mBackButton;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.traffic_status);
        initView();
        mTrafficStatusManager = new TrafficStatusManager(this);
        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(0, null, this);
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

    private void setData(TrafficInfo info) {
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
        if (mTrafficStatusManager != null) {
            mTrafficStatusManager.closeDB();
        }
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

    static class TrafficStatusLoad extends AsyncTaskLoader<TrafficInfo> {
        TrafficStatusManager dbManager;

        public TrafficStatusLoad(Context context, TrafficStatusManager db) {
            super(context);
            dbManager = db;
        }

        @Override
        public TrafficInfo loadInBackground() {
            Log.d("wyg", "loadInBackground------------>>");
            return dbManager.query();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

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

    @Override
    public Loader<TrafficInfo> onCreateLoader(int arg0, Bundle arg1) {
        return new TrafficStatusLoad(this, mTrafficStatusManager);
    }

    @Override
    public void onLoadFinished(Loader<TrafficInfo> arg0, TrafficInfo info) {
        Log.d("wyg", "onLoadFinished------------>>");
        setData(info);
    }

    @Override
    public void onLoaderReset(Loader<TrafficInfo> arg0) {

    }
}
