package com.nano.lanshare.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * 拖动时绘制的代表被拖动物体的View
 * 
 * @author King Bright
 * 
 */
public class DragView extends View {

	private static int ALPHA = 128;
	private Bitmap mDrawingCache;

	private DragObject mDragObject;

	private int left, top;
	private Paint paint;

	public DragView(Context context, Bitmap drawingCache) {
		super(context);
		mDrawingCache = drawingCache;
		paint = new Paint();
		paint.setAlpha(ALPHA);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mDrawingCache, left, top, paint);
	}

	public DragObject getDragObject() {
		return mDragObject;
	}

	public void setDragObject(DragObject object) {
		this.mDragObject = object;
	}

	public void show() {
		DragController.getInstance(null).dragModeStart(this);
	}

	public void move(int x, int y) {
		left = x;
		top = y;
		invalidate();
	}
}
