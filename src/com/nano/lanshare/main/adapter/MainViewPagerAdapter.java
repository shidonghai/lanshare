package com.nano.lanshare.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nano.lanshare.apps.ui.AppFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int position) {
		return FragmentTabsFactory.getInstance().getFragment(position);
	}

	@Override
	public int getCount() {
		return 2;
	}

}
