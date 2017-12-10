package com.dntz.tingmusic.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by 72408 on 2017/12/6.
 */

public class OnlineMusicAdapater extends BaseAdapter {
    private ArrayList<MusicEntity> dataList;
    private Context context;
    private LinearLayout content;
    String urlMusicc = "http://m10.music.126.net/20171208230310/4c4e98bfb8473441a9d1e253f1696fdf/ymusic/68f5/d196/76fb/ff1f2fcfcfcf057fbea4caaee5b99862.mp3";

    public OnlineMusicAdapater(ArrayList<MusicEntity> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position).getMusicname();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView = LayoutInflater.from(context).inflate(R.layout.online_music_item, null);
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.online_music_item,parent,false);
            holder = new ViewHolder();
            holder.musicNameV = (TextView)convertView.findViewById(R.id.online_music_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.musicNameV.setText(dataList.get(position).getMusicname());
        return convertView;

    }
    static class ViewHolder{
        TextView musicNameV;
    }


}
