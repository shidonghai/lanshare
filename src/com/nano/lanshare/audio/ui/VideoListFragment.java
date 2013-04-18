package com.nano.lanshare.audio.ui;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicItemFragment;
import com.nano.lanshare.components.LongClickListener;
import com.nano.lanshare.components.operation.OperationDialog;
import com.nano.lanshare.components.operation.PopupMenuUtil;

public class VideoListFragment extends BasicItemFragment implements
		OnItemClickListener {

	private QueryVideo mQueryVideo;

	private VideoListAdapter mVideoAdapter;

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// return inflater.inflate(R.layout.video_tab, null);
	// }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mVideoAdapter = new VideoListAdapter(getActivity(), null);
		GridView gridView = (GridView) view.findViewById(R.id.gridview);
		gridView.setOnItemClickListener(VideoListFragment.this);
		gridView.setAdapter(mVideoAdapter);
		gridView.setOnItemLongClickListener(new LongClickListener(
				getActivity(), R.id.video_image));
		mProgress.setVisibility(View.VISIBLE);
		mEmptyView.setText(R.string.dm_no_file_prompt_video);

		mQueryVideo = new QueryVideo();
		mQueryVideo.execute();
		Log.d("zxh", "VideoListFragment onViewCreated");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("zxh", "VideoListFragment onStop");
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

			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			mProgress.setVisibility(View.GONE);
			if (null != mVideoAdapter) {
				mVideoAdapter.changeCursor(result);
				mEmptyView
						.setVisibility((result == null || result.getCount() == 0) ? View.VISIBLE
								: View.GONE);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final OperationDialog operationDialog = new OperationDialog(
				getActivity());
		final String path = (String) arg1.getTag();
		Log.d("zxh", "path:" + path);
		operationDialog.setContent(PopupMenuUtil.FILE_POPUP_IAMGES,
				PopupMenuUtil.FILE_OPUP_TEXT, new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						switch (arg2) {
						case PopupMenuUtil.MENU_TRANSPORT:

							break;
						case PopupMenuUtil.MENU_ACTION:
							Intent intent = new Intent(Intent.ACTION_VIEW);
							Uri uri = Uri.fromFile(new File(path));
							intent.setDataAndType(uri, "video/*");
							startActivity(intent);
							break;
						case PopupMenuUtil.MENU_PROPARTY:
							PopupMenuUtil.showPropertyDialog(getActivity(),
									path);
							break;
						case PopupMenuUtil.MENU_OPERATION:

							break;
						default:
							break;
						}
					}
				});
		operationDialog.showAsDropDown(arg1);
	}

	@Override
	public View createContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.video_tab, null);
	}
}
