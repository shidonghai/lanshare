package com.nano.lanshare.pics.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicContentStore;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.thumbnail.util.ImageWorker.LoadMethod;

public class PicAdapter extends CursorAdapter implements BasicContentStore {
	private LayoutInflater mLayoutInflater;
	private Bitmap mDefaultImageIcon;
	private ImageWorker mWorker;
	private int mDataIndex;

	private LoadMethod mPicLoader = new LoadMethod() {
		@Override
		public Bitmap processBitmap(Object obj, Context context) {
			String path = (String) obj;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 400, 400);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}

	};

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	public PicAdapter(Context context, Cursor c, boolean autoRequery,
			LayoutInflater layoutInflater) {
		super(context, c, autoRequery);
		mLayoutInflater = layoutInflater;
		mDefaultImageIcon = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.photo_default_icon);
		mWorker = ((LanshareApplication) context.getApplicationContext())
				.getImageWorker();
	}

	@Override
	public void setContent(Object o) {
		Cursor c = (Cursor) o;
		mDataIndex = c.getColumnIndex(Images.Media.DATA);
		this.changeCursor(c);
		notifyDataSetChanged();
	}

	@Override
	public Object getContent() {
		return getCursor();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		icon.setDrawingCacheEnabled(true);
		mWorker.loadImage(cursor.getString(mDataIndex), icon,
				mDefaultImageIcon, mPicLoader);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup group) {
		return mLayoutInflater.inflate(R.layout.pic_item, null);
	}
}
