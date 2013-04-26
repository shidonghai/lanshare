/**
 * 文件名称 : FriendListFastScrollWarper.java
 * <p>
 * 作者信息 : Yang Ke
 * <p>
 * 创建时间 : Oct 9, 2011, 4:18:25 PM
 * <p>
 * 版权声明 : Copyright (c) 2009-2012 CIeNET Ltd. All rights reserved
 * <p>
 * 评审记录 :
 * <p>
 */

package com.nano.lanshare.friend.scroll;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nano.lanshare.R;

/**
 * 列表包装器，增加快速滚动功能
 * <p>
 */
public class ListScrollWarper extends RelativeLayout implements OnTouchListener {
	/**
	 * 用来显示列表
	 */
	private LinearLayout mListParent;

	/**
	 * A-Z的字母索引视图
	 */
	private ImageView mA2ZView;

	/**
	 * 索引关键字的View
	 */
	private View mFastKeyView;

	/**
	 * TextView,用来显示索引关键字的名字
	 */
	private TextView mKeyTextView;

	/**
	 * 列表快速滚动的接口
	 */
	private IListScroll mFastScroll;

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文
	 */
	public ListScrollWarper(Context aContext) {
		super(aContext);

		inflate(aContext, R.layout.friend_list_warper, this);

		// 用来显示列表
		mListParent = (LinearLayout) findViewById(R.id.fl_parent);

		// A-Z的字母索引视图
		mA2ZView = (ImageView) findViewById(R.id.fl_a2z);
		mA2ZView.setOnTouchListener(this);

		// 用来显示索引关键字的名字
		mFastKeyView = findViewById(R.id.fl_fastkey_back);
		mKeyTextView = (TextView) findViewById(R.id.fl_fastkey_text);
	}

	/**
	 * 对列表进行包装
	 * 
	 * @param aFriendList
	 *            列表视图
	 * @param aFastScroll
	 *            列表快速滚动接口
	 */
	public void warpFriendList(View aListView, IListScroll aFastScroll) {
		mListParent.removeAllViews();
		mListParent.addView(aListView);

		// 设置快速滚动接口
		mFastScroll = aFastScroll;
	}

	/**
	 * 还原快速滚动
	 */
	public void resetFastSroll() {
		mA2ZView.setBackgroundDrawable(null);
		mFastKeyView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 隐藏快速滚动
	 */
	public void hideFastScroll() {
		mA2ZView.setVisibility(View.INVISIBLE);
		mFastKeyView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 显示快速滚动
	 */
	public void showFastScroll() {
		mA2ZView.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏FastKey
	 */
	public void hideFastKey() {
		mFastKeyView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 显示FastKey
	 * 
	 * @param aKey
	 *            FastKey
	 */
	public void showFastKey(String aKey) {
		if (TextUtils.isEmpty(aKey) ^ true) {
			mKeyTextView.setText(aKey);
			mFastKeyView.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		boolean response = false;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE: {
			// 按下和滑动事件
			doScrollByPos(event.getY());
			mA2ZView.setImageResource(R.drawable.zapya_group_search_pressed);
			response = true;
			break;
		}

		case MotionEvent.ACTION_UP: {
			// 弹起事件
			mFastKeyView.setVisibility(View.INVISIBLE);
			mA2ZView.setImageResource(R.drawable.zapya_group_search_default);
			response = true;
			break;
		}

		default: {
			break;
		}
		}

		return response;
	}

	/**
	 * 根据位置滚动
	 * 
	 * @param aYpos
	 *            Y坐标
	 */
	private void doScrollByPos(float aYpos) {
		if (aYpos < 0) {
			doScrollTop();
			return;
		}

		// 一共28个字符，包括字母A上面的2个符号
		int index = (int) (27 * aYpos / getHeight()) - 1;
		if (index < 0) {
			doScrollTop();
			return;
		}

		// 最大就26个字母
		index = (index > 25) ? 25 : index;

		// 设置显示的关键字.
		final char key = (char) ('A' + index);

		mKeyTextView.setText(String.valueOf(key));
		mFastKeyView.setVisibility(View.VISIBLE);

		if (null != mFastScroll) {
			// 列表快速滚动定位
			mFastScroll.scrolltoIndexByKey(String.valueOf(key));
		}
	}

	/**
	 * 滚动到顶端
	 */
	private void doScrollTop() {
		mFastKeyView.setVisibility(View.INVISIBLE);

		if (null != mFastScroll) {
			// 好友列表快速滚动定位
			mFastScroll.scrolltoIndex(0);
		}
	}
}
