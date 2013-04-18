package com.nano.lanshare.components;

import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

import com.nano.lanshare.common.DragView;
import com.nano.lanshare.common.DragViewFactory;

public class LongClickListener implements OnItemLongClickListener {
	private Context mContext;
	private int mId;

	public LongClickListener(Context context, int imageViewId) {
		mContext = context;
		mId = imageViewId;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		DragView dragView = DragViewFactory.createDragView(mContext,
				(ImageView) view.findViewById(mId), null);
		if (dragView == null) {
			return true;
		}
		view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
				HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
		dragView.show();
		return true;
	}

}
