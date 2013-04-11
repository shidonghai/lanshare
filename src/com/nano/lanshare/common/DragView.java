package com.nano.lanshare.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

/**
 * 拖动时绘制的代表被拖动物体的View
 * 
 * @author King Bright
 * 
 */
public class DragView extends View{

	private float mX, mY;
	private int alpha;
	private Bitmap mDragView;

	private DragObject mDragObject;

	public DragView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
}
