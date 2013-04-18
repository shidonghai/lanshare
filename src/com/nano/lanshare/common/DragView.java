package com.nano.lanshare.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * 拖动时绘制的代表被拖动物体的View
 * 
 * @author King Bright
 * 
 */
public class DragView extends View {

	private static final int ALPHA = 166;
	private Bitmap mDrawingCache;

	private DragObject mDragObject;

	private Paint paint;
	private Rect mSrc;
	private Rect mDest;

	private int widthHalf, heightHalf;

	public DragView(Context context, Bitmap drawingCache) {
		super(context);
		mDrawingCache = drawingCache;
		paint = new Paint();
		paint.setAlpha(ALPHA);
		mSrc = new Rect(0, 0, mDrawingCache.getWidth(),
				mDrawingCache.getHeight());
		mDest = new Rect(0, 0, 0, 0);
		widthHalf = mDrawingCache.getWidth() * 3 / 4;
		heightHalf = mDrawingCache.getHeight() * 3 / 4;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mDrawingCache, mSrc, mDest, paint);
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
		mDest.left = x - widthHalf;
		mDest.right = x + widthHalf;
		mDest.top = y - 2 * heightHalf;
		mDest.bottom = y;
		invalidate();
	}
}
