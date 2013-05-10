
package com.nano.lanshare.conn.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.conn.adapter.ConnectAdapter;
import com.nano.lanshare.socket.moudle.Stranger;
import com.nano.lanshare.utils.WifiManagerUtils;

public class HotspotsView implements OnClickListener, OnItemClickListener {
    private Context mContext;
    private ListView mListView;
    private LayoutInflater mInflater;
    private Button mSearchHotspots;
    private ViewGroup mEmptyHotspots;
    private ListView mHotspotsList;
    private TextView mCreateHotspots;
    private WifiManager mWifiManager;
    private ProgressBar mProgressBar;
    private ConnectAdapter mAdapter;
    private boolean isWifiApEnabel = false;
    private static final int SEARCH_TIMEOUT = 5000;
    public static final int START_WIFI_AP = 1;
    public static final int SEARCH_WIFI_AP = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == START_WIFI_AP) {

            } else if (msg.what == SEARCH_WIFI_AP) {

            }
        };
    };

    private BroadcastReceiver mWifiStatusChangeReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                addWifiAp2List();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void addWifiAp2List() {
        List<ScanResult> res = mWifiManager.getScanResults();
        List<Stranger> wifiAps = new ArrayList<Stranger>();
        for (ScanResult scanResult : res) {
            Log.d("wyg", "wifiAps---scanResult-->>" + scanResult);
            if (("[ESS]".equals(scanResult.capabilities)|| "".equals(scanResult.capabilities))
                    && !wifiAps.contains(scanResult.SSID)) {
                Stranger user = new Stranger();
                user.setName(scanResult.SSID);
                user.setUserIdentifier(scanResult.BSSID);
                wifiAps.add(user);
            }
        }
        if (!wifiAps.isEmpty() && mHotspotsList != null
                && mHotspotsList.getVisibility() != View.VISIBLE) {
            mHotspotsList.setVisibility(View.VISIBLE);
            if (mEmptyHotspots != null
                    && mEmptyHotspots.getVisibility() == View.VISIBLE) {
                mEmptyHotspots.setVisibility(View.GONE);
            }
            mAdapter.addUsers(wifiAps);
            mAdapter.notifyDataSetChanged();
        }
        showProgressBar(false);
    }

    public BroadcastReceiver getWifiStatusChangeReciver() {
        return mWifiStatusChangeReciver;
    }

    public void updateView(Bundle bundle) {

    }

    public HotspotsView(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        mInflater = LayoutInflater.from(mContext);
    }

    public View getView() {
        View hotspotList = mInflater.inflate(R.layout.connect_hotspots_list,
                null);
        mSearchHotspots = (Button) hotspotList
                .findViewById(R.id.search_hotspots_button);
        mSearchHotspots.setOnClickListener(this);
        mEmptyHotspots = (ViewGroup) hotspotList
                .findViewById(R.id.empty_hotspots);
        mHotspotsList = (ListView) hotspotList.findViewById(R.id.hotspots_list);
        mAdapter = new ConnectAdapter(mContext);
        mHotspotsList.setAdapter(mAdapter);
        mHotspotsList.setOnItemClickListener(this);

        mCreateHotspots = (TextView) hotspotList
                .findViewById(R.id.create_connect);
        mProgressBar = (ProgressBar) hotspotList
                .findViewById(R.id.search_hotspots_progress);
        mCreateHotspots.setOnClickListener(this);
        return hotspotList;
    }

    private void connect2WifiAp(String ssid, String bssid) {
        WifiManagerUtils.connect2WifiAp(mWifiManager, ssid, bssid);
    }

    @Override
    public void onClick(View v) {
        if (v == mSearchHotspots) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showProgressBar(true);
                    mWifiManager.startScan();
                    HotspotsView.this.dissmissDialog(SEARCH_TIMEOUT);
                }
            });
        } else if (v == mCreateHotspots) {
            // new StartHotspotsTask().execute(Boolean.TRUE);
            showProgressBar(true);
            new Thread() {
                public void run() {
                    isWifiApEnabel = WifiManagerUtils.isWifiApEnable(mWifiManager);
                    Log.d("wyg", "isWifiApEnabel---->>" + isWifiApEnabel);
                    if (!isWifiApEnabel) {
                        setWifiApEnabled(!isWifiApEnabel);
                        isWifiApEnabel = true;
                        HotspotsView.this.dissmissDialog(SEARCH_TIMEOUT);
                    }
                }
            }.start();
        }
    }

    public void dissmissDialog(long time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
            }
        }, time >= 0 ? time : SEARCH_TIMEOUT);
    }

    private boolean setWifiApEnabled(boolean flag) {
        try {
            if (mWifiManager.isWifiEnabled() && flag) {
                mWifiManager.setWifiEnabled(false);
            }
            WifiConfiguration cfg = new WifiConfiguration();
            cfg.SSID = "lanshare1";
            cfg.preSharedKey = "";
            Boolean enable = WifiManagerUtils.setWifiApEnabled(mWifiManager,
                    cfg, flag);
            if (!flag) {
                mWifiManager.setWifiEnabled(true);
            }
            return enable;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showProgressBar(boolean flag) {
        if (mProgressBar == null) {
            return;
        }
        if (flag) {
            mProgressBar.setVisibility(View.VISIBLE);
            // mListView.setVisibility(View.GONE);
            mEmptyHotspots.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            // mListView.setVisibility(View.VISIBLE);
            if (mAdapter.isEmpty()) {
                mEmptyHotspots.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface onWifiApStatusChange {
        void onStatusChange();
    }

    private class SearchHotspotsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class StartHotspotsTask extends
            AsyncTask<Boolean, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            publishProgress(0);
            boolean a = setWifiApEnabled(params[0]);
            Log.d("wyg", "StartHotspotsTask.doInBackground---->>");
            return a;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressBar(false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("wyg", "StartHotspotsTask.onPostExecute---->>");
            super.onPostExecute(result);
            showProgressBar(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            showProgressBar(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Stranger user = mAdapter.getItem(position - 1);
        Log.d("wyg", "onItemClick----->>>" + user);
        connect2WifiAp(user.getName(),user.getUserIdentifier());
    }

}
