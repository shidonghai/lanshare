package com.nano.lanshare.audio.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.Media;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.operation.OperationDialog;

public class VideoListFragment extends Fragment implements OnItemClickListener {

	private QueryVideo mQueryVideo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.video_tab, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQueryVideo = new QueryVideo();
		mQueryVideo.execute();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (null != mQueryVideo) {
			mQueryVideo.cancel(true);
		}
	}

	private class QueryVideo extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
			String selection = Media.SIZE + " >0";

			Cursor cursor = getActivity().getContentResolver().query(
					Video.Media.EXTERNAL_CONTENT_URI, null, selection, null,
					Media.DISPLAY_NAME + " COLLATE LOCALIZED");

			Log.d("zxh", "count:" + cursor.getCount());

			cursor.moveToFirst();
			do {
				Log.d("zxh",
						cursor.getLong(cursor.getColumnIndex(Media._ID))
								+ "  "
								+ cursor.getString(cursor
										.getColumnIndex(Media.ARTIST))
								+ cursor.getString(cursor
										.getColumnIndex(Media.DATA)));

			} while (cursor.moveToNext());

			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			Log.d("zxh", "onPostExecute:" + result.getCount());
			VideoListAdapter adapter = new VideoListAdapter(getActivity(),
					result);
			GridView gridView = (GridView) getActivity().findViewById(
					R.id.gridview);
			gridView.setOnItemClickListener(VideoListFragment.this);
			gridView.setAdapter(adapter);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		OperationDialog dialog = new OperationDialog(getActivity(),
				OperationDialog.TYPE_VIDEO, null);
		dialog.showAsDropDown(arg1);
	}
}
