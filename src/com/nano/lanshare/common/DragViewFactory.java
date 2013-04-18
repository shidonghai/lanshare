package com.nano.lanshare.common;

import android.content.Context;
import android.graphics.Bitmap;
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
		view.destroyDrawingCache();
		view.buildDrawingCache(true);
		Bitmap bitmap = view.getDrawingCache(true);
		if (bitmap == null) {
			return null;
		}
		DragView dragView = new DragView(context, bitmap);
		dragView.setDragObject(object);
		return dragView;
	}
}
