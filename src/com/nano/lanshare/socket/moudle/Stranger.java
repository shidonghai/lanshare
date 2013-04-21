package com.nano.lanshare.socket.moudle;

public class Stranger extends User
{

    public Stranger()
    {
        mType = USER_TYPE_STRANGER;
    }

    @Override
    public User copyFromOther(User other)
    {
        mUserName = other.mUserName;
        mUserIp = other.mUserIp;
        mUserIdentifier = other.mUserIdentifier;
        mUserPhoto = other.mUserPhoto;

        return this;
    }

}
