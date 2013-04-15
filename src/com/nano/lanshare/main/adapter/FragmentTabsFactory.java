package com.nano.lanshare.main.adapter;

import android.support.v4.app.Fragment;

import com.nano.lanshare.apps.ui.AppFragment;
import com.nano.lanshare.audio.ui.AudioBase;
import com.nano.lanshare.file.ui.FileListFragment;
import com.nano.lanshare.history.fragment.HistoryFragment;

public class FragmentTabsFactory {

	public static final int TABS_HISTORY = 4;

	private static FragmentTabsFactory factory = new FragmentTabsFactory();

	private FragmentTabsFactory() {
	}

	public static FragmentTabsFactory getInstance() {
		return factory;
	}

    public Fragment getFragment(int tabId) {
        switch (tabId) {
        case 0:
           return new AppFragment();
        case 2:
           return new AudioBase();
        case 3:
           return new FileListFragment();
        }

        return new HistoryFragment();
        /*
         * switch (tabId) { case TABS_HISTORY: return new HistoryFragment();
         * default: return null; }
         */
    }
}
