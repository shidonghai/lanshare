package com.nano.lanshare.socket.moudle;

public class Stranger extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6810507648290615546L;

	public Stranger() {
		mType = USER_TYPE_STRANGER;
	}

	@Override
	public User copyFromOther(User other) {
		mUserName = other.mUserName;
		mUserIp = other.mUserIp;
		mUserIdentifier = other.mUserIdentifier;
		mUserPhoto = other.mUserPhoto;

		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (Stranger.class.isInstance(o)) {
			Stranger stranger = (Stranger) o;
			return stranger.getUserIdentifier()
					.equals(this.getUserIdentifier());
		}
		return false;
	}

}
