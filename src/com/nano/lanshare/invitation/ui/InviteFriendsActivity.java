package com.nano.lanshare.invitation.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nano.lanshare.R;
import com.nano.lanshare.friend.ui.FriendListView;

public class InviteFriendsActivity extends FragmentActivity implements
		OnClickListener {
	private Button mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_friends);
		initView();
	}

	private void initView() {
		mBack = (Button) findViewById(R.id.invite_back);
		mBack.setOnClickListener(this);

		LinearLayout layout = (LinearLayout) findViewById(R.id.content);
		FriendListView friendListView = new FriendListView(this);
		layout.addView(friendListView.getScrollFriendListView());
	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			finish();
		}
	}
}
