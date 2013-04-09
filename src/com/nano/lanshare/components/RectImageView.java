package com.nano.lanshare.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Custom IamgeView with rect.
 * 
 * @author Xiaohu
 * 
 */
public class RectImageView extends ImageView {

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
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRect(rect, paint);
	}

}
