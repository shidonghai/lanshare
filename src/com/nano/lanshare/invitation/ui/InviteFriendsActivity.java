package com.nano.lanshare.invitation.ui;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.friend.ContactInfo;
import com.nano.lanshare.friend.logic.FriendDataManager;
import com.nano.lanshare.friend.logic.FriendSortListData;
import com.nano.lanshare.friend.ui.FriendListView;
import com.nano.lanshare.utils.RotateView;

public class InviteFriendsActivity extends FragmentActivity implements
		OnClickListener, OnItemClickListener {

	private final int MAX_SElECT_SIZE = 4;

	private Button mBack;

	private FriendListView mFriendListView;

	private RotateView mRotateView;

	private List<ContactInfo> mSelectedContacts = new ArrayList<ContactInfo>(4);

	private SelectedLayout[] mSelectedLayouts = new SelectedLayout[MAX_SElECT_SIZE];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_friends);
		initView();

		new QueryContactTask().execute(this);
	}

	private void initView() {
		mRotateView = (RotateView) findViewById(R.id.rotate_view);
		mRotateView.start();
		mBack = (Button) findViewById(R.id.invite_back);
		mBack.setOnClickListener(this);

		LinearLayout layout = (LinearLayout) findViewById(R.id.content);
		mFriendListView = new FriendListView(this);
		mFriendListView.setOnItemClickListener(this);
		layout.addView(mFriendListView.getScrollFriendListView());

		initSelectedLayout();
	}

	private void initSelectedLayout() {
		mSelectedLayouts[0] = new SelectedLayout(
				(ImageView) findViewById(R.id.select_info_avatar_1),
				(TextView) findViewById(R.id.select_info_name_1));
		mSelectedLayouts[1] = new SelectedLayout(
				(ImageView) findViewById(R.id.select_info_avatar_2),
				(TextView) findViewById(R.id.select_info_name_2));
		mSelectedLayouts[2] = new SelectedLayout(
				(ImageView) findViewById(R.id.select_info_avatar_3),
				(TextView) findViewById(R.id.select_info_name_3));
		mSelectedLayouts[3] = new SelectedLayout(
				(ImageView) findViewById(R.id.select_info_avatar_4),
				(TextView) findViewById(R.id.select_info_name_4));
	}

	private void updateSelectedLayout() {
		for (int i = 0; i < mSelectedContacts.size(); i++) {
			ContactInfo info = mSelectedContacts.get(i);
			Log.d("zxh", "info:" + info.mContactName);
			mSelectedLayouts[i].name.setText(info.mContactName);
		}

		// for (int i = 0; i < mSelectedLayouts.length; i++) {
		// ContactInfo info = mSelectedContacts.get(i);
		// mSelectedLayouts[i].name.setText(info == null ? ""
		// : info.mContactName);
		// }
		int length = mSelectedLayouts.length;
		int size = mSelectedContacts.size();
		for (int i = length - 1; i > size - 1; i--) {
			mSelectedLayouts[i].name.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBack) {
			finish();
		}
	}

	private class QueryContactTask extends
			AsyncTask<Context, Void, FriendSortListData> {

		@Override
		protected FriendSortListData doInBackground(Context... params) {
			return FriendDataManager.getFriendDataManager().queryLocalContacts(
					params[0]);
		}

		@Override
		protected void onPostExecute(FriendSortListData result) {
			mFriendListView.updateList(result);
			mRotateView.stop();
			mRotateView.setVisibility(View.GONE);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ContactInfo info = mFriendListView.getContactList().get(arg2);
		if (info.mSelected) {
			info.mSelected = false;
			mSelectedContacts.remove(info);
		} else if (mSelectedContacts.size() < MAX_SElECT_SIZE) {
			info.mSelected = true;
			mSelectedContacts.add(info);
		}

		// mAdapter.getContactList().get(arg2).mSelected = !mAdapter
		// .getContactList().get(arg2).mSelected;
		mFriendListView.notifyDataSetChanged();
		updateSelectedLayout();
	}

	private class SelectedLayout {

		SelectedLayout(ImageView aAvatar, TextView aName) {
			avatar = aAvatar;
			name = aName;
		}

		ImageView avatar;

		TextView name;
	}
}
