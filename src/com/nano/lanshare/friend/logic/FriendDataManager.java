package com.nano.lanshare.friend.logic;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;

import com.nano.lanshare.friend.Constant;
import com.nano.lanshare.friend.ContactInfo;

public class FriendDataManager {

	private Map<String, SoftReference<SortInfo>> mSortInfoMap = new HashMap<String, SoftReference<SortInfo>>();

	private Map<String, SoftReference<String>> mPinyinMap = new HashMap<String, SoftReference<String>>();
	private Map<String, List<String>> idPhones;
	private List<ContactInfo> mLocalContacts = new LinkedList<ContactInfo>();
	Map<String, String> idName;
	private FriendSortListData mSortListData;
	private static FriendDataManager mInstance;

	private FriendDataManager() {

	}

	public static FriendDataManager getFriendDataManager() {
		if (null == mInstance) {
			mInstance = new FriendDataManager();
		}
		return mInstance;
	}

	public FriendSortListData queryLocalContacts(Context context) {
		readPhones(context);

		List<Long> ids = readContacts(context);

		transferLocalContact(context, ids);

		return sortAllFriends();
	}

	private void readPhones(Context context) {
		// 保存号码和ID的对应关系
		Map<String, Long> phoneId = new HashMap<String, Long>();
		// 保存ID对应的号码列表
		idPhones = new HashMap<String, List<String>>();
		// 保存ID和MOBILE类型号码的关系，只是合并联系人模块使用
		Map<String, String> idMobile = new HashMap<String, String>();

		// 查询电话号码
		Cursor cursor = context.getContentResolver().query(
				CommonDataKinds.Phone.CONTENT_URI,
				new String[] { CommonDataKinds.Phone.CONTACT_ID,
						CommonDataKinds.Phone.NUMBER,
						CommonDataKinds.Phone.TYPE }, null, null,
				" is_primary desc");

		if ((null != cursor) && (cursor.moveToFirst())) {
			try {
				do {
					// 本地的ID
					final long id = cursor.getLong(cursor
							.getColumnIndex(CommonDataKinds.Phone.CONTACT_ID));
					// 电话号码
					final String phoneNumber = cursor.getString(cursor
							.getColumnIndex(CommonDataKinds.Phone.NUMBER));
					// 类型
					final int type = cursor.getInt(cursor
							.getColumnIndex(CommonDataKinds.Phone.TYPE));

					if (!TextUtils.isEmpty(phoneNumber)) {
						// 保存号码和本地ID的对应关系，用于根据号码查找到本地的ID
						phoneId.put(phoneNumber, Long.valueOf(id));

						// 缓存ID和号码的关系，用于根据ID获取号码列表
						String key = String.valueOf(id);
						List<String> phones = idPhones.get(key);
						if (null == phones) {
							phones = new ArrayList<String>();
							idPhones.put(key, phones);
						}
						phones.add(phoneNumber);

						// 缓存ID和MOBILE类型号码的关系，用于合并联系人
						if (ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE == type) {
							idMobile.put(key, phoneNumber);
						}
					}
				} while (cursor.moveToNext());
			} finally {
				cursor.close();
			}
		}

		// // 保存到缓存里
		// IFriendShare share = LogicFacade.getFriendShare();
		// share.cachePhoneId(phoneId);
		// share.cacheIdPhones(idPhones);
		// share.cacheIdMobile(idMobile);
	}

