/**
 * 文件名称 : SortInfo.java
 * <p>
 * 作者信息 : Yang Ke
 * <p>
 * 创建时间 : Feb 17, 2011, 2:07:07 PM
 * <p>
 * 版权声明 : Copyright (c) 2009-2012 CIeNET Ltd. All rights reserved
 * <p>
 * 评审记录 :
 * <p>
 */

package com.nano.lanshare.friend.logic;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.nano.lanshare.friend.Constant;

/**
 * 用于排序的对象
 * <p>
 */
public final class SortInfo implements Comparable<SortInfo> {
	/**
	 * 名字的汉字拼音数组，用于搜索的时候匹配
	 */
	private String[] mSpellNames;

	/**
	 * 名字的汉语拼音
	 */
	private String mSpellName = "";

	/**
	 * 高亮索引
	 */
	private List<Integer> mMatchIndex = new ArrayList<Integer>();

	/**
	 * 获取名字的第一个字母，用于fast scroll
	 * 
	 * @return 名字的第一个字母
	 */
	public char getFirstChar() {
		return (TextUtils.isEmpty(mSpellName)) ? Constant.SORTKEY_INVALID
				: mSpellName.charAt(0);
	}

	/**
	 * 设置拼音名字
	 * 
	 * @param aSpellName
	 *            拼音名字
	 */
	public void setSpellName(String aSpellName) {
		mSpellName = aSpellName;
	}

	/**
	 * 设置名字拼音数组
	 * 
	 * @param aSpellNames
	 *            名字拼音数组
	 */
	public void setSpellNames(String[] aSpellNames) {
		// 拼音数组
		mSpellNames = aSpellNames;

		if (null != aSpellNames && aSpellNames.length > 0) {
			// 先分析第一个
			String value = aSpellNames[0];
			if (TextUtils.isEmpty(value)) {
				// 如果是空的，就增加一个#
				mSpellName += Constant.SORTKEY_INVALID;
			} else {
				// 如果不是空的，就取第一个字母分析
				char first = value.charAt(0);
				if (first < 'a' || first > 'z') {
					// 如果在a-z范围外，就增加一个排序标记
					mSpellName += Constant.SORTKEY_INVALID;
				}

				mSpellName += value;
			}

			// 继续添加后面的拼音
			for (int i = 1; i < aSpellNames.length; i++) {
				if (null != aSpellNames[i]) {
					mSpellName += aSpellNames[i];
				}
			}
		} else {
			// 增加一个#
			mSpellName += Constant.SORTKEY_INVALID;
		}
	}

	/**
	 * 名字里是否含有搜索关键字
	 * 
	 * @param aSearchKey
	 *            搜索关键字
	 * @return 是否含有搜索关键字
	 */
	public boolean containsSearchKey(String aSearchKey) {
		clearMatchIndex();

		if (null != mSpellNames && null != aSearchKey) {
			if (aSearchKey.length() > mSpellName.length()) {
				// 如果搜索的关键字的长度大于名字的整体长度，不能匹配
				return false;
			}

			for (int i = 0; i < mSpellNames.length; i++) {
				String value = mSpellNames[i];
				if (!TextUtils.isEmpty(value) && value.startsWith(aSearchKey)) {
					// 拼音完全匹配了某个字，例如搜索的是yan
					mMatchIndex.add(i);
				}
			}

			if (mMatchIndex.size() > 0) {
				// 如果能匹配到某个字的开始部分，就返回了
				return true;
			}

			// 匹配首字母
			String firstLetters = "";
			for (int i = 0; i < mSpellNames.length; i++) {
				String value = mSpellNames[i];
				if (!TextUtils.isEmpty(value)) {
					// 如果这个拼音不是空的，把首字母加起来
					firstLetters += value.charAt(0);
				}
			}

			if (!TextUtils.isEmpty(firstLetters)) {
				int index = firstLetters.indexOf(aSearchKey);
				if (index != -1) {
					for (int i = 0; i < aSearchKey.length(); i++) {
						mMatchIndex.add(index + i);
					}

					return true;
				}
			}

			// 更为复杂的检索
			for (int i = 0; i < mSpellNames.length; i++) {
				// 向前检查
				List<Integer> list = forwardCheck(i, aSearchKey);
				if (list.size() > 0) {
					mMatchIndex.addAll(list);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 向前检查
	 * 
	 * @param aIndex
	 *            当前位置
	 * @param aSearchKey
	 *            搜索关键字
	 * @return 匹配的数组
	 */
	private List<Integer> forwardCheck(int aIndex, String aSearchKey) {
		List<Integer> list = new ArrayList<Integer>();

		do {
			// 当前位置的拼音
			String value = mSpellNames[aIndex];
			if (!TextUtils.isEmpty(value)) {
				if (value.trim().length() == 0) {
					// 如果是空字符串，跳过
					aIndex++;
				} else {
					// 如果不是空字符串，检测
					if (value.startsWith(aSearchKey)) {
						// 这个字以搜索的关键字开头，表示直接匹配上了
						list.add(aIndex);
						break;
					} else if (aSearchKey.startsWith(value)) {
						// 搜索的关键字以这个字的拼音开头
						list.add(aIndex++);
						// 搜索关键字需要重新截取
						aSearchKey = aSearchKey.substring(value.length());
					} else if (aSearchKey.startsWith(value.substring(0, 1))) {
						// 搜索的关键字以这个字的拼音首字母开头
						list.add(aIndex++);
						// 搜索关键字需要重新截取首字母以后的
						aSearchKey = aSearchKey.substring(1);
					} else {
						// 不能匹配
						list.clear();
						break;
					}
				}

				if ((aSearchKey.length() > 0) && (aIndex == mSpellNames.length)) {
					// 超出范围了，不能匹配
					list.clear();
					break;
				}
			} else {
				// 当前位置的拼音是空的，不能匹配，直接返回
				list.clear();
				break;
			}

		} while (aIndex < mSpellNames.length);

		return list;
	}

	/**
	 * 获取高亮索引
	 * 
	 * @return 高亮索引
	 */
	public List<Integer> getMatchIndex() {
		return mMatchIndex;
	}

	/**
	 * 清理搜索的高亮索引
	 */
	public void clearMatchIndex() {
		mMatchIndex.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SortInfo another) {
		return mSpellName.compareToIgnoreCase(another.mSpellName);
	}
}
