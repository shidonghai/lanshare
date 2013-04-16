package com.nano.lanshare.audio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.nano.lanshare.R;
import com.nano.lanshare.audio.logic.IMusicStatusListener;
import com.nano.lanshare.audio.logic.MusicManger;
import com.nano.lanshare.components.operation.OperationDialog;

/**
 * Music tab
 * 
 * @author Xiaohu
 * 
 */
public class MusicTabFragment extends Fragment implements OnItemClickListener,
		IMusicStatusListener, OnClickListener, OnSeekBarChangeListener {

	private MusicManger mMusicManger;

	private int mCurrentIndex;

	private SeekBar mSeekBar;

	private TextView mCurrentSong;

	private ImageView mPlay;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mSeekBar.setProgress(mMusicManger.getCurrentPosition());
			sendEmptyMessageDelayed(0, 500);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.music_tab, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mMusicManger = MusicManger.getInstance(getActivity());
		mMusicManger.registerListener(this);

		MusicListAdapter adapter = new MusicListAdapter(getActivity(),
				mMusicManger.getMusicList());
		ListView listView = (ListView) getView().findViewById(R.id.music_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		ImageView detail = (ImageView) view.findViewById(R.id.music_detail);
		ImageView previou = (ImageView) view.findViewById(R.id.music_pre);
		mPlay = (ImageView) view.findViewById(R.id.music_pause);
		ImageView next = (ImageView) view.findViewById(R.id.music_next);
		ImageView model = (ImageView) view.findViewById(R.id.music_mode);
		detail.setOnClickListener(this);
		previou.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		next.setOnClickListener(this);
		model.setOnClickListener(this);

		mCurrentSong = (TextView) view.findViewById(R.id.music_name);
		mSeekBar = (SeekBar) view.findViewById(R.id.music_seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);
	}

	// @Override
	// public void onResume() {
	// super.onResume();
	// if (mMusicManger.isPlaying()) {
	// mPlay.setImageResource(R.drawable.zapya_data_music_play_normal);
	// mHandler.removeMessages(0);
	// } else {
	// mPlay.setImageResource(R.drawable.zapya_data_music_pause_normal);
	// mHandler.sendEmptyMessage(0);
	// }
	// }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		OperationDialog operationDialog = new OperationDialog(getActivity(),
				OperationDialog.TYPE_MUSIC, null);
		operationDialog.showAsDropDown(arg1);
	}

	@Override
	public void onPrepare() {
		mCurrentSong.setText(mMusicManger.getMusicList().get(
				mMusicManger.getCurrentIndex()).title);
		mSeekBar.setMax(mMusicManger.getDuration());
		mPlay.setImageResource(R.drawable.zapya_data_music_pause_normal);
		mHandler.sendEmptyMessage(0);
	}

	@Override
	public void onComplete() {

	}

	@Override
	public void onError() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.music_detail:
			gotoMusicDetail();
			break;
		case R.id.music_pre:
			mMusicManger.previou();
			break;
		case R.id.music_next:
			mMusicManger.next();
			break;
		case R.id.music_pause:
			togglePlaying();
		default:
			break;
		}
	}

	private void gotoMusicDetail() {
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		MusicDetailFragment detailFragment = new MusicDetailFragment();
		transaction.add(R.id.base_viewgroup, detailFragment, "music");
		transaction.addToBackStack(null);
		transaction.commit();
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

	@Override
	public void onStop() {
		super.onStop();
		mMusicManger.unRegisterListener(this);
		mHandler.removeMessages(0);
	}

	private void togglePlaying() {
		if (!mMusicManger.isStarted()) {
			mMusicManger.play(mMusicManger.getCurrentIndex());
			mPlay.setImageResource(R.drawable.zapya_data_music_pause_normal);
		} else if (mMusicManger.isPlaying()) {
			mPlay.setImageResource(R.drawable.zapya_data_music_play_normal);
			mMusicManger.pause();
			mHandler.removeMessages(0);
		} else {
			mPlay.setImageResource(R.drawable.zapya_data_music_pause_normal);
			mMusicManger.start();
			mHandler.sendEmptyMessage(0);
		}

	}
}
