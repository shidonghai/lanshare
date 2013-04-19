package com.nano.lanshare.file.ui;

import android.os.Message;
import android.widget.LinearLayout;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;

public class FileTabFragment extends BasicTabFragment {

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		LinearLayout leftLayout = new LinearLayout(getActivity());
		leftLayout.setId(R.id.file_left_tab);
		getGroup(LEFT).addView(leftLayout);
		getFragmentManager().beginTransaction()
				.add(R.id.file_left_tab, new InboxFileFragment()).commit();

		LinearLayout rightLayout = new LinearLayout(getActivity());
		rightLayout.setId(R.id.file_right_tab);
		getGroup(RIGHT).addView(rightLayout);
		getFragmentManager().beginTransaction()
				.add(R.id.file_right_tab, new FileListFragment()).commit();
	}

}
