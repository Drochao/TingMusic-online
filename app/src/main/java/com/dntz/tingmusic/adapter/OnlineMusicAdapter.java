package com.dntz.tingmusic.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.activity.DownloadActivity;
import com.dntz.tingmusic.entity.MusicEntity;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by dntz on 2017/3/28.
 */

public class OnlineMusicAdapter extends BaseAdapter {
    private ArrayList<MusicEntity> dataList;
    private Context context;
    public OnlineMusicAdapter(ArrayList<MusicEntity> dataList, Context context) {
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.online_music_item, null);
            holder = new ViewHolder();
            holder.musicNameV = (TextView) convertView.findViewById(R.id.online_music_name);
            holder.downloadIv = (ImageView) convertView.findViewById(R.id.download_iv);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        MusicEntity musicEntity = dataList.get(position);
        holder.musicNameV.setText(musicEntity.getMusicname());


        holder.musicNameV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PlayEvent playEvent;
//                playEvent = new PlayEvent();
//                List<MusicEntity> queue = new ArrayList<>();
//                queue.add(geturl(getItem(position).getMusicurl()));
//                playEvent.setAction(PlayEvent.Action.PLAY);
//                playEvent.setQueue(queue);
//                EventBus.getDefault().post(playEvent);
            }
        });

        holder.downloadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getItem(position).getMusicurl();
                String name = getItem(position).getMusicname();
                Toast.makeText(context,name+"正在下载", Toast.LENGTH_LONG).show();
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(DownloadActivity.rul + url);
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


    static class ViewHolder{
        private TextView musicNameV;
        private ImageView downloadIv;
    }
//    private MusicEntity geturl(String url) {
//        MusicEntity musicEntity = new MusicEntity();
//        musicEntity.setMusicurl(url);
//        return musicEntity;
//    }

}
