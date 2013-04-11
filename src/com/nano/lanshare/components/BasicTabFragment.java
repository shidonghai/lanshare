package com.nano.lanshare.components;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nano.lanshare.R;

public abstract class BasicTabFragment extends Fragment implements
		OnClickListener, OnItemClickListener, OnItemLongClickListener {
	protected static final int LEFT = 1;
	protected static final int RIGHT = 2;

	protected static final int START_LOADING = 3;
	protected static final int FINISH_LOADING = 4;
	protected static final int UPDATE_UI = 5;

	private TextView mLeftTab;
	private TextView mRightTab;

	private GridView mLeftContent;
	private GridView mRightContent;

	private ProgressBar mProgress;

	private View mScroller;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_LOADING:
				// progress dialog show;
				mLeftContent.setVisibility(View.GONE);
				mRightContent.setVisibility(View.GONE);
				mProgress.setVisibility(View.VISIBLE);
				break;
			case FINISH_LOADING:
				// progress dialog dismiss;
				mLeftContent.setVisibility(mLeftTab.isSelected() ? View.VISIBLE
						: View.GONE);
				mLeftContent
						.setVisibility(!mLeftTab.isSelected() ? View.VISIBLE
								: View.GONE);
				mProgress.setVisibility(View.GONE);
				break;
			case UPDATE_UI:
				onUpdateData(msg);
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.basic_tab_layout, null);

		mProgress = (ProgressBar) view.findViewById(R.id.progress);
		mScroller = view.findViewById(R.id.scroller);

		mLeftTab = (TextView) view.findViewById(R.id.left_tab);
		mRightTab = (TextView) view.findViewById(R.id.right_tab);
		mLeftTab.setSelected(true);

		mLeftContent = (GridView) view.findViewById(R.id.left_content);
		mRightContent = (GridView) view.findViewById(R.id.right_content);

		// register tab click listener
		mLeftTab.setOnClickListener(this);
		mRightTab.setOnClickListener(this);
		// register adapter item click listener
		mLeftContent.setOnItemClickListener(this);
		mRightContent.setOnItemClickListener(this);
		// register adapter item long click listener
		mLeftContent.setOnItemLongClickListener(this);
		mRightContent.setOnItemLongClickListener(this);

		init();
		return view;
	}

	/**
	 * 实现更新界面的方法，为了区别更新哪个adapter，可以在message的arg1参数中声明LEFT或者RIGHT，来区别更新。
	 * 
	 * @param msg
	 */
	protected abstract void onUpdateData(Message msg);

	/**
	 * 在这个方法中做一些初始化工作
	 */
	protected abstract void init();

	protected void notifyStartLoading() {
		mHandler.sendEmptyMessage(START_LOADING);
	}

	protected void notifyFinishLoading() {
		mHandler.sendEmptyMessage(FINISH_LOADING);
	}

	public Handler getHandler() {
		return mHandler;
	}

	protected void setTitle(int side, String title) {
		switch (side) {
		case LEFT:
			mLeftTab.setText(title);
			break;
		case RIGHT:
			mLeftTab.setText(title);
			break;
		}
	}

	protected BasicContentStore getStore(int side) {
		switch (side) {
		case LEFT:
			return (BasicContentStore) mLeftContent.getAdapter();
		case RIGHT:
			return (BasicContentStore) mRightContent.getAdapter();
		}
		return null;
	}

	protected GridView getGridView(int side) {
		switch (side) {
		case LEFT:
			return mLeftContent;
		case RIGHT:
			return mRightContent;
		}
		return null;
	}

	protected void setAdapter(int side, BasicContentStore adapter) {
		switch (side) {
		case LEFT:
			mLeftContent.setAdapter((ListAdapter) adapter);
			break;
		case RIGHT:
			mRightContent.setAdapter((ListAdapter) adapter);
			break;
		}
	}

	private void scroll2Right(View v) {
		TranslateAnimation tranAnim = new TranslateAnimation(v.getLeft(),
				v.getLeft() + v.getWidth(), v.getTop(), v.getTop());
		tranAnim.setFillAfter(true);
		tranAnim.setDuration(400);
		v.startAnimation(tranAnim);
	}

	private void scroll2Left(View v) {
		TranslateAnimation tranAnim = new TranslateAnimation(v.getLeft()
				+ v.getWidth(), v.getLeft(), v.getTop(), v.getTop());
		tranAnim.setFillAfter(true);
		tranAnim.setDuration(400);
		v.startAnimation(tranAnim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_tab:
			if (mLeftTab.isSelected()) {
				return;
			}
			scroll2Left(mScroller);

			mLeftTab.setSelected(true);
			mRightTab.setSelected(false);
			mLeftContent.setVisibility(View.VISIBLE);
			mRightContent.setVisibility(View.GONE);
			break;
		case R.id.right_tab:
			if (mRightTab.isSelected()) {
				return;
			}
			scroll2Right(mScroller);

			mRightTab.setSelected(true);
			mLeftTab.setSelected(false);
			mRightContent.setVisibility(View.VISIBLE);
			mLeftContent.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		if (adapterView.getAdapter() == mLeftContent.getAdapter()) {
			onLeftItemClick(adapterView, view, position, id);
		} else {
			onRightItemClick(adapterView, view, position, id);
		}
	}

	/**
	 * click listener for right side items
	 * 
	 * @param adapterView
	 * @param view
	 * @param position
	 * @param id
	 */
	private void onRightItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
	}

	/**
	 * click listener for left side items
	 * 
	 * @param adapterView
	 * @param view
	 * @param position
	 * @param id
	 */
	private void onLeftItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		if (adapterView.getAdapter() == mLeftContent.getAdapter()) {
			return onLeftItemLongClick(adapterView, view, position, id);
		} else {
			return onRightItemLongClick(adapterView, view, position, id);
		}
	}

	/**
	 * long click listener for left side items
	 * 
	 * @param adapterView
	 * @param view
	 * @param position
	 * @param id
	 */
	private boolean onLeftItemLongClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		return false;
	}

	/**
	 * long click listener for right side items
	 * 
	 * @param adapterView
	 * @param view
	 * @param position
	 * @param id
	 */
	private boolean onRightItemLongClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		return false;
	}
}
