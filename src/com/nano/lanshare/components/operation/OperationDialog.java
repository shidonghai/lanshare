package com.nano.lanshare.components.operation;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class OperationDialog extends PopupWindow {

	public static final int TYPE_APP = 0;

	public static final int TYPE_IMAGE = 1;

	public static final int TYPE_MUSIC = 2;

	public static final int TYPE_VIDEO = 3;

	public static final int TYPE_FILE = 4;

	private final int Y_OFF_BASE = -20;

	private View mContentView;

	private int screenH;

	public OperationDialog(Context context, int type, String path) {
		super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		mContentView = OperationContent.CreateContentView(context, type);
		setContentView(mContentView);
		setFocusable(true);
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());

		WindowManager windowManage = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		screenH = windowManage.getDefaultDisplay().getHeight();
	}

	@Override
	public void showAsDropDown(View anchor) {
		int xOff = getXoff(anchor);
		int yOff = getYoff(anchor);
		super.showAsDropDown(anchor, xOff, yOff);
	}

	private int getXoff(View anchor) {
		int anchorWidth = anchor.getWidth();
		mContentView.measure(0, 0);
		int contentWidth = mContentView.getMeasuredWidth();

		if (anchorWidth > contentWidth) {
			return anchorWidth / 2 - contentWidth / 2;
		}
		return 0;
	}

	private int getYoff(View anchor) {
		int anchorHeight = anchor.getHeight();
		mContentView.measure(0, 0);
		int contentHeight = mContentView.getMeasuredHeight();
		int[] location = new int[2];
		anchor.getLocationInWindow(location);
		int y = location[1] + anchorHeight;
		int spaceLeft = screenH - y;
		Log.d("zxh", "spaceLeft:" + spaceLeft + "   contentHeight:"
				+ contentHeight);
		if (spaceLeft < contentHeight) {
			return -(contentHeight + anchorHeight + Y_OFF_BASE);
		}

		return Y_OFF_BASE;

	}
}
