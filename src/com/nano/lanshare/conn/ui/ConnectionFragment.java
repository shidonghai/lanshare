package com.nano.lanshare.conn.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.conn.logic.UserChangedListener;
import com.nano.lanshare.conn.logic.UserManager;
import com.nano.lanshare.conn.ui.PullToRefreshListView.OnRefreshListener;
import com.nano.lanshare.main.BaseActivity;
import com.nano.lanshare.socket.SocketBroadcastReceiver;
import com.nano.lanshare.socket.SocketService;
import com.nano.lanshare.socket.moudle.Stranger;

public class ConnectionFragment extends BasicTabFragment implements
		OnClickListener, UserChangedListener {
	private ListView mUserListView;
	private LayoutInflater mInflater;
	private Button mSearchHotspots;
	private View mEmptyView;
	private ViewGroup mEmptyHotspots;
	private ListView mHotspotsList;
	private HotspotsView mHotspotsView;
	private List<ScanResult> mWifiList;

	private UserManager mUserManager;

	private UserAdapter mAdapter;

	private SocketBroadcastReceiver mReceiver;
	private Intent mIntent;
	private String mFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserManager = UserManager.getInstance();
		mAdapter = new UserAdapter(getActivity());
		mReceiver = new SocketBroadcastReceiver(getActivity());
	}

	private void registerReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		getActivity().registerReceiver(
				mHotspotsView.getWifiStatusChangeReciver(), intentFilter);
	}

	private void unregisterReceiver() {
		getActivity().unregisterReceiver(
				mHotspotsView.getWifiStatusChangeReciver());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver();
		mUserManager.registerCallback(this);
		mReceiver.unregister();
	}

	@Override
	public void onResume() {
		super.onResume();
		mUserManager.registerCallback(this);
		registerReceiver();

		mReceiver.register();

		mIntent = getActivity().getIntent();
		if (mIntent == null) {
			return;
		}
		if (BaseActivity.PICK_A_FRIEND_AND_SEND.equals(mIntent.getAction())) {
			mFile = mIntent.getStringExtra("file");
		}
	}

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		initLeftView();
		initRightView();
	}

	private void initLeftView() {
		setTitle(LEFT, getString(R.string.wifi_conn, 0));
		View friendsContent = mInflater.inflate(R.layout.connect_people_list,
				null);
		getGroup(LEFT).addView(friendsContent);
		mEmptyView = friendsContent.findViewById(R.id.empty_list);
		mUserListView = (ListView) friendsContent
				.findViewById(R.id.friends_list);
		mUserListView.setAdapter(mAdapter);
		((PullToRefreshListView) mUserListView)
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						new GetDataTask().execute();
					}
				});
		mUserListView.setOnItemClickListener(new OnUserClickListener());

	}

	private void initRightView() {
		setTitle(RIGHT, getString(R.string.hot_spot, 0));
		/*
		 * View hotspotList = mInflater.inflate(R.layout.connect_hotspots_list,
		 * null); mSearchHotspots = (Button)
		 * hotspotList.findViewById(R.id.search_hotspots_button);
		 * mSearchHotspots.setOnClickListener(this); mEmptyHotspots =
		 * (ViewGroup) hotspotList.findViewById(R.id.empty_hotspots);
		 * mHotspotsList = (ListView)
		 * hotspotList.findViewById(R.id.hotspots_list);
		 */
		mHotspotsView = new HotspotsView(getActivity());
		getGroup(RIGHT).addView(mHotspotsView.getView());

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mSearchHotspots) {
			// new SearchHotspotsTask().execute();
		}
	}

	private void showHotspotsLoading(boolean show) {
		if (show) {

		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				;
			}
			return new String[] { "aa" };
		}

		@Override
		protected void onPostExecute(String[] result) {

			// Call onRefreshComplete when the list has been refreshed.
			((PullToRefreshListView) mUserListView).onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	@Override
	public void notifyUserChanged(ArrayList<Stranger> list) {
		if (list.size() > 0) {
			mUserListView.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
		} else {
			mEmptyView.setVisibility(View.VISIBLE);
			mUserListView.setVisibility(View.GONE);
		}
		mAdapter.addUsers(list);
	}

	class OnUserClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (mFile != null) {
				// TODO send
				Intent intent = new Intent(getActivity(), SocketService.class);
				intent.putExtra(SocketService.CMD_CODE,
						SocketService.CMD_TRANSFER);
				intent.putExtra(SocketService.DATA, mFile);
				intent.putExtra(SocketService.TARGET_USER,
						mAdapter.getItem(position - 1));
				getActivity().startService(intent);
				getActivity().finish();
			}
		}

	}
}
