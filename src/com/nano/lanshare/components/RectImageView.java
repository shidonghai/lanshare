package com.nano.lanshare.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.nano.lanshare.thumbnail.util.RecyclingImageView;

/**
 * Custom IamgeView with rect. Default rect color is white.
 * 
 * @author Xiaohu
 * 
 */
public class RectImageView extends RecyclingImageView {
	public static final int DEFAULT_COLOR = Color.WHITE;

	private int mRectColor = DEFAULT_COLOR;

	public RectImageView(Context context) {
		super(context);
	}

	public RectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect rect = canvas.getClipBounds();
		Paint paint = new Paint();
		paint.setColor(mRectColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRect(rect, paint);
	}

	/**
	 * Set the rect color, default is white.
	 *
	 * @param color
	 *            rect color.
	 */
	public void setRectColor(int color) {
		mRectColor = color;
	}

}
