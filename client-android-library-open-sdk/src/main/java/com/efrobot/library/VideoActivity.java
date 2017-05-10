package com.efrobot.library;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.VideoView;

import java.lang.ref.WeakReference;

public class VideoActivity extends Activity {
    public static final String TAG = "VideoActivity";
    public static BaseHandler mBaseHandler;
    private VideoView videoView;
    private static VideoActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        RobotManager.mVideoActivity=this;
        initView();
        playFace();
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.video);
        mBaseHandler = new BaseHandler(this);
    }

    /**
     * 播放表情
     */
    public void playFace() {
        Bundle extras = getIntent().getExtras();
        final String path = extras.getString("path");
        final int type=extras.getInt("type");
        //播放视频
        if (mBaseHandler != null) {
            mBaseHandler.removeMessages(1);
        }
        int visibility = videoView.getVisibility();
        if (visibility != View.VISIBLE) {
            videoView.setVisibility(View.VISIBLE);
        }
        fullScreen();

        final Uri mUri = Uri.parse(path);// "raw/kun.mp4");
        videoView.setVideoURI(mUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                mp.start();
                if(type==0){
                    mp.setLooping(true);
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(type==0){
                    videoView.setVideoPath(path);
                    videoView.start();
                }else {
                   mBaseHandler.sendEmptyMessageDelayed(1, 300);
                }

            }
        });

    }
    @Override
    protected void onResume() {
        fullScreen();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RobotManager.mVideoActivity != null) {
            RobotManager.mVideoActivity.finish();
            RobotManager.mVideoActivity = null;
        }
    }
    public void stop() {
        videoView.stopPlayback();
        finish();
    }

    private static class BaseHandler extends Handler {
        private final WeakReference<VideoActivity> mObjects;

        public BaseHandler(VideoActivity videoActivity) {
            mObjects = new WeakReference<VideoActivity>(videoActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoActivity mObject = mObjects.get();
            if (mObject != null)
                mObject.handleMessage(msg);
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                playFace();
                break;

            case 1:
                stop();
                break;
        }
    }

    /**
     * 全屏显示方法
     */
    public void fullScreen() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        if (videoView != null)
            videoView.setSystemUiVisibility(uiFlags);
    }
}
