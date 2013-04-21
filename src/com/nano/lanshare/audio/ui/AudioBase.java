package com.nano.lanshare.audio.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.widget.LinearLayout;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;

public class AudioBase extends BasicTabFragment {

	@Override
	protected void onUpdateData(Message msg) {

	}

	@Override
	protected void init() {
		String music = String.format(getString(R.string.dm_tab_title_music),
				setVideoCount(Audio.Media.EXTERNAL_CONTENT_URI));
		String video = String.format(getString(R.string.dm_tab_title_movies),
				setVideoCount(Video.Media.EXTERNAL_CONTENT_URI));
		setTitle(LEFT, music);
		setTitle(RIGHT, video);

		LinearLayout leftLayout = new LinearLayout(getActivity());
		leftLayout.setId(R.id.music_left_tab);
		getGroup(LEFT).addView(leftLayout);

		LinearLayout rightLayout = new LinearLayout(getActivity());
		rightLayout.setId(R.id.music_right_tab);
		getGroup(RIGHT).addView(rightLayout);

		getFragmentManager().beginTransaction()
				.add(R.id.music_left_tab, new MusicTabFragment()).commit();
		getFragmentManager().beginTransaction()
				.add(R.id.music_right_tab, new VideoListFragment()).commit();
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
