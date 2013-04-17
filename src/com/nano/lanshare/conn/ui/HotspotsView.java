
package com.nano.lanshare.conn.ui;

import java.lang.reflect.Method;

import com.nano.lanshare.R;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HotspotsView implements OnClickListener {
    private Context mContext;
    private ListView mListView;
    private LayoutInflater mInflater;
    private Button mSearchHotspots;
    private ViewGroup mEmptyHotspots;
    private ListView mHotspotsList;
    private TextView mCreateHotspots;
    private WifiManager mWifiManager;
    private ProgressBar mProgressBar;

    public HotspotsView(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mInflater = LayoutInflater.from(mContext);
    }

    public View getView() {
        View hotspotList = mInflater.inflate(R.layout.connect_hotspots_list, null);
        mSearchHotspots = (Button) hotspotList.findViewById(R.id.search_hotspots_button);
        mSearchHotspots.setOnClickListener(this);
        mEmptyHotspots = (ViewGroup) hotspotList.findViewById(R.id.empty_hotspots);
        mHotspotsList = (ListView) hotspotList.findViewById(R.id.hotspots_list);
        mCreateHotspots = (TextView) hotspotList.findViewById(R.id.create_connect);
        mProgressBar = (ProgressBar) hotspotList.findViewById(R.id.search_hotspots_progress);
        mCreateHotspots.setOnClickListener(this);
        return hotspotList;
    }

    @Override
    public void onClick(View v) {
        if (v == mSearchHotspots) {

        } else if (v == mCreateHotspots) {
            new StartHotspotsTask().execute(Boolean.TRUE);
        }
    }

    private boolean setWifiApEnabled(boolean flag) {
        try {
            if (mWifiManager.isWifiEnabled() && flag) {
                mWifiManager.setWifiEnabled(false);
            }
            WifiConfiguration cfg = new WifiConfiguration();
            cfg.SSID = "lanshare1";
            cfg.preSharedKey = "";
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, Boolean.TYPE);
            Boolean enable = (Boolean) method.invoke(mWifiManager, cfg, flag);
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
        if (flag) {
            mProgressBar.setVisibility(View.VISIBLE);
            // mListView.setVisibility(View.GONE);
            mEmptyHotspots.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            // mListView.setVisibility(View.VISIBLE);
            mEmptyHotspots.setVisibility(View.VISIBLE);
        }
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

    private class StartHotspotsTask extends AsyncTask<Boolean, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            publishProgress(0);
            return setWifiApEnabled(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar(false);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressBar(false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            showProgressBar(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            showProgressBar(true);
        }
    }
}
