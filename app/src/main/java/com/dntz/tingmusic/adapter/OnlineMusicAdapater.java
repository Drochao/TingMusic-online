package com.dntz.tingmusic.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.activity.MainActivity;
import com.dntz.tingmusic.entity.MusicEntity;
import com.dntz.tingmusic.util.OkHttp;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by 72408 on 2017/12/6.
 */

public class OnlineMusicAdapater extends BaseAdapter {
    private ArrayList<MusicEntity> dataList;
    private Context context;
    private TextView musicNameV;
    private LinearLayout content;

    public OnlineMusicAdapater(ArrayList<MusicEntity> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public MusicEntity getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.online_music_item, null);

        musicNameV = (TextView) convertView.findViewById(R.id.online_music_name);
        content = (LinearLayout) convertView.findViewById(R.id.content_view);
        System.out.println(dataList);
        MusicEntity musicEntity = dataList.get(position);
        System.out.println(musicEntity);
        musicNameV.setText(musicEntity.getMusicname());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getItem(position).getMusicurl();
                String name = getItem(position).getMusicname();
                Toast.makeText(context,"下载成功",Toast.LENGTH_LONG).show();
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(MainActivity.rul + url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                //设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
                        name+".mp3");
//                request.setDestinationInExternalFilesDir(getApplicationContext(), null, "tar.apk");
                long id = downloadManager.enqueue(request);
            }
        });


        return convertView;
    }
//    DownloadManager downloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
//
//    Uri uri = Uri.parse(MainActivity.rul + url);
//    //Uri uri = Uri.parse(MainActivity.urlPic);
//    DownloadManager.Request request = new DownloadManager.Request(uri);
//
//    //设置允许使用的网络类型，这里是移动网络和wifi都可以
//                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
//
//    //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
//    //request.setShowRunningNotification(false);
//
//    //不显示下载界面
//                request.setVisibleInDownloadsUi(true);
//                request.setTitle("下载歌曲");
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
////               request.setDestinationInExternalPublicDir(dr, getItem(position).getMusicname());
////                request.setDestinationInExternalFilesDir(dr, getItem(position).getMusicname());
//
//    /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
////request.setDestinationInExternalFilesDir(this, null, "tar.apk");
//    long id = downloadManager.enqueue(request);

}
