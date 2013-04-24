package com.nano.lanshare.friend;

import com.nano.lanshare.friend.logic.SortInfo;

public class ContactInfo {
	/**
	 * 好友在本地数据库里的ID
	 */
	public long mLocalID = -1;

	/**
	 * 联系人的名字
	 */
	public String mContactName;

	/**
	 * 联系人的号码，只是用来展示用的
	 */
	public String mPhone;

	/**
	 * 用来排序的对象
	 */
	public SortInfo mSortInfo;

	/**
	 * 如果当前是联系人界面，则表示该联系人是否被选中
	 */
	public boolean mSelected;
}
