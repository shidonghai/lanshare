package com.nano.lanshare.main.adapter;

import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
	HashMap<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments.containsKey(position)) {
			return fragments.get(position);
		} else {
			Fragment fragment = FragmentTabsFactory.getInstance().getFragment(
					position);
			fragments.put(position, fragment);
			return fragment;
		}

	}

	@Override
	public int getCount() {
		return 5;
	}

}
