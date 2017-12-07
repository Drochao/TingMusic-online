package com.dntz.tingmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.adapter.PlaylistAdapter;
import com.dntz.tingmusic.database.DBManager;
import com.dntz.tingmusic.entity.MusicInfo;
import com.dntz.tingmusic.entity.PlayListInfo;
import com.dntz.tingmusic.receiver.PlayerManagerReceiver;
import com.dntz.tingmusic.service.MusicPlayerService;
import com.dntz.tingmusic.util.Constant;
import com.dntz.tingmusic.util.MyMusicUtil;
import com.dntz.tingmusic.view.MusicPopMenuWindow;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

public class PlaylistActivity extends PlayBarBaseActivity {

    private static final String TAG = "PlaylistActivity";
    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<MusicInfo> musicInfoList;
    private PlayListInfo playListInfo;
    private Toolbar toolbar;
    private TextView noneTv;//没有歌单时现实的TextView
    private DBManager dbManager;
    private UpdateReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playListInfo = getIntent().getParcelableExtra("playlistInfo");


        toolbar = (Toolbar) findViewById(R.id.activity_playlist_toolbar);
        setSupportActionBar(toolbar);
        //设置界面返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(playListInfo.getName());
        }

        dbManager = DBManager.getInstance(this);
        musicInfoList = dbManager.getMusicListByPlaylist(playListInfo.getId());

        initView();

        register();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.activity_playlist_rv);
        playlistAdapter = new PlaylistAdapter(this,playListInfo,musicInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(playlistAdapter);

        noneTv = (TextView)findViewById(R.id.activity_playlist_none_tv);
       //判断歌曲列表的数据 如果为0这将noneTv设置为可见
        if (playListInfo.getCount() == 0){
            recyclerView.setVisibility(View.GONE);
            noneTv.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            noneTv.setVisibility(View.GONE);
        }


        playlistAdapter.setOnItemClickListener(new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onOpenMenuClick(int position) {
                MusicInfo musicInfo = musicInfoList.get(position);
                showPopFormBottom(musicInfo);
            }

            @Override
            public void onDeleteMenuClick(View swipeView, int position) {
                MusicInfo musicInfo = musicInfoList.get(position);
                final int curId = musicInfo.getId();//删除音乐ID
                final int musicId = MyMusicUtil.getIntShared(Constant.KEY_ID);//当前音乐ID
                //从列表移除
                int ret = dbManager.removeMusicFromPlaylist(musicInfo.getId(),playListInfo.getId());
                if (ret > 0){
                    Toast.makeText(PlaylistActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PlaylistActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
                if (curId == musicId) {
                    //移除的是当前播放的音乐
                    Intent intent = new Intent(MusicPlayerService.PLAYER_MANAGER_ACTION);//向MusicPlayerService发送Intent信息
                    intent.putExtra(Constant.COMMAND, Constant.COMMAND_STOP);//信息内容
                    sendBroadcast(intent);
                }
                musicInfoList = dbManager.getMusicListByPlaylist(playListInfo.getId());
                playlistAdapter.updateMusicInfoList(musicInfoList);
                //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                ((SwipeMenuLayout) swipeView).quickClose();
            }

        });

        // 当点击外部空白处时，关闭正在展开的侧滑菜单
        findViewById(R.id.activity_playlist).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });

    }

    public void showPopFormBottom(MusicInfo musicInfo) {
        MusicPopMenuWindow menuPopupWindow = new MusicPopMenuWindow(PlaylistActivity.this,musicInfo,findViewById(R.id.activity_playlist),Constant.ACTIVITY_MYLIST);
//      设置Popupwindow显示位置（从底部弹出）
        menuPopupWindow.showAtLocation(findViewById(R.id.activity_playlist), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = PlaylistActivity.this.getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        getWindow().setAttributes(params);

        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        menuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });

        menuPopupWindow.setOnDeleteUpdateListener(new MusicPopMenuWindow.OnDeleteUpdateListener() {
            @Override
            public void onDeleteUpdate() {
                musicInfoList = dbManager.getMusicListByPlaylist(playListInfo.getId());
                playlistAdapter.updateMusicInfoList(musicInfoList);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }


    private void register() {
        try {
            if (mReceiver != null) {
                this.unRegister();
            }
            mReceiver = new UpdateReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(PlayerManagerReceiver.ACTION_UPDATE_UI_ADAPTER);
            this.registerReceiver(mReceiver, intentFilter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void unRegister() {
        try {
            if (mReceiver != null) {
                this.unregisterReceiver(mReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            playlistAdapter.notifyDataSetChanged();
        }
    }
}
