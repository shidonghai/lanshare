package com.nano.lanshare.pics.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nano.lanshare.components.BasicContentStore;

public class PicAdapter extends CursorAdapter implements BasicContentStore {
	private LayoutInflater mLayoutInflater;

	public PicAdapter(Context context, Cursor c, boolean autoRequery,
			LayoutInflater layoutInflater) {
		super(context, c, autoRequery);
		mLayoutInflater = layoutInflater;
	}

	@Override
	public void setContent(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup group) {
//		mLayoutInflater.inflate(resource, root)
		return null;
	}
}
