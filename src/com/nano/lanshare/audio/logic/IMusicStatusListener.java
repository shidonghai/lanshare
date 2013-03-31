package com.nano.lanshare.audio.logic;

/**
 * Music status interface.
 * 
 * @author Xiaohu
 * 
 */
public interface IMusicStatusListener {

	// MediaPlayer onPrepare
	void onPrepare();

	void onComplete();

	void onError();

}
