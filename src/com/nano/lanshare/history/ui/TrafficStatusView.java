
package com.nano.lanshare.history.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TrafficStatusView extends View {

    private static Map<String, Integer> mTrafficColorMap;
    private Map<String, Integer> mTrafficInfoMap;
    static {
        mTrafficColorMap = new HashMap<String, Integer>();
        mTrafficColorMap.put("send", Color.GREEN);
        mTrafficColorMap.put("receive", Color.YELLOW);
        mTrafficColorMap.put("gprs", Color.CYAN);
        mTrafficColorMap.put("wifi", Color.RED);
        mTrafficColorMap.put("wifiAp", Color.BLUE);
    }

    public TrafficStatusView(Context context) {
        super(context);
    }

    public TrafficStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        int oldColor = paint.getColor();
        paint.setColor(mTrafficColorMap.get("send"));
        canvas.drawRect(0, 0, 100, 20, paint);
        paint.setColor(mTrafficColorMap.get("receive"));
        canvas.drawRect(100, 0, getWidth(), 20, paint);
        paint.setColor(oldColor);

        super.onDraw(canvas);
    }

    public void setTrafficData(Map<String, Integer> info) {
        mTrafficInfoMap = info;
    }

}
