package com.nano.lanshare.apps.ui;

import java.util.List;

import android.content.pm.PackageInfo;
import android.os.Message;
import android.widget.GridView;

import com.nano.lanshare.R;
import com.nano.lanshare.apps.AppListener;
import com.nano.lanshare.apps.AppLoader;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.history.fragment.HistoryFragment;
import com.nano.lanshare.pics.ui.PicFragment;

public class AppFragment extends BasicTabFragment {
	private AppLoader mAppLoader;
	private AppListener mAppListener = new AppListener() {

		@Override
		public void onInstallApp(PackageInfo info) {
		}

		@Override
		public void onUninstallApp(PackageInfo info) {
		}

		@Override
		public void onLoading() {
		}

		@Override
		public void onLoaded(List<PackageInfo> mGames, List<PackageInfo> mApps) {
			Message msg1 = getHandler().obtainMessage();
			msg1.arg1 = LEFT;
			msg1.what = UPDATE_UI;
			msg1.obj = mGames;
			msg1.sendToTarget();

			Message msg2 = getHandler().obtainMessage();
			msg2.arg1 = RIGHT;
			msg2.what = UPDATE_UI;
			msg2.obj = mApps;
			msg2.sendToTarget();
		}

	};

	GridView mLeftGrid;
	GridView mRightGrid;
	AppAdapter mLeftAdapter;
	AppAdapter mRightAdapter;

	@Override
	protected void init() {
		// mAppLoader = new AppLoader(getActivity());
		// mAppLoader.setAppListener(mAppListener);
		// mAppLoader.startLoading();
		getFragmentManager().beginTransaction()
				.add(R.id.left_container, new HistoryFragment()).commit();
		// mLeftGrid = new GridView(getActivity());
		// mLeftGrid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		// mLeftGrid.setNumColumns(4);
		// getGroup(LEFT).addView(mLeftGrid);
		// mLeftAdapter = new AppAdapter(getLayoutInflater(null));
		// mLeftGrid.setAdapter(mLeftAdapter);
		//
		// mRightGrid = new GridView(getActivity());
		// mRightGrid.setLayoutParams(new
		// LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		// mRightGrid.setNumColumns(4);
		//
		// mRightAdapter = new AppAdapter(getLayoutInflater(null));
		// getGroup(RIGHT).addView(mRightGrid);
		// mRightGrid.setAdapter(mRightAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		// mAppLoader.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		// mAppLoader.onStop();
	}

	@Override
	protected void onUpdateData(Message msg) {
		// if (msg.arg1 == LEFT) {
		// mLeftAdapter.setContent(msg.obj);
		// } else {
		// mLeftAdapter.setContent(msg.obj);
		// }
	}
}
