package com.aospstudio.android.apps.mediabook;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class cb extends Service {
    private static final String TAG = null;
    MediaPlayer mediaplayer;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaplayer.setLooping(true); // Set looping
        mediaplayer.setVolume(100,100);

    }
    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaplayer.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
        //mediaplayer.start();
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        mediaplayer.stop();
        mediaplayer.release();
    }

    @Override
    public void onLowMemory() {

    }
}
