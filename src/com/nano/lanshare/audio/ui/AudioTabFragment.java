package com.nano.lanshare.audio.ui;

import com.nano.lanshare.R;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Video;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AudioTabFragment extends Fragment {
	private FragmentTabHost mTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTabHost = (FragmentTabHost) getActivity().findViewById(
				android.R.id.tabhost);
		mTabHost.setup(getActivity(),
				getActivity().getSupportFragmentManager(), R.id.realtabcontent);

		String music = String.format(getString(R.string.dm_tab_title_music),
				setVideoCount(Audio.Media.EXTERNAL_CONTENT_URI));
		mTabHost.addTab(mTabHost.newTabSpec("Music").setIndicator(music),
				MusicTabFragment.class, null);
		String video = String.format(getString(R.string.dm_tab_title_movies),
				setVideoCount(Video.Media.EXTERNAL_CONTENT_URI));
		mTabHost.addTab(mTabHost.newTabSpec("Video").setIndicator(video),
				VideoListFragment.class, null);
	}

	// Query the music or video count.
	private int setVideoCount(Uri uri) {
		int count = 0;

		String selection = Media.SIZE + " >0 ";
		Cursor cursor = getActivity().getContentResolver().query(uri, null,
				selection, null, null);
		if (null != cursor) {
			count = cursor.getCount();
			cursor.close();
		}

		return count;
	}

}
