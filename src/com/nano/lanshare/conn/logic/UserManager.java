package com.nano.lanshare.conn.logic;

import java.util.ArrayList;
import java.util.List;

import com.nano.lanshare.socket.moudle.Stranger;

public class UserManager {
	private static UserManager mUserManager = new UserManager();
	private ArrayList<Stranger> mUserList = new ArrayList<Stranger>();
	private ArrayList<UserChangedListener> mListeners = new ArrayList<UserChangedListener>();

	private UserManager() {
	}

	public static UserManager getInstance() {
		return mUserManager;
	}

	public boolean updateUserStatus(Stranger stranger) {
		// TODO
		for (UserChangedListener l : mListeners) {
			l.notifyUserChanged(mUserList);
		}
		return false;
	}

	public boolean addUser(Stranger stranger) {
		synchronized (mUserList) {
			if (!mUserList.contains(stranger)) {
				mUserList.add(stranger);
				for (UserChangedListener l : mListeners) {
					l.notifyUserChanged(mUserList);
				}
				return true;
			}
			return false;
		}
	}

	public void removerUser(Stranger stranger) {
		synchronized (mUserList) {
			if (mUserList.contains(stranger)) {
				mUserList.remove(stranger);

				for (UserChangedListener l : mListeners) {
					l.notifyUserChanged(mUserList);
				}
			}
		}
	}

	public void clearUser() {
		synchronized (mUserList) {
			mUserList.clear();
		}
	}

	public List<Stranger> getUserList() {
		return mUserList;
	}

	public void registerCallback(UserChangedListener listener) {
		if (!mListeners.contains(listener)) {
			mListeners.add(listener);
			listener.notifyUserChanged(mUserList);
		}
	}

	public void unregisterCallback(UserChangedListener listener) {
		if (mListeners.contains(listener)) {
			mListeners.remove(listener);
		}
	}
}
