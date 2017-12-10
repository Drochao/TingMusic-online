package com.dntz.tingmusic.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.adapter.OnlineMusicAdapater;
import com.dntz.tingmusic.adapter.PlaylistAdapter;
import com.dntz.tingmusic.entity.MusicEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String rulgetmusic = "http://192.168.1.105:8080/getMusic";
    public static final String rul = "http://192.168.1.107:8080/musicsystem/";
    public static final String dr = Environment.getDataDirectory().getPath() ;
    private ListView listView;
    private Handler myHandler;
    private String json = "[{\"musicid\":51,\"musicmd5\":\"\",\"musicname\":\"巴赫\"," +
            "\"musicauthor\":\"岑宁儿\",\"musiclyric\":\"1\"," +
            "\"musickeywards\":\"1\"," +
            "\"musicimage\":\"upload/image/a5efbe1881384b30b7324879ecaf470f.jpg\"," +
            "\"musicalbum\":\"1\"," +
            "\"musicurl\":\"upload/image/e7bda4ab8f494f3d948a2ec700865855.mp3\"}," +
            "{\"musicid\":52,\"musicmd5\":\"\",\"musicname\":\"浮夸\"," +
            "\"musicauthor\":\"陈奕迅\",\"musiclyric\":\"2\",\"musickeywards\":\"2\"," +
            "\"musicimage\":\"upload/image/f4056dc02547463f99bf7cb95efbf73e.png\"," +
            "\"musicalbum\":\"1\"," +
            "\"musicurl\":\"upload/image/037a931844b94981bd3fb7b617401f93.mp3\"}," +
            "{\"musicid\":53,\"musicmd5\":\"\",\"musicname\":\"成都\"," +
            "\"musicauthor\":\"1\",\"musiclyric\":\"\",\"musickeywards\":\"\"," +
            "\"musicimage\":\"\",\"musicalbum\":\"\"," +
            "\"musicurl\":\"upload/image/0bfb3c3243174db39254eefded2445f8.mp3\"}]";
    String[] urlMusicc= new String[]{"http://m10.music.126.net/20171207191505/6bbec2370e03e1eed6f09acc50bceb82/ymusic/ba68/6e0f/35b5/06214f396fee38fc6a47bc63846acb88.mp3",
            "http://win.web.ri01.sycdn.kuwo.cn/resource/n3/48/31/4052450782.mp3",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2012178017,770654329&fm=27&gp=0.jpg"};
    String urlMusic = "http://192.168.1.107:8080/musicsystem/upload/image/0bfb3c3243174db39254eefded2445f8.mp3";
    String url = "http://127.0.0.1:8080/getMusic";
    private OnlineMusicAdapater onlineMusicAdapater = null;
    private List<MusicEntity> dataList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        dataList = new ArrayList<MusicEntity>();
        onlineMusicAdapater = new OnlineMusicAdapater((ArrayList<MusicEntity>) dataList, MainActivity.this);
        listView = (ListView) findViewById(R.id.online_music_list);
        listView.setAdapter(onlineMusicAdapater);

        toolbar = (Toolbar) findViewById(R.id.activity_download_toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("在线下载");
        }

        //downButton = (Button) findViewById(R.id.down_button);
        myHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200:
                        String json = (String) msg.getData().get("json");
                        ArrayList<MusicEntity> dataList;
                        dataList = fromJsonList(json, MusicEntity.class);
                        OnlineMusicAdapater onlineMusicAdapater = new OnlineMusicAdapater(dataList, MainActivity.this);
                        listView.setAdapter(onlineMusicAdapater);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        init();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = listView.getItemAtPosition(position)+"";
                String url = urlMusicc[position];
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                //设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
                        name+".mp3");
//                request.setDestinationInExternalFilesDir(getApplicationContext(), null, "tar.apk");
                id = downloadManager.enqueue(request);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("000" + dr);
                        //System.out.println(new OkHttp().downMusic(urlMusic, getdr() , "xxxx.mp3"));
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }


    private String getdr() {
        return this.getApplicationContext().getFilesDir().getPath();
    }

    private void init() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                    //String json = new OkHttp().getAllMusic(rulgetmusic);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("json",json);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 200;
                    myHandler.sendMessage(message);
            }
        }).start();
    }

    public <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(new Gson().fromJson(elem, cls));
        }
        return mList;
    }



}
