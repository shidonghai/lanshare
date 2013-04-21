package com.nano.lanshare.main.adapter;

import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
	private HashMap<Integer, Fragment> fragments;

	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new HashMap<Integer, Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments.containsKey(fragments)) {
			return fragments.get(position);
		}
		Fragment fragment = FragmentTabsFactory.getInstance().getFragment(
				position);
		fragments.put(position, fragment);
		return fragment;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}
}