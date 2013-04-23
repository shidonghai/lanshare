
package com.nano.lanshare.history.fragment;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.Model.HistoryDBManager;
import com.nano.lanshare.Model.IDataBaseChange;
import com.nano.lanshare.components.operation.OperationDialog;
import com.nano.lanshare.components.operation.PopupMenuUtil;
import com.nano.lanshare.history.adapter.HistoryListAdapter;
import com.nano.lanshare.history.adapter.HistoryListAdapter.onDataLoadChange;
import com.nano.lanshare.history.been.HistoryInfo;
import com.nano.lanshare.history.logic.IHistoryDelete;
import com.nano.lanshare.main.BaseActivity;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageCache.ImageCacheParams;
import com.nano.lanshare.thumbnail.util.ImageWorker;

public class HistoryFragment extends Fragment implements OnItemClickListener, onDataLoadChange,
        IHistoryDelete,
        OnItemLongClickListener,
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
    private OperationDialog mMenu;
    private HistoryInfo mClickInfo;
    private static final int LOAD_TYPE_RECODE = 0;
    private static final int LOAD_TYPE_MSG = 1;
    private int mLoadDateType = LOAD_TYPE_RECODE;
    public static final int QUERY_LIST = 1;
    public static final int DELET_ITEMS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        // View listHeader = inflater.inflate(R.layout.history_list_header,
        // null);
        mHistoryList = (ListView) view.findViewById(R.id.history_list);
        initView(view);
        mHistoryList.setOnItemClickListener(this);
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
        registeHistoryDeleteListener();
        setHasOptionsMenu(true);
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
        mMenu = new OperationDialog(getActivity());

        ImageWorker worker = ((LanshareApplication) getActivity()
                .getApplication()).getImageWorker();
        worker.addImageCache(getFragmentManager(), new ImageCacheParams(
                getActivity().getApplicationContext(), "diskcache"));

        mHistoryList.setOnScrollListener(worker.getScrollerListener());

        mHistoryList.setAdapter(mAdapter);
        mHistoryList.setOnItemClickListener(this);
    }

    /**
     * Set the items as check mode
     */
    public void setCheckedMode(boolean flag) {
        mAdapter.setCheckMode(flag);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.isCheckedMode()) {
            mAdapter.setChecked(view, position);
        } else {
            final HistoryInfo clickInfo = mAdapter.getItem(position);
            mMenu.setContent(PopupMenuUtil.HISTORY_OPERATER, PopupMenuUtil.HISTORY_OPERATER_NAME,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doHistoryClick(which, clickInfo);
                        }
                    });
            mMenu.showAsDropDown(view);
        }
    }

    /**
     * <p>
     * Click the list item and set the menu
     * </p>
     * 
     * @param which
     * @param clickInfo
     */
    private void doHistoryClick(int which, HistoryInfo clickInfo) {
        switch (which) {
            case PopupMenuUtil.FILE_SEND: {
                return;
            }
            case PopupMenuUtil.FILE_SHARE: {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment newFragment = ShareDialog.newInstance(clickInfo);
                newFragment.show(ft, "dialog");
                return;
            }
            case PopupMenuUtil.FILE_OPEN: {
                return;
            }
            case PopupMenuUtil.FILE_DELETE: {
                mDbManager.deleteByID(clickInfo.id);
                return;
            }
            case PopupMenuUtil.FILE_DETAIL: {
                // new DialogFragment().
                final PropertyDialog dialog = new PropertyDialog(getActivity());
                dialog.setButton(getString(R.string.dm_dialog_ok), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setVal(clickInfo);
                dialog.show();
                return;
            }

            default:
                break;
        }
    }

    public void registeHistoryDeleteListener() {
        ((BaseActivity) getActivity()).setDeleteListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mAdapter.isCheckedMode()) {
            menu.clear();
            getActivity().getMenuInflater().inflate(R.menu.menu_edit, menu);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_del) {
            // setCheckedMode(true);
        } else if (item.getItemId() == R.id.menu_select) {
            mAdapter.setAllChecked(true);
        } else if (item.getItemId() == R.id.menu_unselect) {
            mAdapter.setAllChecked(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    public void setChecked(View view, int position) {
        mAdapter.setChecked(view, position);
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
        Log.d("wyg", "size---->>" + list.size());
        mAdapter.setDate(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<HistoryInfo>> arg0) {
        mAdapter.setDate(null);
    }

    private class DeleteTask extends AsyncTask<List<HistoryInfo>, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(List<HistoryInfo>... list) {
            mDbManager.delete(list[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

    }

    @Override
    public void onDataChange() {
        mLoaderManager.restartLoader(QUERY_LIST, null, this);
    }

    @Override
    public void onChange() {
        if (mLoadDateType == LOAD_TYPE_MSG) {
            mLoadDateType = LOAD_TYPE_RECODE;
        } else {
            mLoadDateType = LOAD_TYPE_MSG;
        }
    }

    @Override
    public void prepareDelete() {
        setCheckedMode(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startDelete() {
        List<HistoryInfo> list = mAdapter.getSelectedList();
        if (list.isEmpty()) {
            return;
        } else {
            new DeleteTask().execute(list);
        }
    }

    @Override
    public void deleting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelDelete() {
        setCheckedMode(false);
    }

    @Override
    public void finishDeleted() {
        // TODO Auto-generated method stub

    }

}
