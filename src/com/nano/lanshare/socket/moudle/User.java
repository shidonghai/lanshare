
package com.nano.lanshare.socket.moudle;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public static int USER_TYPE_STRANGER = 0;
    public static int USER_TYPE_FRIEND = 1;
    public static int USER_TYPE_BLACKER = 2;

    public static int USER_STATUS_ONLINE = 0;
    public static int USER_STATUS_OFFLINE = 1;
    public static int USER_STATUS_HIDE = 2;

    // user status
    protected int mStatus;

    // has new message come from this user
    protected boolean mHasNewMessage;

    // user type
    protected int mType = 0;

    // nicky name of user
    protected String mUserName = null;

    // unique identifier of a user, like MAC address
    protected String mUserIdentifier = null;

    // ip address of this user
    protected String mUserIp = null;

    // user photo data
    protected byte[] mUserPhoto = null;

    public void setHasNewMessage(boolean hasNewMessage) {
        mHasNewMessage = hasNewMessage;
    }

    public boolean hasNewMessage() {
        return mHasNewMessage;
    }

    public int getUserStatus() {
        return mStatus;
    }

    public void setUserStatus(int status) {
        mStatus = status;
    }

    public void setName(String name) {
        mUserName = name;
    }

    public String getName() {
        return mUserName;
    }

    public void setUserIdentifier(String identifier) {
        mUserIdentifier = identifier;
    }

    public String getUserIdentifier() {
        return mUserIdentifier;
    }

    public void setUserIp(String ip) {
        mUserIp = ip;
    }

    public String getUserIp() {
        return mUserIp;
    }

    public void setUserPhoto(byte[] photo) {
        mUserPhoto = photo;
    }

    public byte[] getUserPhoto() {
        return mUserPhoto;
    }

    @Override
    public boolean equals(Object other) {
        boolean flag = other instanceof User;
        if (flag) {
            User user = (User) other;
            return mUserIp.equals(user.mUserIp) && mUserIdentifier.equals(user.mUserIdentifier);
        } else {
            return flag;
        }
    }

    abstract public User copyFromOther(User other);
}
