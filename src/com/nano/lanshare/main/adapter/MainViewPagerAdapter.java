package com.nano.lanshare.main.adapter;

import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
	// private HashMap<Integer, Fragment> fragments;

	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// fragments = new HashMap<Integer, Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = FragmentTabsFactory.getInstance().getFragment(
				position);
		return fragment;
	}

	@Override
	public int getCount() {
		return 5;
	}

}