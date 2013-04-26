package com.nano.lanshare.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nano.lanshare.R;

public class RotateView extends ImageView {
	/**
	 * 转动频率
	 */
	private static final int INCREMENT = 30;

	/**
	 * 延迟时间
	 */
	private static final int DELAY_TIME = 80;

	/**
	 * 处理器
	 */
	private Handler mHandler;

	/**
	 * 是否停止
	 */
	private boolean flagStop;

	private int msgCount;

	private int arc;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 */
	public RotateView(Context context) {
		super(context);
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param attrs
	 *            属性
	 */
	public RotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	public void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.VISIBLE) {
			start();
		} else {
			flagStop = true;
		}
	}

	/**
	 * 停止
	 */
	public void stop() {
		flagStop = true;
	}

	/**
	 * 开始
	 */
	public void start() {
		flagStop = false;
		if (msgCount == 0) {
			mHandler.sendEmptyMessage(0);
			msgCount++;
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (getDrawable() == null) {
			// setImageDrawable(getResources().getDrawable(
			// R.drawable.zapya_group_refresh_normal));
			setImageResource(R.drawable.zapya_group_refresh_normal);
		}
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				msgCount--;
				if (!flagStop) {
					invalidate();
					arc = (arc + INCREMENT) % 360;
					sendEmptyMessageDelayed(0, DELAY_TIME);
					msgCount++;
				}
			}
		};
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(arc, canvas.getClipBounds().width() / 2, canvas
				.getClipBounds().height() / 2);
		super.onDraw(canvas);
	}
}
