package com.nano.lanshare.apps.ui;

import android.os.Message;

import com.nano.lanshare.apps.AppLoader;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.components.DataLoadListener;

public class AppFragment extends BasicTabFragment {
	private AppLoader mAppLoader;

	@Override
	protected void init() {
		mAppLoader = new AppLoader(getActivity());
	}

	@Override
	protected void onStartLoading(DataLoadListener listener) {

	}

	@Override
	protected void onUpdateData(Message msg) {
	}

	@Override
	protected DataLoadListener initListener() {
		return null;
	}

}
