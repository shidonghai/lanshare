package com.nano.lanshare.file.ui;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.file.FileItem;
import com.nano.lanshare.file.FileList;
import com.nano.lanshare.utils.FileSizeUtil;

public class FileListAdapter extends BaseAdapter {

	public static final int FILE_TYPE_FOLDER = 0;

	public static final int FILE_TYPE_FILE = 1;

	public static final int FILE_TYPE_BACK = 2;

	private static final int[] TYPE_COUNT = new int[] { FILE_TYPE_FOLDER,
			FILE_TYPE_FILE, FILE_TYPE_BACK };

	private FileList mList;

	private LayoutInflater mInflater;

	public FileListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
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
		case FILE_TYPE_FILE: {
			convertView = createFileTypeView(convertView, mList.getFileList()
					.get(position).file);
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

	private View createFileTypeView(View convertView, File file) {
		FileViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.file_layout, null);

			viewHolder = new FileViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.file_name);
			viewHolder.size = (TextView) convertView
					.findViewById(R.id.file_size);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (FileViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(file.getName());
		viewHolder.size.setText(FileSizeUtil.formatFromByte(file.length()));
		return convertView;

	}

	public void setFiles(FileList list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	private class FolderViewHolder {
		TextView name;
	}

	private class FileViewHolder {
		TextView name;
		TextView size;
	}

}
