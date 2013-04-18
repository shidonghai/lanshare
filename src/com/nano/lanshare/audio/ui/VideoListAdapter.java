package com.nano.lanshare.audio.ui;

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Video.Media;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.thumbnail.util.ImageWorker.LoadMethod;

public class VideoListAdapter extends CursorAdapter {

	private Context mContext;

	private Bitmap mDefaultImageIcon;

	private ImageWorker mWorker;

	private LoadMethod mVideoLoadMethod = new LoadMethod() {

		@Override
		public Bitmap processBitmap(Object obj, Context context) {
			Bitmap bitmap = Thumbnails.getThumbnail(
					mContext.getContentResolver(), (Long) obj,
					Thumbnails.MICRO_KIND, null);
			return bitmap;
		}
	};

	public VideoListAdapter(Context context, Cursor c) {
		super(context, c);
		mContext = context;
		mDefaultImageIcon = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.zapya_data_video_l);
		mWorker = ((LanshareApplication) context.getApplicationContext())
				.getImageWorker();
	}

	@Override
	public void bindView(View contentView, Context arg1, Cursor cursor) {
		TextView textView = (TextView) contentView.findViewById(R.id.duration);
		textView.setText(parseSec(cursor.getInt(cursor
				.getColumnIndex(Media.DURATION))));

		ImageView imageView = (ImageView) contentView
				.findViewById(R.id.video_image);
		// Bitmap bitmap =
		// Thumbnails.getThumbnail(mContext.getContentResolver(),
		// cursor.getLong(cursor.getColumnIndex(Media._ID)),
		// Thumbnails.MICRO_KIND, null);
		// imageView.setImageBitmap(bitmap);
		mWorker.loadImage(cursor.getLong(cursor.getColumnIndex(Media._ID)),
				imageView, mDefaultImageIcon, mVideoLoadMethod);
		contentView.setTag(cursor.getString(cursor.getColumnIndex(Media.DATA)));
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup arg2) {
		return LayoutInflater.from(context).inflate(R.layout.video_grid_item,
				null);
	}

	private String parseSec(int timeMs) {
		if (timeMs < 0) {
			timeMs = 0;
		}
		int totalSeconds = timeMs % 1000 >= 500 ? timeMs / 1000 + 1
				: timeMs / 1000;
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;

		Formatter mFormatter = new java.util.Formatter(null,
				Locale.getDefault());
		return mFormatter.format("%02d:%02d", minutes, seconds).toString();

	}
}
