package com.dntz.tingmusic.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatDelegate;

import com.dntz.tingmusic.service.MusicPlayerService;

/**
 * Created by dntz on 2017/10/8.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Intent startIntent = new Intent(MyApplication.this,MusicPlayerService.class);
        startService(startIntent);
    }


    public static Context getContext() {
        return context;
    }
}
