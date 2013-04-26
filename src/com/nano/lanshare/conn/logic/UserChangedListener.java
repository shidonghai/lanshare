package com.nano.lanshare.conn.logic;

import java.util.ArrayList;

import com.nano.lanshare.socket.moudle.Stranger;

public interface UserChangedListener {
	public void notifyUserChanged(ArrayList<Stranger> list);
}
