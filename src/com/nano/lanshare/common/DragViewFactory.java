package com.nano.lanshare.common;

import android.content.Context;
import android.widget.ImageView;

public class DragViewFactory {
	/**
	 * 根据被长按的item的数据和他的view构造一个DragView
	 * 
	 * @param context
	 * @param view
	 * @param object
	 * @return
	 */
	public static DragView createDragView(Context context, ImageView view,
			DragObject object) {
		DragView dragView = new DragView(context, view.getDrawingCache(true));
		dragView.setDragObject(object);
		return dragView;
	}
}
