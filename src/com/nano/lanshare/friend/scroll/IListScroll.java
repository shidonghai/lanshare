package com.nano.lanshare.friend.scroll;

/**
 * 快速滚动的接口
 * <p>
 */
public interface IListScroll {
	/**
	 * 根据索引，跳转到对应的位置
	 *
	 * @param aKey
	 *            索引
	 */
	void scrolltoIndexByKey(String aKey);

	/**
	 * 滚动到aIndex对应的位置
	 *
	 * @param aIndex
	 *            位置
	 */
	void scrolltoIndex(int aIndex);
}
