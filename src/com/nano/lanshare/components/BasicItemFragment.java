package com.nano.lanshare.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nano.lanshare.R;

public abstract class BasicItemFragment extends Fragment {
	protected ProgressBar mProgress;

	protected TextView mEmptyView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_item, null);

		mEmptyView = (TextView) view.findViewById(R.id.empty);
		mProgress = (ProgressBar) view.findViewById(R.id.progress);

		FrameLayout contentLayout = (FrameLayout) view
				.findViewById(R.id.content_view);
		contentLayout.addView(createContentView(inflater));

		return view;
	}

	public abstract View createContentView(LayoutInflater inflater);
}
