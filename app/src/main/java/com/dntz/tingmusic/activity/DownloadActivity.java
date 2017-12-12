package com.dntz.tingmusic.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.adapter.OnlineMusicAdapter;
import com.dntz.tingmusic.entity.MusicEntity;
import com.dntz.tingmusic.util.OkHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends PlayBarBaseActivity {
    private Toolbar toolbar;
    public static final String rulgetmusic = "http://192.168.1.114:8080/getMusic/";
    public static final String rul = "http://192.168.1.109:8080/musicsystem/";
    public static final String dr = Environment.getDataDirectory().getPath() ;
    private ListView listView;
    private Handler myHandler;
    private String json = "[{\"musicid\":51,\"musicmd5\":\"\",\"musicname\":\"成都\"," +
            "\"musicauthor\":\"岑宁儿\",\"musiclyric\":\"1\"," +
            "\"musickeywards\":\"1\"," +
            "\"musicimage\":\"upload/image/a5efbe1881384b30b7324879ecaf470f.jpg\"," +
            "\"musicalbum\":\"1\"," +
            "\"musicurl\":\"http://win.web.ri01.sycdn.kuwo.cn/resource/n3/48/31/4052450782.mp3\"}," +
            "{\"musicid\":52,\"musicmd5\":\"\",\"musicname\":\"浮夸\"," +
            "\"musicauthor\":\"陈奕迅\",\"musiclyric\":\"2\",\"musickeywards\":\"2\"," +
            "\"musicimage\":\"upload/image/f4056dc02547463f99bf7cb95efbf73e.png\"," +
            "\"musicalbum\":\"1\"," +
            "\"musicurl\":\"upload/image/037a931844b94981bd3fb7b617401f93.mp3\"}," +
            "{\"musicid\":53,\"musicmd5\":\"\",\"musicname\":\"巴赫\"," +
            "\"musicauthor\":\"1\",\"musiclyric\":\"\",\"musickeywards\":\"\"," +
            "\"musicimage\":\"\",\"musicalbum\":\"\"," +
            "\"musicurl\":\"upload/image/0bfb3c3243174db39254eefded2445f8.mp3\"}]";
    private OnlineMusicAdapter onlineMusicAdapater = null;
    private List<MusicEntity> dataList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //startService(new Intent(this, PlayerService.class));
        dataList = new ArrayList<MusicEntity>();
        onlineMusicAdapater = new OnlineMusicAdapter((ArrayList<MusicEntity>) dataList, DownloadActivity.this);
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
                        OnlineMusicAdapter onlineMusicAdapater = new OnlineMusicAdapter(dataList, DownloadActivity.this);
                        listView.setAdapter(onlineMusicAdapater);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
       init();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String name = listView.getItemAtPosition(position)+"";
//                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//
//                Uri uri = Uri.parse(rul + url);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                //设置允许使用的网络类型，这里是移动网络和wifi都可以
//                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
//                        name+".mp3");
////                request.setDestinationInExternalFilesDir(getApplicationContext(), null, "tar.apk");
//                id = downloadManager.enqueue(request);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("000" + dr);
//                        //System.out.println(new OkHttp().downMusic(urlMusic, getdr() , "xxxx.mp3"));
//                    }
//                }).start();
//            }
//        });
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

                try {
                    String jsonMusic = new OkHttp().getAllMusic(rulgetmusic);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("json",jsonMusic);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 200;
                    myHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
