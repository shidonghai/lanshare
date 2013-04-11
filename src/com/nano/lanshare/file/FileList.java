package com.nano.lanshare.file;

import java.util.List;

public class FileList {
	private List<FileItem> mList;

	private int mCurrentPosition;

	public void setPosition(int currentPosition) {
		this.mCurrentPosition = currentPosition;
	}

	public int getPosition() {
		return mCurrentPosition;
	}

	public FileList(List<FileItem> list) {
		this.mList = list;
	}

	public List<FileItem> getFileList() {
		return mList;
	}

	public void setFileList(List<FileItem> list) {
		this.mList = list;
	}
}
