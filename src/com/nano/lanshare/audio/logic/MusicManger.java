package com.nano.lanshare.audio.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;

import com.nano.lanshare.audio.bean.MusicInfo;

public class MusicManger {
	public static final int PLAY_MODE_LIST = 0;

	public static final int PLAY_MODE_LIST_SINGLECIRCLE = 1;

	public static final int PLAY_MODE_RANDOM = 2;

	public static final int PLAY_MODE_LIST_CIRCLE = 3;

	public static final String MUSIC_PREFERENCE = "music_preference";

	public static final String MUSIC_PLAY_MODE = "music_play_mode";

	private static MusicManger instance;

	private MusicBinderAidl mServerAidl;

	private MusicManger() {
	}

	public static MusicManger getInstance() {
		if (null == instance) {
			instance = new MusicManger();
		}

		return instance;
	}

	private List<IMusicStatusListener> mMusicStatusListener = new ArrayList<IMusicStatusListener>();

	private List<MusicInfo> mInfos;

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mServerAidl = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mServerAidl = MusicBinderAidl.Stub.asInterface(service);
		}
	};

	/**
	 * Get the music list.
	 * 
	 * @return List<MusicInfo>
	 */
	public synchronized List<MusicInfo> getMusicList() {
		return mInfos;
	}

	public boolean isStartServer() {
		return mServerAidl != null;
	}

	public synchronized List<MusicInfo> queryMusic(Context context) {
		List<MusicInfo> list = new ArrayList<MusicInfo>();
		String[] projection = new String[] { Media._ID, Media.DISPLAY_NAME,
				Media.MIME_TYPE, Media.TITLE, Media.ALBUM, Media.ARTIST,
				Media.SIZE };

		String selection = Media.SIZE + " >0 ";

		Cursor cursor = context.getContentResolver().query(
				Media.EXTERNAL_CONTENT_URI, projection, selection, null,
				Media.DISPLAY_NAME + " COLLATE LOCALIZED");

		if (null != cursor && cursor.moveToFirst()) {
			try {
				MusicInfo info;
				do {
					info = new MusicInfo();
					info.id = cursor.getLong(cursor.getColumnIndex(Media._ID));
					info.artist = cursor
							.getString(cursor
									.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST));
					info.title = cursor
							.getString(cursor
									.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE));
					info.size = cursor
							.getLong(cursor
									.getColumnIndex(android.provider.MediaStore.Audio.Media.SIZE));
					info.uri = ContentUris.withAppendedId(
							Audio.Media.EXTERNAL_CONTENT_URI, info.id);
					list.add(info);
				} while (cursor.moveToNext());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		mInfos = list;
		return list;
	}

	/**
	 * Add the music status listener.
	 * 
	 * @param listener
	 */
	public void registerListener(IMusicStatusListener listener) {
		if (null != listener && !mMusicStatusListener.contains(listener)) {
			mMusicStatusListener.add(listener);
		}
	}

	/**
	 * Remove the music status listener.
	 * 
	 * @param listener
	 */
	public void unRegisterListener(IMusicStatusListener listener) {
		if (null != listener && mMusicStatusListener.contains(listener)) {
			mMusicStatusListener.remove(listener);
		}
	}

	public void onError() {
		for (IMusicStatusListener listener : mMusicStatusListener) {
			listener.onError();
		}
	}

	public void onPrepare() {
		for (IMusicStatusListener listener : mMusicStatusListener) {
			listener.onPrepare();
		}
	}

	public void onComplete() {
		for (IMusicStatusListener listener : mMusicStatusListener) {
			listener.onComplete();
		}
	}

	public boolean bindServer(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MusicPlayService.class);
		return context.bindService(intent, mServiceConnection,
				Context.BIND_AUTO_CREATE);
	}

	public void unBindServer(Context context) {
		if (null != mServiceConnection) {
			context.unbindService(mServiceConnection);
			context.stopService(new Intent(context, MusicPlayService.class));
		}
	}

	public void start() {
		try {
			mServerAidl.startPlay();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play the music.
	 * 
	 * @param id
	 *            music index
	 */
	public void play(int id) {
		try {
			mServerAidl.play(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		try {
			mServerAidl.pause();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void next() {
		try {
			mServerAidl.next();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void previou() {
		try {
			mServerAidl.prev();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public int getDuration() {
		try {
			return mServerAidl.getDuration();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void seek(int msec) {
		try {
			mServerAidl.seek(msec);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public int getCurrentPosition() {
		try {
			return mServerAidl.getCurrentPosion();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int getCurrentIndex() {
		try {
			return mServerAidl.getMusicIndex();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public boolean isStarted() {
		try {
			return mServerAidl.isStarted();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isPlaying() {
		try {
			return mServerAidl.isPlaying();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}

	public int getPlayMode(Context context) {
		SharedPreferences musicPreferences = context.getSharedPreferences(
				MUSIC_PREFERENCE, Context.MODE_PRIVATE);
		return musicPreferences.getInt(MUSIC_PLAY_MODE, PLAY_MODE_LIST);
	}

	public void setPlayMode(Context context, int playMode) {
		SharedPreferences musicPreferences = context.getSharedPreferences(
				MUSIC_PREFERENCE, Context.MODE_PRIVATE);
		musicPreferences.edit().putInt(MUSIC_PLAY_MODE, playMode).commit();
	}

	public int changePlayMode(Context context) {
		int currentMode = getPlayMode(context);
		int playMode = currentMode == PLAY_MODE_LIST_CIRCLE ? PLAY_MODE_LIST
				: currentMode + 1;
		setPlayMode(context, playMode);
		return playMode;
	}
}
