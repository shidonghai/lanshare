package com.nano.lanshare.audio.ui;

import java.util.List;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.nano.lanshare.audio.bean.MusicInfo;
import com.nano.lanshare.audio.logic.IMusicStatusListener;
import com.nano.lanshare.audio.logic.MusicManger;
import com.nano.lanshare.components.BasicItemFragment;
import com.nano.lanshare.components.LongClickListener;
import com.nano.lanshare.components.operation.OperationDialog;
import com.nano.lanshare.components.operation.PopupMenuUtil;
import com.nano.lanshare.utils.FileUtil;

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

	private ImageView mPlayMode;

	private MusicListAdapter mAdapter;

	private ScanMusic mScanMusicTask;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mSeekBar.setProgress(mMusicManger.getCurrentPosition());
			sendEmptyMessageDelayed(0, 500);
		};
	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mMusicManger = MusicManger.getInstance();
		mMusicManger.registerListener(this);

		// MusicListAdapter adapter = new MusicListAdapter(getActivity(),
		// mMusicManger.getMusicList());
		mAdapter = new MusicListAdapter(getActivity());
		ListView listView = (ListView) getView().findViewById(R.id.music_list);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(new LongClickListener(
				getActivity(), R.id.music_image));

		mPlay = (ImageView) view.findViewById(R.id.music_pause);
		mPlayMode = (ImageView) view.findViewById(R.id.music_mode);
		mPlayMode.setOnClickListener(this);

		mCurrentSong = (TextView) view.findViewById(R.id.music_name);
		mSeekBar = (SeekBar) view.findViewById(R.id.music_seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);
		mProgress.setVisibility(View.VISIBLE);

		mScanMusicTask = new ScanMusic(view);
		mScanMusicTask.execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		setPlayMode(mMusicManger.getPlayMode(getActivity()), mPlayMode);

		if (mMusicManger.isStarted()) {
			mCurrentSong.setText(mMusicManger.getMusicList().get(
					mMusicManger.getCurrentIndex()).title);
			mSeekBar.setMax(mMusicManger.getDuration());
			mSeekBar.setProgress(mMusicManger.getCurrentPosition());

			if (mMusicManger.isPlaying()) {
				mHandler.sendEmptyMessage(0);
				mPlay.setImageResource(R.drawable.zapya_data_music_pause_normal);
			} else {
				mPlay.setImageResource(R.drawable.zapya_data_music_play_normal);
				Log.d("zxh",
						"mPlay.setImageResource(R.drawable.zapya_data_music_play_normal)");
			}
		}
	}

	private void setPlayMode(int mode, ImageView modeView) {
		int playMode = R.drawable.zapya_data_music_random_ist_normal;
		switch (mode) {
		case MusicManger.PLAY_MODE_LIST:
			playMode = R.drawable.zapya_data_music_random_ist_normal;
			break;
		case MusicManger.PLAY_MODE_LIST_SINGLECIRCLE:
			playMode = R.drawable.zapya_data_music_random_circleone_normal;
			break;
		case MusicManger.PLAY_MODE_RANDOM:
			playMode = R.drawable.zapya_data_music_random_normal;
			break;
		case MusicManger.PLAY_MODE_LIST_CIRCLE:
			playMode = R.drawable.zapya_data_music_random_circlelist_normal;
			break;
		default:
			break;
		}
		modeView.setImageResource(playMode);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, final int position,
			long arg3) {
		final OperationDialog operationDialog = new OperationDialog(
				getActivity());

		operationDialog.setContent(PopupMenuUtil.FILE_POPUP_IAMGES,
				PopupMenuUtil.AUDIO_POPUP_TEXT,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case PopupMenuUtil.MENU_TRANSPORT:

							break;
						case PopupMenuUtil.MENU_ACTION:
							mMusicManger.play(position);
							break;
						case PopupMenuUtil.MENU_PROPARTY:
							FileUtil.showPropertyDialog(
									getActivity(),
									mMusicManger.getMusicList().get(position).path);
							break;
						case PopupMenuUtil.MENU_OPERATION:
							FileUtil.showFileOperationDialog(
									getActivity(),
									mMusicManger.getMusicList().get(position).path);
							break;
						default:
							break;
						}
					}
				});

		operationDialog.showAsDropDown(view);

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
		case R.id.music_mode:
			setPlayMode(mMusicManger.changePlayMode(getActivity()), mPlayMode);
			break;
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

		if (null != mScanMusicTask) {
			Log.d("zxh", "cancel");
			mScanMusicTask.cancel(true);
		}
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

	private class ScanMusic extends AsyncTask<Void, Void, List<MusicInfo>> {
		private View mView;

		public ScanMusic(View view) {
			mView = view;
		}

		@Override
		protected List<MusicInfo> doInBackground(Void... params) {
			if (null == mMusicManger.getMusicList()) {
				mMusicManger.queryMusic(getActivity());
			}

			return mMusicManger.getMusicList();
		}

		@Override
		protected void onPostExecute(List<MusicInfo> result) {
			Log.d("zxh", "onPostExecute");
			mProgress.setVisibility(View.GONE);
			mAdapter.setPlaylist(result);
			mAdapter.setPlaylist(mMusicManger.getMusicList());
			mEmptyView.setText(R.string.dm_no_file_prompt_audio);
			mEmptyView
					.setVisibility(mMusicManger.getMusicList().size() == 0 ? View.VISIBLE
							: View.GONE);

			ImageView previou = (ImageView) mView.findViewById(R.id.music_pre);
			ImageView next = (ImageView) mView.findViewById(R.id.music_next);
			ImageView gotoDetail = (ImageView) mView
					.findViewById(R.id.music_detail);
			gotoDetail.setOnClickListener(MusicTabFragment.this);
			previou.setOnClickListener(MusicTabFragment.this);
			next.setOnClickListener(MusicTabFragment.this);
			mPlay.setOnClickListener(MusicTabFragment.this);
		}

	}
}
