package com.nano.lanshare.audio.ui;

import android.os.Message;
import android.widget.LinearLayout;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;

public class AudioBase extends BasicTabFragment {

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		LinearLayout leftLayout = new LinearLayout(getActivity());
		leftLayout.setId(R.id.music_left_tab);
		getGroup(LEFT).addView(leftLayout);

		LinearLayout rightLayout = new LinearLayout(getActivity());
		rightLayout.setId(R.id.music_right_tab);
		getGroup(RIGHT).addView(rightLayout);

		getFragmentManager().beginTransaction()
				.add(R.id.music_left_tab, new MusicTabFragment()).commit();
		getFragmentManager().beginTransaction()
				.add(R.id.music_right_tab, new VideoListFragment()).commit();
	}

}
