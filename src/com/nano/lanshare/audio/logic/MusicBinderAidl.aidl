package com.nano.lanshare.audio.logic;

interface MusicBinderAidl
{
    boolean isPlaying();
    boolean isStarted();
    void stop();
    void pause();
    void startPlay();
    void play(int id);
    void playOnLine(in Bundle songInfo);
    void prev();
    void next();
    void setCurrentInfo(in Bundle mediaInfo);
    String getCurrentContentFilePath();
    
    int getDuration();
    int getCurrentPosion();
    int getMusicIndex();
    void seek(int msec);
    void release();
    int getState();
    
}