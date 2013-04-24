
package com.nano.lanshare.conn.logic;

import java.util.ArrayList;
import java.util.List;

import com.nano.lanshare.socket.moudle.Stranger;

public class UserManager {
    private static UserManager mUserManager = new UserManager();
    private List<Stranger> mUserList = new ArrayList<Stranger>();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return mUserManager;
    }

    public void addUser(Stranger stranger) {
        synchronized (mUserList) {
            if (!mUserList.contains(stranger)) {
                mUserList.add(stranger);
            }
        }
    }

    public void removerUser(Stranger stranger) {
        synchronized (mUserList) {
            if (mUserList.contains(stranger)) {
                mUserList.remove(stranger);
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
}
