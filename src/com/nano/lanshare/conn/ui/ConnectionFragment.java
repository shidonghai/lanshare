package com.nano.lanshare.conn.ui;

import java.util.List;

import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.conn.ui.PullToRefreshListView.OnRefreshListener;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.socket.logic.SocketController;
import com.nano.lanshare.socket.moudle.DiscoveryMessage;
import com.nano.lanshare.socket.moudle.FileTransferMessage;
import com.nano.lanshare.socket.moudle.SMessage;
import com.nano.lanshare.socket.moudle.Stranger;

public class ConnectionFragment extends BasicTabFragment implements
		OnClickListener {

	private ListView mListView;
	private LayoutInflater mInflater;
	private Button mSearchHotspots;
	private ViewGroup mEmptyHotspots;
	private ListView mHotspotsList;
	private HotspotsView mHotspotsView;
	private List<ScanResult> mWifiList;

	private SocketController mController;

	private UserAdapter mAdapter;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1000: {
				mController.discover(1000);
//				mHandler.sendEmptyMessageDelayed(1000, 5000);
				break;
			}
			case SMessage.MSG_DISCOVER: {
				DiscoveryMessage discoverMsg = (DiscoveryMessage) msg.obj;

				// add this user to the user list
				Stranger stranger = new Stranger();
				stranger.setName(discoverMsg.getName());
				stranger.setUserIdentifier(discoverMsg.getMACAddress());
				stranger.setUserIp(discoverMsg.getRemoteAddress());
				stranger.setUserPhoto(discoverMsg.getPhoto());

				Toast.makeText(getActivity(), "find one", 1000).show();
				if (discoverMsg.getMsgDirection() == SMessage.REQ) {
					// response to Discovery
					mController.responseForDiscover(discoverMsg);
				}

				break;
			}
			case SMessage.MSG_FILE_TRANSFER: {
				// read this message, and response for it
				FileTransferMessage transferMessage = (FileTransferMessage) msg.obj;

				// otherwise, let UserChatAcitivity to handle

				break;
			}
			case SMessage.MSG_STATUS_UPDATE: {
				break;
			}
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver();
	}

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		initLeftView();
		initRightView();

		mController = ((LanshareApplication) getActivity().getApplication())
				.getSocketController();
		mController.setHandler(mHandler);
		mHandler.sendEmptyMessage(1000);
	}

	private void initLeftView() {
		setTitle(LEFT, getString(R.string.wifi_conn, 0));
		View friendsContent = mInflater.inflate(R.layout.connect_people_list,
				null);
		getGroup(LEFT).addView(friendsContent);
		mListView = (ListView) friendsContent.findViewById(R.id.friends_list);
		((PullToRefreshListView) mListView)
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						new GetDataTask().execute();
					}
				});
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
			((PullToRefreshListView) mListView).onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
