package com.nano.lanshare.file.ui;

import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;

public class FileTabFragment extends BasicTabFragment {

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setTitle(msg.arg1, (String) msg.obj);
		};
	};

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		setTitle(LEFT, String.format(getString(R.string.dm_tab_title_zapya), 0));
		setTitle(RIGHT,
				String.format(getString(R.string.dm_tab_title_sdcard), 0));

		LinearLayout leftLayout = new LinearLayout(getActivity());
		leftLayout.setId(R.id.file_left_tab);
		getGroup(LEFT).addView(leftLayout);
		getFragmentManager().beginTransaction()
				.add(R.id.file_left_tab, new InboxFileFragment(mHandler))
				.commit();

		LinearLayout rightLayout = new LinearLayout(getActivity());
		rightLayout.setId(R.id.file_right_tab);
		getGroup(RIGHT).addView(rightLayout);
		getFragmentManager().beginTransaction()
				.add(R.id.file_right_tab, new FileListFragment(mHandler))
				.commit();
	}
}
