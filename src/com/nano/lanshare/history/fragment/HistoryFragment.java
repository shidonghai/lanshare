
package com.nano.lanshare.history.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.Model.HistoryDBManager;
import com.nano.lanshare.Model.IDataBaseChange;
import com.nano.lanshare.history.adapter.HistoryListAdapter;
import com.nano.lanshare.history.been.HistoryInfo;

public class HistoryFragment extends Fragment implements OnItemClickListener,
        LoaderCallbacks<List<HistoryInfo>>, IDataBaseChange {
    // The space of storage
    private TextView mStorageSpace;
    // The net flow info
    private Button mTraffiInfo;
    private Button mKaiyaInfo;
    // The list of the history
    private ListView mHistoryList;
    private HistoryListAdapter mAdapter;
    private HistoryDBManager mDbManager;
    private LoaderManager mLoaderManager;
    public static final int QUERY_LIST = 1;
    public static final int DELET_ITEMS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        // View listHeader = inflater.inflate(R.layout.history_list_header,
        // null);
        mHistoryList = (ListView) view.findViewById(R.id.history_list);
        initView(view);
        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(QUERY_LIST, null, this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HistoryListAdapter(getActivity(), null, true);
        mDbManager = new HistoryDBManager(getActivity());
        mDbManager.registerContentObserver(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDbManager != null) {
            mDbManager.closeDB();
            mDbManager.registerContentObserver(this);
        }
    }

    private void initView(View view) {
        mHistoryList.setAdapter(mAdapter);
        mHistoryList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void setChecked(View view,int position) {
        mAdapter.setChecked(view,position);
    }

    public void deteleItems() {

    }

    private static class HistoryLoader extends AsyncTaskLoader<List<HistoryInfo>> {
        HistoryDBManager dbManager;

        public HistoryLoader(Context context, HistoryDBManager dao) {
            super(context);
            dbManager = dao;
        }

        @Override
        public List<HistoryInfo> loadInBackground() {
            // just test date
            /*
             * for (int i = 0; i < 20; i++) { HistoryInfo info = new
             * HistoryInfo(); info.filePath = "1" + i; info.date =
             * System.currentTimeMillis() + ""; info.reciver = (i + 33) + "";
             * info.sender = (i + 222) + ""; info.historyType = i % 2;
             * dbManager.insert(info); }
             */
            return dbManager.listAll();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }

    @Override
    public Loader<List<HistoryInfo>> onCreateLoader(int arg0, Bundle arg1) {
        return new HistoryLoader(getActivity(), mDbManager);
    }

    @Override
    public void onLoadFinished(Loader<List<HistoryInfo>> arg0, List<HistoryInfo> list) {
        mAdapter.setDate(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<HistoryInfo>> arg0) {
        mAdapter.setDate(null);
    }

    @Override
    public void onDataChange() {
        if (mLoaderManager.hasRunningLoaders()) {
            mLoaderManager.restartLoader(QUERY_LIST, null, this);
        }
    }

}
