package com.nano.lanshare.audio.logic;

interface MusicBinderAidl
{
    boolean isPlaying();
    void stop();
    void pause();
    void startPlay();
    void play(int id);
    void playOnLine(in Bundle songInfo);
    void prev(in Bundle SongInfo);
    void next(in Bundle SongInfo);
    void setCurrentInfo(in Bundle mediaInfo);
    String getCurrentContentFilePath();
    
    int getDuration();
    int getCurrentPosion();
    int getMusicIndex();
    void seek(int msec);
    void release();
    int getState();
    
}