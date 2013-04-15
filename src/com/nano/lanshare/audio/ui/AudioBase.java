package com.nano.lanshare.audio.ui;

import android.os.Message;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;

public class AudioBase extends BasicTabFragment {

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		getFragmentManager().beginTransaction()
				.add(R.id.left_container, new MusicTabFragment()).commit();
		getFragmentManager().beginTransaction()
				.add(R.id.right_container, new VideoListFragment()).commit();
	}

}
