package com.nano.lanshare.apps.ui;

import java.util.List;

import android.content.pm.PackageInfo;
import android.os.Message;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;

import com.nano.lanshare.R;
import com.nano.lanshare.apps.AppListener;
import com.nano.lanshare.apps.AppLoader;
import com.nano.lanshare.apps.AppLoader.AppInfo;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.components.LongClickListener;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageCache.ImageCacheParams;
import com.nano.lanshare.thumbnail.util.ImageWorker;

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
		public void onLoaded(List<AppInfo> mGames, List<AppInfo> mApps) {
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

			notifyFinishLoading();
		}

	};

	GridView mLeftGrid;
	GridView mRightGrid;
	AppAdapter mLeftAdapter;
	AppAdapter mRightAdapter;

	@Override
	protected void init() {
		ImageWorker worker = ((LanshareApplication) getActivity()
				.getApplication()).getImageWorker();
		worker.addImageCache(getFragmentManager(), new ImageCacheParams(
				getActivity().getApplicationContext(), "diskcache"));
		// configure left
		mLeftGrid = new GridView(getActivity());
		mLeftGrid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mLeftGrid.setNumColumns(4);
		mLeftGrid.setVerticalSpacing(16);
		getGroup(LEFT).addView(mLeftGrid);
		mLeftAdapter = new AppAdapter(getActivity().getApplicationContext(),
				worker, getLayoutInflater(null));
		mLeftGrid.setAdapter(mLeftAdapter);
		// configure right
		mRightGrid = new GridView(getActivity());
		mRightGrid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mRightGrid.setNumColumns(4);
		mRightGrid.setVerticalSpacing(16);
		mRightAdapter = new AppAdapter(getActivity().getApplicationContext(),
				worker, getLayoutInflater(null));
		getGroup(RIGHT).addView(mRightGrid);
		mRightGrid.setAdapter(mRightAdapter);

		// set listeners
		mLeftGrid.setOnScrollListener(worker.getScrollerListener());
		mRightGrid.setOnScrollListener(worker.getScrollerListener());
		LongClickListener mLongClickListener = new LongClickListener(
				getActivity(), R.id.icon);
		mLeftGrid.setOnItemLongClickListener(mLongClickListener);
		mRightGrid.setOnItemLongClickListener(mLongClickListener);

		// start loading data
		notifyStartLoading();
		mAppLoader = new AppLoader(getActivity());
		mAppLoader.setAppListener(mAppListener);
		mAppLoader.startLoading();
	}

	@Override
	public void onResume() {
		super.onResume();
		mAppLoader.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		mAppLoader.onStop();
	}

	@Override
	protected void onUpdateData(Message msg) {
		if (msg.arg1 == LEFT) {
			mLeftAdapter.setContent(msg.obj);
		} else {
			mRightAdapter.setContent(msg.obj);
		}
	}
}
