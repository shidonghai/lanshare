package com.nano.lanshare.friend.logic;

import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.nano.lanshare.friend.ContactInfo;

/**
 * 好友的排序列表数据对象，封装列表，HintCount，Section集合等
 * <p>
 */
public final class FriendSortListData {
	/**
	 * 联系人的数目
	 */
	private int mHintCount;

	/**
	 * Section的集合，对应了选择联系人关键字和索引位置
	 */
	private Map<String, Integer> mSectionMap;

	/**
	 * Section以后的联系人列表
	 */
	private List<ContactInfo> mSectionList;

	/**
	 * 设置联系人数目
	 * 
	 * @param aHintCount
	 *            联系人的数目
	 */
	public void setHintCount(int aHintCount) {
		mHintCount = aHintCount;
	}

	/**
	 * 获取联系人的数目
	 * 
	 * @return 联系人的数目
	 */
	public int getHintCount() {
		return mHintCount;
	}

	/**
	 * 设置Section的集合
	 * 
	 * @param aSectionMap
	 *            Section的集合
	 */
	public void setSectionMap(Map<String, Integer> aSectionMap) {
		mSectionMap = aSectionMap;
	}

	/**
	 * 获取Section的集合
	 * 
	 * @return Section的集合
	 */
	public Map<String, Integer> getSectionMap() {
		return mSectionMap;
	}

	/**
	 * 根据Section的名字获取Section索引
	 * 
	 * @param aSectionName
	 *            Section的名字
	 * @return Section索引
	 */
	public int getSectionIndexByName(String aSectionName) {
		int index = -1;

		if (null == mSectionMap || TextUtils.isEmpty(aSectionName)) {
			return index;
		}

		Integer value = mSectionMap.get(aSectionName);
		if (null != value) {
			index = value.intValue();
		}

		return index;
	}

	/**
	 * 设置Section以后的联系人列表
	 * 
	 * @param aSectionList
	 *            Section以后的联系人列表
	 */
	public void setSectionList(List<ContactInfo> aSectionList) {
		mSectionList = aSectionList;
	}

	/**
	 * 获取Section以后的联系人列表
	 * 
	 * @return Section以后的联系人列表
	 */
	public List<ContactInfo> getSectionList() {
		return mSectionList;
	}

	/**
	 * 销毁释放内存
	 */
	public void destroy() {
		if (null != mSectionMap) {
			mSectionMap.clear();
		}

		if (null != mSectionList) {
			mSectionList.clear();
		}
	}
}
