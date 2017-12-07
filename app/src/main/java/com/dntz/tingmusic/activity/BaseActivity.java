package com.dntz.tingmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.util.MyMusicUtil;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TengLuoPurpleTheme);
    }


}
