package com.nano.lanshare.audio.ui;

import java.util.Formatter;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.audio.bean.MusicInfo;
import com.nano.lanshare.audio.logic.IMusicStatusListener;
import com.nano.lanshare.audio.logic.MusicManger;

public class MusicDetailFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener, IMusicStatusListener {

	public final int REWIND_FORWARD_BASE = 5000;

	private TextView mTitle;

	private TextView mCurrentPosition;

	private TextView mDuration;

	private TextView mMusicName;

	private TextView mArtist;

	private ImageView mPlay;

	private MusicManger mMusicManger;

	private SeekBar mSeekBar;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			refresh();
			sendEmptyMessageDelayed(0, 500);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.music_detail, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mMusicManger = MusicManger.getInstance();
		mMusicManger.registerListener(this);

		mTitle = (TextView) view.findViewById(R.id.music_index);
		mCurrentPosition = (TextView) view
				.findViewById(R.id.music_current_duration);
		mDuration = (TextView) view.findViewById(R.id.music_duration);
		mMusicName = (TextView) view.findViewById(R.id.music_name);
		mArtist = (TextView) view.findViewById(R.id.music_artist);

		ImageView back = (ImageView) view.findViewById(R.id.music_back);
		ImageView playMode = (ImageView) view
				.findViewById(R.id.music_play_mode);
		setPlayMode(mMusicManger.getPlayMode(getActivity()), playMode);
		ImageView previou = (ImageView) view.findViewById(R.id.music_pre);
		ImageView rewind = (ImageView) view.findViewById(R.id.music_rewind);
		mPlay = (ImageView) view.findViewById(R.id.music_pause);
		ImageView forward = (ImageView) view.findViewById(R.id.music_forward);
		ImageView next = (ImageView) view.findViewById(R.id.music_next);
		back.setOnClickListener(this);
		playMode.setOnClickListener(this);
		previou.setOnClickListener(this);
		rewind.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		forward.setOnClickListener(this);
		next.setOnClickListener(this);

		mSeekBar = (SeekBar) view.findViewById(R.id.music_seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);

		init();
	}

	private void init() {
		int index = mMusicManger.getCurrentIndex();
		mTitle.setText(String.format(getString(R.string.musci_title_index),
				index + 1, mMusicManger.getMusicList().size()));

		MusicInfo info = mMusicManger.getMusicList().get(index);
		mArtist.setText(info.artist);
		mMusicName.setText(info.title);
		mCurrentPosition.setText(parseSec(mMusicManger.getCurrentPosition()));
		mDuration.setText(parseSec(mMusicManger.getDuration()));
		mSeekBar.setMax(mMusicManger.getDuration());
		mSeekBar.setProgress(mMusicManger.getCurrentPosition());

		if (mMusicManger.isPlaying()) {
			mHandler.sendEmptyMessage(0);
			mPlay.setImageResource(R.drawable.zapya_data_music_single_pause_normal);
		}
	}

	private void refresh() {
		mCurrentPosition.setText(parseSec(mMusicManger.getCurrentPosition()));
		mSeekBar.setProgress(mMusicManger.getCurrentPosition());
	}

	@Override
	public void onStop() {
		super.onStop();
		mMusicManger.unRegisterListener(this);
		mHandler.removeMessages(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.music_pause:
			togglePlaying();
			break;
		case R.id.music_pre:
			mMusicManger.previou();
			break;
		case R.id.music_next:
			mMusicManger.next();
			break;
		case R.id.music_back:
			getFragmentManager().beginTransaction().remove(this).commit();
			break;
		case R.id.music_play_mode:
			setPlayMode(mMusicManger.changePlayMode(getActivity()),
					(ImageView) v);
			break;
		case R.id.music_rewind:
			mMusicManger.seek(mMusicManger.getCurrentPosition()
					- REWIND_FORWARD_BASE);
			break;
		case R.id.music_forward:
			mMusicManger.seek(mMusicManger.getCurrentPosition()
					+ REWIND_FORWARD_BASE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mMusicManger.seek(seekBar.getProgress());
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

	private void togglePlaying() {
		if (!mMusicManger.isStarted()) {
			mMusicManger.play(mMusicManger.getCurrentIndex());
			mPlay.setImageResource(R.drawable.zapya_data_music_single_pause_normal);
		} else if (mMusicManger.isPlaying()) {
			mPlay.setImageResource(R.drawable.zapya_data_music_single_play_normal);
			mMusicManger.pause();
			mHandler.removeMessages(0);
		} else {
			mPlay.setImageResource(R.drawable.zapya_data_music_single_pause_normal);
			mMusicManger.start();
			mHandler.sendEmptyMessage(0);
		}

	}

	private void setPlayMode(int mode, ImageView modeView) {
		int playMode = R.drawable.zapya_data_music_single_list_normal;
		switch (mode) {
		case MusicManger.PLAY_MODE_LIST:
			playMode = R.drawable.zapya_data_music_single_list_normal;
			break;
		case MusicManger.PLAY_MODE_LIST_SINGLECIRCLE:
			playMode = R.drawable.zapya_data_music_single_list_singlecircle_normal;
			break;
		case MusicManger.PLAY_MODE_RANDOM:
			playMode = R.drawable.zapya_data_music_single_random_normal;
			break;
		case MusicManger.PLAY_MODE_LIST_CIRCLE:
			playMode = R.drawable.zapya_data_music_single_listcircle_normal;
			break;
		default:
			break;
		}
		modeView.setImageResource(playMode);
	}

	@Override
	public void onPrepare() {
		init();
		mHandler.sendEmptyMessage(0);
	}

	@Override
	public void onComplete() {
		mHandler.removeMessages(0);
	}

	@Override
	public void onError() {
		mHandler.removeMessages(0);
	}

}
