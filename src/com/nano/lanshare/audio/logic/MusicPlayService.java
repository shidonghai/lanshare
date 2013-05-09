package com.nano.lanshare.audio.logic;

import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MusicPlayService extends Service implements OnPreparedListener,
		OnCompletionListener, OnErrorListener {

	private boolean isStarted = false;

	private MediaPlayer mPlayer;

	private MusicManger mMusicManger;

	private int mCureentIndex = -1;

	@Override
	public void onCreate() {
		super.onCreate();
		mPlayer = new MediaPlayer();
		Log.d("zxh", "MusicPlayService onCreate");
		mMusicManger = MusicManger.getInstance();
	}

	@Override
	public IBinder onBind(Intent arg0) {

		return mBinder;
	}

	private MusicBinderAidl.Stub mBinder = new MusicBinderAidl.Stub() {

		@Override
		public void stop() throws RemoteException {

		}

		@Override
		public void startPlay() throws RemoteException {
			mPlayer.start();
		}

		@Override
		public void setCurrentInfo(Bundle mediaInfo) throws RemoteException {

		}

		@Override
		public void seek(int msec) throws RemoteException {
			mPlayer.seekTo(msec);
		}

		@Override
		public void release() throws RemoteException {

		}

		@Override
		public void prev() throws RemoteException {
			mCureentIndex = mCureentIndex - 1;
			if (mCureentIndex < 0) {
				mCureentIndex = mMusicManger.getMusicList().size() - 1;
			}
			Log.d("zxh", "mCureentIndex" + mCureentIndex);
			playMusic(mCureentIndex);
		}

		@Override
		public void playOnLine(Bundle songInfo) throws RemoteException {

		}

		@Override
		public void play(int id) throws RemoteException {
			playMusic(id);
		}

		@Override
		public void pause() throws RemoteException {
			mPlayer.pause();
		}

		@Override
		public void next() throws RemoteException {
			playNext();
		}

		@Override
		public boolean isPlaying() throws RemoteException {
			// TODO Auto-generated method stub
			return mPlayer.isPlaying();
		}

		@Override
		public int getState() throws RemoteException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getDuration() throws RemoteException {
			return isStarted ? mPlayer.getDuration() : 0;
		}

		@Override
		public int getCurrentPosion() throws RemoteException {
			return isStarted ? mPlayer.getCurrentPosition() : 0;
		}

		@Override
		public String getCurrentContentFilePath() throws RemoteException {
			return null;
		}

		@Override
		public int getMusicIndex() throws RemoteException {
			return mCureentIndex;
		}

		@Override
		public boolean isStarted() throws RemoteException {
			// TODO Auto-generated method stub
			return isStarted;
		}
	};

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mMusicManger.onError();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mMusicManger.onComplete();

		switch (mMusicManger.getPlayMode(getApplicationContext())) {
		case MusicManger.PLAY_MODE_LIST:
			if (mCureentIndex != mMusicManger.getMusicList().size() - 1) {
				playNext();
			}
			break;
		case MusicManger.PLAY_MODE_LIST_SINGLECIRCLE:
			playMusic(mCureentIndex);
			break;
		case MusicManger.PLAY_MODE_RANDOM:
			Random random = new Random();
			int index = random.nextInt(mMusicManger.getMusicList().size());
			playMusic(index);
			break;
		case MusicManger.PLAY_MODE_LIST_CIRCLE:
			playNext();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mMusicManger.onPrepare();
		mPlayer.start();
	}

	public void playMusic(int id) {
		if (id < 0) {
			return;
		}

		if (null != mPlayer) {
			isStarted = true;
			Log.d("zxh", "playMusic:" + id);
			mPlayer.reset();
			try {
				mCureentIndex = id;
				mPlayer.setDataSource(getApplicationContext(), mMusicManger
						.getMusicList().get(id).uri);
				mPlayer.setOnCompletionListener(this);
				mPlayer.setOnPreparedListener(this);
				mPlayer.setOnErrorListener(this);
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mPlayer.prepareAsync();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void playNext() {
		mCureentIndex = mCureentIndex + 1;
		if (mCureentIndex == mMusicManger.getMusicList().size()) {
			mCureentIndex = 0;
		}
		Log.d("zxh", "mCureentIndex" + mCureentIndex);
		playMusic(mCureentIndex);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mPlayer) {
			Log.d("zxh", " MusicPlayService onDestroy");
			mPlayer.release();
			mPlayer = null;
		}
	}

}
