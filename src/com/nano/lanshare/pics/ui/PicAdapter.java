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
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
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
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