	private List<Long> readContacts(Context context) {
		// 用来缓存ID和名字的关系
		idName = new HashMap<String, String>();
		// 用来缓存ID和PHOTO ID的关系
		List<Long> photos = new ArrayList<Long>();
		// 用来缓存ID和收藏的关系
		Map<String, Boolean> idStar = new HashMap<String, Boolean>();
		// ID集合
		List<Long> ids = new ArrayList<Long>();

		// 查询所有的联系人
		Cursor cursor = context.getContentResolver()
				.query(Contacts.CONTENT_URI,
						new String[] { Contacts.DISPLAY_NAME, Contacts._ID,
								Contacts.STARRED, Contacts.PHOTO_ID }, null,
						null, null);

		if ((null != cursor) && (cursor.moveToFirst())) {
			try {
				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(Contacts._ID));
					final String key = String.valueOf(id);
					final String name = cursor.getString(cursor
							.getColumnIndex(Contacts.DISPLAY_NAME));
					final boolean star = cursor.getShort(cursor
							.getColumnIndex(Contacts.STARRED)) == 1;
					final long photoId = cursor.getLong(cursor
							.getColumnIndex(Contacts.PHOTO_ID));

					idName.put(key, name);
					idStar.put(key, Boolean.valueOf(star));
					ids.add(Long.valueOf(id));

					if (photoId != 0) {
						photos.add(Long.valueOf(id));
					}

				} while (cursor.moveToNext());
			} finally {
				cursor.close();
			}
		}

		// 保存到缓存里
		// IFriendShare share = LogicFacade.getFriendShare();
		// share.cacheIdDisplayName(idName);
		// share.cacheIdStar(idStar);
		// share.cachePhotoIds(photos);

		return ids;
	}

	void transferLocalContact(Context context, List<Long> aIds) {
		if (null != aIds) {
			List<ContactInfo> localContacts = new LinkedList<ContactInfo>();

			for (Long id : aIds) {
				final long idValue = id.longValue();
				if (-1 != idValue) {
					List<String> phones = idPhones.get(String.valueOf(idValue));
					if (phones == null || phones.size() < 0) {
						continue;
					}
					ContactInfo contact = new ContactInfo();

					// 本地数据库中的id
					contact.mLocalID = idValue;

					// 获取电话号码列表
					contact.mPhone = phones.get(0);

					// 名字
					contact.mContactName = idName.get(String.valueOf(idValue));

					if (TextUtils.isEmpty(contact.mContactName)) {
						// 一般是有名字的，没有就用未知标记
						contact.mContactName = "unknow";
					}

					// 排序关键字
					contact.mSortInfo = getSortInfo(context,
							contact.mContactName);

					localContacts.add(contact);
				}
			}

			synchronized (this) {
				mLocalContacts = localContacts;
			}
		}
	}

	public SortInfo getSortInfo(Context context, String aFriendName) {
		SortInfo sortInfo = null;

		SoftReference<SortInfo> obj = mSortInfoMap.get(aFriendName);
		if (null != obj) {
			sortInfo = obj.get();
		}

		if (null == sortInfo) {
			sortInfo = new SortInfo();

			// 拼音数组
			sortInfo.setSpellNames(PinYin.getInstance(context)
					.getFriendPinYinStringArray(aFriendName));

			mSortInfoMap
					.put(aFriendName, new SoftReference<SortInfo>(sortInfo));
		}

		return sortInfo;
	}

	public FriendSortListData sortAllFriends() {
		FriendSortListData result = new FriendSortListData();

		List<ContactInfo> tempList = new LinkedList<ContactInfo>();

		// 添加本地联系人
		for (ContactInfo info : mLocalContacts) {
			tempList.add(info);
		}

		List<ContactInfo> sectionList = new LinkedList<ContactInfo>();

		// 获取总的数目
		result.setHintCount(tempList.size());

		Map<String, Integer> sectionMap = new HashMap<String, Integer>();
		sortFriendsBySection(tempList, new FriendCompator(), sectionList,
				sectionMap);

		// 保存到结果里
		result.setSectionList(sectionList);
		result.setSectionMap(sectionMap);

		return result;
	}

	public static void sortFriendsBySection(List<ContactInfo> aTempFriendList,
			Comparator<ContactInfo> aCompator, List<ContactInfo> aSectionList,
			Map<String, Integer> aSectionMap) {
		if (aTempFriendList.isEmpty() ^ true) {
			// 先进行一下排序
			Collections.sort(aTempFriendList, aCompator);

			// 添加排序的联系人
			char sortKey = 0;
			for (ContactInfo info : aTempFriendList) {
				char currentKey = info.mSortInfo.getFirstChar();
				if (sortKey != currentKey) {
					sortKey = currentKey;

					// 分隔符名字用索引的大写
					String sectionName = String.valueOf(sortKey).toUpperCase();

					if (Constant.SORTKEY_INVALID == sortKey) {
						// 无效的排序索引转换成需要显示的#
						sectionName = Constant.INVALID_NAME;
					}

					// 把对应的Section的名字和索引关系保存一下
					aSectionMap.put(sectionName, aSectionList.size());
				}

				aSectionList.add(info);
			}

			// 数据用完了，清除掉
			aTempFriendList.clear();
		}
	}

	public String findPinyinInCache(String aKey) {
		SoftReference<String> obj = mPinyinMap.get(aKey);
		return (null == obj) ? null : obj.get();
	}

	public void cachePinyin(String aKey, String aValue) {
		mPinyinMap.put(aKey, new SoftReference<String>(aValue));
	}

	private static class FriendCompator implements Comparator<ContactInfo> {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(ContactInfo object1, ContactInfo object2) {
			if (null == object1) {
				return -1;
			}

			if (null == object2) {
				return 1;
			}

			return object1.mSortInfo.compareTo(object2.mSortInfo);
		}
	}
}
