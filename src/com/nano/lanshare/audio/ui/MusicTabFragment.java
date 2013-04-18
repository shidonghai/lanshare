package com.nano.lanshare.audio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.audio.logic.IMusicStatusListener;
import com.nano.lanshare.audio.logic.MusicManger;
import com.nano.lanshare.components.BasicItemFragment;
import com.nano.lanshare.components.LongClickListener;
import com.nano.lanshare.components.operation.FileOperationContentView;
import com.nano.lanshare.components.operation.OperationDialog;

/**
 * Music tab
 * 
 * @author Xiaohu
 * 
 */
public class MusicTabFragment extends BasicItemFragment implements
		OnItemClickListener, IMusicStatusListener, OnClickListener,
		OnSeekBarChangeListener {

	private MusicManger mMusicManger;

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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mMusicManger = MusicManger.getInstance(getActivity());
		mMusicManger.registerListener(this);

		MusicListAdapter adapter = new MusicListAdapter(getActivity(),
				mMusicManger.getMusicList());
		ListView listView = (ListView) getView().findViewById(R.id.music_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(new LongClickListener(
				getActivity(), R.id.music_image));
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

		mEmptyView.setText(R.string.dm_no_file_prompt_audio);
		mEmptyView
				.setVisibility(mMusicManger.getMusicList().size() == 0 ? View.VISIBLE
						: View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mMusicManger.isStarted()) {
			mCurrentSong.setText(mMusicManger.getMusicList().get(
					mMusicManger.getCurrentIndex()).title);
			mSeekBar.setMax(mMusicManger.getDuration());
			mSeekBar.setProgress(mMusicManger.getCurrentPosition());

			if (mMusicManger.isPlaying()) {
				mHandler.sendEmptyMessage(0);
				mPlay.setImageResource(R.drawable.zapya_data_music_pause_normal);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		final OperationDialog operationDialog = new OperationDialog(
				getActivity());

		FileOperationContentView fileContentView = new FileOperationContentView(
				OperationDialog.TYPE_MUSIC, mMusicManger.getMusicList().get(
						arg2).path, operationDialog);
		fileContentView.setActionClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMusicManger.play(arg2);
				operationDialog.dismiss();
			}
		});
		operationDialog.setContentView(fileContentView
				.createContentView(getActivity()));

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
		if (mMusicManger.getCurrentIndex() == -1) {
			return;
		}

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

	@Override
	public View createContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.music_tab, null);
	}
}
