package com.nano.lanshare.components.operation;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nano.lanshare.R;

public class OperationDialog extends PopupWindow {

	public static final int TYPE_APP = 0;

	public static final int TYPE_IMAGE = 1;

	public static final int TYPE_MUSIC = 2;

	public static final int TYPE_VIDEO = 3;

	public static final int TYPE_FILE = 4;

	private final int Y_OFF_BASE = -20;

	private int mScreenH;

	private Context mContext;

	public OperationDialog(Context context) {
		super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mContext = context;

		setFocusable(true);
		setOutsideTouchable(true);

		// If do not setBackgroundDrawable, setOutsideTouchable will no used.
		setBackgroundDrawable(new BitmapDrawable());

		WindowManager windowManage = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mScreenH = windowManage.getDefaultDisplay().getHeight();
	}

	@Override
	public void showAsDropDown(View anchor) {
		int xOff = getXoff(anchor);
		int yOff = getYoff(anchor);
		super.showAsDropDown(anchor, xOff, yOff);
	}

	private int getXoff(View anchor) {
		int anchorWidth = anchor.getWidth();
		getContentView().measure(0, 0);
		int contentWidth = getContentView().getMeasuredWidth();

		if (anchorWidth > contentWidth) {
			return anchorWidth / 2 - contentWidth / 2;
		}
		return 0;
	}

	private int getYoff(View anchor) {
		int anchorHeight = anchor.getHeight();
		getContentView().measure(0, 0);
		int contentHeight = getContentView().getMeasuredHeight();
		int[] location = new int[2];
		anchor.getLocationInWindow(location);
		int y = location[1] + anchorHeight;
		int spaceLeft = mScreenH - y;
		Log.d("zxh", "spaceLeft:" + spaceLeft + "   contentHeight:"
				+ contentHeight);
		if (spaceLeft < contentHeight) {
			return -(contentHeight + anchorHeight + Y_OFF_BASE);
		}

		return Y_OFF_BASE;

	}

	public void setContent(int[] ids, int[] name,
			final OnClickListener itemClickListener) {
		final int length = ids.length;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		// top view
		final View top = inflater.inflate(R.layout.popup_menu_top, null);
		TextView text = (TextView) top.findViewById(R.id.menu_text);
		text.setText(name[0]);
		ImageView image = (ImageView) top.findViewById(R.id.menu_image);
		image.setImageResource(ids[0]);
		top.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
				itemClickListener.onClick(null, 0);
			}
		});
		linearLayout.addView(top);

		// center view
		if (length > 2) {
			for (int i = 1; i < length - 1; i++) {
				final int index = i;
				final View center = inflater.inflate(
						R.layout.popup_menu_center, null);
				TextView textCenter = (TextView) center
						.findViewById(R.id.menu_text);
				textCenter.setText(name[i]);
				ImageView imageCenter = (ImageView) center
						.findViewById(R.id.menu_image);
				imageCenter.setImageResource(ids[i]);
				center.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismiss();
						itemClickListener.onClick(null, index);
					}
				});
				linearLayout.addView(center);
			}
		}

		// bottom view
		final View bottom = inflater.inflate(R.layout.popup_menu_bottom, null);
		TextView textBottom = (TextView) bottom.findViewById(R.id.menu_text);
		textBottom.setText(name[length - 1]);
		ImageView imageBottom = (ImageView) bottom
				.findViewById(R.id.menu_image);
		imageBottom.setImageResource(ids[length - 1]);
		bottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
				itemClickListener.onClick(null, length - 1);
			}
		});
		linearLayout.addView(bottom);

		// Set up the popup window.
		setContentView(linearLayout);
	}

}
