package com.nano.lanshare.friend.ui;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nano.lanshare.friend.ContactInfo;
import com.nano.lanshare.friend.logic.FriendDataManager;
import com.nano.lanshare.friend.logic.FriendSortListData;
import com.nano.lanshare.friend.scroll.IListScroll;
import com.nano.lanshare.friend.scroll.ListScrollWarper;

public class FriendListView extends ListView implements IListScroll,
		OnScrollListener, android.widget.AdapterView.OnItemClickListener {

	private final int MAX_SElECT_SIZE = 4;

	private boolean mIsScrolling;

	private FriendsAdapter mAdapter;

	private FriendSortListData mSortListData;

	private ListScrollWarper mWarper;

	private List<ContactInfo> mSelectedContacts = new ArrayList<ContactInfo>();

	public FriendListView(Context context) {
		super(context);

		setScrollbarFadingEnabled(true);
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
		setCacheColorHint(Color.TRANSPARENT);
		setDividerHeight(0);
		setOnScrollListener(this);
		// setOnItemClickListener(this);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		mAdapter = new FriendsAdapter(context);
		setAdapter(mAdapter);
		mWarper = new ListScrollWarper(context);
	}

	// public FriendListView(Context context, AttributeSet attrs) {
	// super(context, attrs);
	// }

	public void updateList(FriendSortListData sortListData) {
		mSortListData = sortListData;
		mAdapter.updateList(mSortListData.getSectionList());
	}

	@Override
	public void scrolltoIndexByKey(String aKey) {
		if (null != mSortListData) {
			int index = mSortListData.getSectionIndexByName(aKey);

			if (index != -1) {
				setSelection(index);
			}
		}

	}

	public View getScrollFriendListView() {
		// 设置列表项的点击事件观察者
		// 设置好友列表样式
		View view = this;

		if (null != view.getParent()) {
			// 移除View的Parent
			((ViewGroup) view.getParent()).removeView(view);
		}

		if (null != mWarper) {
			mWarper.warpFriendList(this, this);
			view = mWarper;

			if (null != view.getParent()) {
				// 移除View的Parent
				((ViewGroup) view.getParent()).removeView(view);
			}
		}

		return view;
	}

	@Override
	public void scrolltoIndex(int aIndex) {
		setSelection(aIndex);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mIsScrolling) {
			final int index = firstVisibleItem;
			mWarper.showFastKey(getNameFirstChar(mAdapter.getContactList(),
					index));
			// mWarper.showFastKey(String.valueOf(mAdapter.getContactList().get(
			// index).mSortInfo.getFirstChar() - 26));
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		switch (scrollState) {
		case SCROLL_STATE_IDLE: {
			mIsScrolling = false;
			mWarper.hideFastKey();
			break;
		}

		case SCROLL_STATE_TOUCH_SCROLL: {
			mIsScrolling = true;
			break;
		}

		default: {
			break;
		}
		}

	}

	private String getNameFirstChar(List<ContactInfo> aSectionList, int aIndex) {
		if ((null != aSectionList) && (0 <= aIndex)
				&& (aIndex < aSectionList.size())) {
			ContactInfo info = aSectionList.get(aIndex);
			String name = info.mContactName;
			if (null != name) {
				name = name.trim();

				if (name.length() > 0) {
					return String.valueOf(name.charAt(0));
				}
			}
		}

		return null;
	}

	public List<ContactInfo> getContactList() {
		return mAdapter.getContactList();
	}

	public void notifyDataSetChanged() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ContactInfo info = mAdapter.getContactList().get(arg2);
		if (info.mSelected) {
			info.mSelected = false;
			mSelectedContacts.remove(info);
		} else if (mSelectedContacts.size() < MAX_SElECT_SIZE) {
			info.mSelected = true;
			mSelectedContacts.add(info);
		}

		// mAdapter.getContactList().get(arg2).mSelected = !mAdapter
		// .getContactList().get(arg2).mSelected;
		mAdapter.notifyDataSetChanged();
	}
}
