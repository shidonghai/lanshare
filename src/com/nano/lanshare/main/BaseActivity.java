package com.nano.lanshare.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.nano.lanshare.conn.logic.UserManager;
import com.nano.lanshare.conn.ui.ConnectActivity;
import com.nano.lanshare.history.logic.IHistoryDelete;
import com.nano.lanshare.invitation.ui.InviteFriendsActivity;
import com.nano.lanshare.main.adapter.FragmentTabsFactory;
import com.nano.lanshare.main.adapter.MainViewPagerAdapter;
import com.nano.lanshare.main.ui.ExitDialog;
import com.nano.lanshare.socket.SocketBroadcastReceiver;
import com.nano.lanshare.socket.SocketService;

public class BaseActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	public static final String PICK_A_FRIEND_AND_SEND = "pick_a_friend_and_send";

	private ViewPager mViewPager;
	private List<TextView> mTabs = new ArrayList<TextView>();
	private MainViewPagerAdapter mPagerAdapter;
	private TextView mAppTab;
	private TextView mPhotoTab;
	private TextView mMediaTab;
	private TextView mFileTab;
	private TextView mHistoryTab;
	private ViewGroup mDeleteView;
	private ViewGroup mTabsContainer;
	private TextView mDelCancel;
	private TextView mDelConfrim;
	private IHistoryDelete mDeleteListener;

	private View mSelected;
	private ImageView mTabIndexMarker;

	private View mConnectFriends;

	private DragController mDragController;

	private Handler mHandler = new Handler();

	private SocketBroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_base);
		initView();
		mDragController = DragController
				.getInstance((ViewGroup) findViewById(R.id.base_viewgroup));

		MusicManger.getInstance().bindServer(this);

		mReceiver = new SocketBroadcastReceiver(this);

		startSocketService();
	}

	private void startSocketService() {
		Intent intent = new Intent();
		intent.setClass(BaseActivity.this, SocketService.class);
		intent.putExtra(SocketService.CMD_CODE, SocketService.CMD_DISCOVER);
		startService(intent);
	}

	private void stopSocketService() {
		Intent intent = new Intent();
		intent.setClass(BaseActivity.this, SocketService.class);
		stopService(intent);
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

		mDeleteView = (ViewGroup) findViewById(R.id.delete_view);
		mTabsContainer = (ViewGroup) findViewById(R.id.tabs_container);

		mDelCancel = (TextView) findViewById(R.id.del_cancel);
		mDelCancel.setOnClickListener(this);

		mDelConfrim = (TextView) findViewById(R.id.del_confrim);
		mDelConfrim.setOnClickListener(this);

		mConnectFriends = findViewById(R.id.connect_friends);
		mConnectFriends.setOnClickListener(this);

		mTabIndexMarker = (ImageView) findViewById(R.id.tab_background);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(mPagerAdapter); // 设置第一个tab为默认为选中状态
		mViewPager.setCurrentItem(0);

		View inviteView = findViewById(R.id.right);
		inviteView.setOnClickListener(this);
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
					doScroll(mMediaTab, mAppTab, mTabIndexMarker, 0);
				}
				mSelected = tab;
			} else {
				tv.setSelected(false);
			}
		}
	}

	@Override
	protected void onPause() {
		mReceiver.unregister();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mReceiver.register();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setTabSelected(mAppTab);
			}
		}, 100);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MusicManger.getInstance().unBindServer(this);
		stopSocketService();
		UserManager.getInstance().clearUser();
		mDragController.destroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_hist, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	public int getCurrentTabIndex() {
		return mViewPager.getCurrentItem();
	}

	public void setDeleteListener(IHistoryDelete l) {
		mDeleteListener = l;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (FragmentTabsFactory.TABS_HISTORY == getCurrentTabIndex()
				&& item.getItemId() == R.id.menu_del) {
			showHistoryDeleteMode(true);
		}
		return super.onOptionsItemSelected(item);
	}

	private void showHistoryDeleteMode(boolean flag) {
		if (flag) {
			mDeleteView.setVisibility(View.VISIBLE);
			mTabsContainer.setVisibility(View.GONE);
			mDeleteListener.prepareDelete();
		} else {
			mDeleteView.setVisibility(View.GONE);
			mTabsContainer.setVisibility(View.VISIBLE);
			mDeleteListener.cancelDelete();
		}
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
		switch (v.getId()) {
		case R.id.connect_friends:
			startActivity(new Intent(this, ConnectActivity.class));
			break;
		case R.id.leftTab1:
		case R.id.leftTab2:
		case R.id.leftTab3:
		case R.id.leftTab4:
		case R.id.leftTab5:
			setTabSelected(v);
			mViewPager.setCurrentItem(mTabs.indexOf(v), false);
			break;
		case R.id.del_cancel:
			showHistoryDeleteMode(false);
			break;
		case R.id.del_confrim:
			mDeleteListener.startDelete();
			// showHistoryDeleteMode(false);
			break;
		case R.id.right:
			startActivity(new Intent(this, InviteFriendsActivity.class));
			break;
		default:
			break;
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
