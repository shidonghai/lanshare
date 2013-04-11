
package com.nano.lanshare.main.adapter;

import com.nano.lanshare.history.fragment.HistoryFragment;

import android.support.v4.app.Fragment;

public class FragmentTabsFactory {

    public static final int TABS_HISTORY = 4;

    private static FragmentTabsFactory factory = new FragmentTabsFactory();

    private FragmentTabsFactory() {
    }

    public static FragmentTabsFactory getInstance() {
        return factory;
    }

    public Fragment getFragment(int tabId) {
        return new HistoryFragment();
        /*
         * switch (tabId) { case TABS_HISTORY: return new HistoryFragment();
         * default: return null; }
         */
    }

}
