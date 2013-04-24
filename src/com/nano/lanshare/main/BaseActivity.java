package com.nano.lanshare.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.audio.logic.MusicManger;
import com.nano.lanshare.common.DragController;
import com.nano.lanshare.conn.ui.ConnectActivity;
import com.nano.lanshare.invitation.ui.InviteFriendsActivity;
import com.nano.lanshare.main.adapter.MainViewPagerAdapter;
import com.nano.lanshare.main.ui.ExitDialog;

public class BaseActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	private ViewPager mViewPager;
	private List<TextView> mTabs = new ArrayList<TextView>();
	private MainViewPagerAdapter mPagerAdapter;
	private TextView mAppTab;
	private TextView mPhotoTab;
	private TextView mMediaTab;
	private TextView mFileTab;
	private TextView mHistoryTab;

	private View mSelected;
	private ImageView mTabIndexMarker;

	private View mConnectFriends;
	private View mInviteFriends;

	private DragController mDragController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_base);
		initView();
		mDragController = DragController
				.getInstance((ViewGroup) findViewById(R.id.base_viewgroup));

		MusicManger.getInstance().bindServer(this);
	}

	/**
	 * 初始化，包括添加view到viewpager中
	 */
	private void initView() {
		mAppTab = (TextView) findViewById(R.id.leftTab1);
		mAppTab.setOnClickListener(this);
		mTabs.add(mAppTab);

		mPhotoTab = (TextView) findViewById(R.id.leftTab2);
		mPhotoTab.setOnClickListener(this);
		mTabs.add(mPhotoTab);

		mMediaTab = (TextView) findViewById(R.id.leftTab3);
		mMediaTab.setOnClickListener(this);
		mTabs.add(mMediaTab);

		mFileTab = (TextView) findViewById(R.id.leftTab4);
		mFileTab.setOnClickListener(this);
		mTabs.add(mFileTab);

		mHistoryTab = (TextView) findViewById(R.id.leftTab5);
		mHistoryTab.setOnClickListener(this);
		mTabs.add(mHistoryTab);

		mInviteFriends = findViewById(R.id.right);
		mInviteFriends.setOnClickListener(this);

		mConnectFriends = findViewById(R.id.connect_friends);
		mConnectFriends.setOnClickListener(this);

		mTabIndexMarker = (ImageView) findViewById(R.id.tab_background);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(mPagerAdapter); // 设置第一个tab为默认为选中状态
		mViewPager.setCurrentItem(0);
		setTabSelected(mAppTab);
	}

	/**
	 * 设置当前选中的tab
	 * 
	 * @param tab
	 */
	private void setTabSelected(View tab) {
		for (View tv : mTabs) {
			if (tab == tv) {
				tv.setSelected(true);
				if (mSelected != null) {
					doScroll(mSelected, tab, mTabIndexMarker, 400);
				} else {
					doScroll(mMediaTab, mAppTab, mTabIndexMarker, 400);
				}
				mSelected = tab;
			} else {
				tv.setSelected(false);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MusicManger.getInstance().unBindServer(this);
	}

	private void showExitDialog() {

		DialogFragment exitDialog = ExitDialog.newInstance();
		exitDialog.show(getSupportFragmentManager(), "");
	}

	public void doPositiveClick() {
		finish();
	}

	public void doNegativeClick() {
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().findFragmentByTag("music") == null) {
			showExitDialog();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		setTabSelected(mTabs.get(position));
	}

	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onClick(View v) {
		if (v == mConnectFriends) {
			startActivity(new Intent(this, ConnectActivity.class));
		} else if (v == mInviteFriends) {
			startActivity(new Intent(this, InviteFriendsActivity.class));
		} else {
			setTabSelected(v);
			mViewPager.setCurrentItem(mTabs.indexOf(v), false);
		}

	}

	private void doScroll(View view1, View view2, View viewToScroll,
			int duration) {
		TranslateAnimation ta = new TranslateAnimation(
				(view1.getLeft() + view1.getRight() - mMediaTab.getLeft() - mMediaTab
						.getRight()) / 2, (view2.getLeft() + view2.getRight()
						- mMediaTab.getLeft() - mMediaTab.getRight()) / 2,
				viewToScroll.getTop(), viewToScroll.getTop());
		ta.setFillAfter(true);
		ta.setDuration(duration);
		viewToScroll.startAnimation(ta);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			mDragController.update(x, y);
		}
		if (!mDragController.isDragMode()) {
			return super.dispatchTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			mDragController.dragModeEnd();
			break;
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			mDragController.update(x, y);
			break;
		}
		return true;
	}
}
