package com.nano.lanshare.main.adapter;

import android.support.v4.app.Fragment;

import com.nano.lanshare.apps.ui.AppFragment;
import com.nano.lanshare.history.fragment.HistoryFragment;
import com.nano.lanshare.pics.ui.PicFragment;

public class FragmentTabsFactory {
	public static final int TABS_APP = 0;
	public static final int TABS_PIC = 1;
	public static final int TABS_HISTORY = 4;

	private static FragmentTabsFactory factory = new FragmentTabsFactory();

	private FragmentTabsFactory() {
	}

	public static FragmentTabsFactory getInstance() {
		return factory;
	}

	public Fragment getFragment(int tabId) {
		switch (tabId) {
		case TABS_APP:
			return new AppFragment();
		case TABS_PIC:
			return new PicFragment();
		}
		return new HistoryFragment();
		/*
		 * switch (tabId) { case TABS_HISTORY: return new HistoryFragment();
		 * default: return null; }
		 */
	}
}
