package com.nano.lanshare.file.ui;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.RectImageView;
import com.nano.lanshare.file.FileItem;
import com.nano.lanshare.file.FileList;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.thumbnail.util.ImageWorker.LoadMethod;
import com.nano.lanshare.utils.FileSizeUtil;

public class FileListAdapter extends BaseAdapter {

	public static final int FILE_TYPE_FOLDER = 0;

	public static final int FILE_TYPE_FILE = 1;

	public static final int FILE_TYPE_BACK = 2;

	public static final int FILE_TYPE_IMAGE = 3;

	public static final int FILE_TYPE_AUDIO = 4;

	private static final int[] TYPE_COUNT = new int[] { FILE_TYPE_FOLDER,
			FILE_TYPE_FILE, FILE_TYPE_BACK, FILE_TYPE_IMAGE };

	private FileList mList;

	private LayoutInflater mInflater;

	private Bitmap mDefaultImageIcon;
	private ImageWorker mWorker;
	private LoadMethod mPicLoader = new LoadMethod() {
		@Override
		public Bitmap processBitmap(Object obj, Context context) {
			String path = (String) obj;
			Log.d("zxh", "processBitmap:" + path);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 50, 50);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}

	};

	public FileListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mDefaultImageIcon = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.photo_default_icon);
		mWorker = ((LanshareApplication) context.getApplicationContext())
				.getImageWorker();
	}

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

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.getFileList() == null ? 0 : mList
				.getFileList().size();
	}

	public FileList getFiles() {
		return mList;
	}

	@Override
	public FileItem getItem(int position) {
		return mList == null ? null : mList.getFileList() == null ? null
				: mList.getFileList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT.length;
	}

	@Override
	public int getItemViewType(int position) {
		// if (null == mList.getFileList().get(position)) {
		// return FILE_TYPE_BACK;
		// }
		//
		// return mList.getFileList().get(position).isDirectory() ?
		// FILE_TYPE_FOLDER
		// : FILE_TYPE_FILE;
		return mList.getFileList().get(position).type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		switch (getItemViewType(position)) {
		case FILE_TYPE_FOLDER: {
			convertView = createFolderTypeView(convertView, mList.getFileList()
					.get(position).file);
			break;
		}
		case FILE_TYPE_IMAGE:
		case FILE_TYPE_FILE: {
			convertView = createFileTypeView(convertView, mList.getFileList()
					.get(position));
			break;
		}
		case FILE_TYPE_BACK: {
			convertView = createBackView(convertView);
			break;
		}
		default: {
			break;
		}
		}

		return convertView;
	}

	private View createBackView(View convertView) {
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.file_back_layout, null);
		}

		return convertView;
	}

	private View createFolderTypeView(View convertView, File file) {
		FolderViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.file_folder, null);

			viewHolder = new FolderViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.folder_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (FolderViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(file.getName());
		return convertView;
	}

	private View createFileTypeView(View convertView, FileItem fileItem) {
		FileViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.file_layout, null);

			viewHolder = new FileViewHolder();
			viewHolder.icon = (RectImageView) convertView
					.findViewById(R.id.icon);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.file_name);
			viewHolder.size = (TextView) convertView
					.findViewById(R.id.file_size);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (FileViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(fileItem.file.getName());
		viewHolder.size.setText(FileSizeUtil.formatFromByte(fileItem.file
				.length()));

		// if (fileItem.type == FileListAdapter.FILE_TYPE_IMAGE) {
		// Log.d("zxh", "FileListAdapter.FILE_TYPE_IMAGE");
		// mWorker.loadImage(fileItem.file.getAbsolutePath(), viewHolder.icon,
		// mDefaultImageIcon, mPicLoader);
		// }
		switchItem(fileItem, viewHolder.icon);
		return convertView;

	}

	private void switchItem(FileItem fileItem, RectImageView itemView) {
		switch (fileItem.type) {
		case FileListAdapter.FILE_TYPE_IMAGE:
			mWorker.loadImage(fileItem.file.getAbsolutePath(), itemView,
					mDefaultImageIcon, mPicLoader);
			break;
		default:
			itemView.setRectColor(Color.TRANSPARENT);
			break;
		}
	}

	public void setFiles(FileList list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	private class FolderViewHolder {
		TextView name;
	}

	private class FileViewHolder {
		RectImageView icon;
		TextView name;
		TextView size;
	}

}
