package com.jonny.mascaras;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.jonny.mascaras.model.Video;

public class Task extends Activity{

    private Video videoView;
    private int x,y;
    private AbsoluteLayout.LayoutParams mRootParam;

    private RelativeLayout layout;
    private ImageButton padlock;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    private boolean sw = true;

    View.OnTouchListener t,u;

    private int r = 0;
    static final int MIN_WIDTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        int img = getIntent().getIntExtra("img",0);
        String[] videos = {"anim_cyborg","generic_eyeball","anim_cyclops","anim_frankenstein","anim_cyclops","generic_eyeball","zombie_mugshot"};

        videoView = (Video) findViewById(R.id.videoView);
        layout = (RelativeLayout) findViewById(R.id.cont);
        padlock = (ImageButton) findViewById(R.id.padlock);

        mRootParam = (AbsoluteLayout.LayoutParams) layout.getLayoutParams();

        Uri path = Uri.parse("android.resource://com.jonny.mascaras/raw/"+videos[img]);
        videoView.setVideoURI(path);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        mScaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureListener());
        mGestureDetector = new GestureDetector(this, new MySimpleOnGestureListener());


        t = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        };



        u = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                mScaleGestureDetector.onTouchEvent(event);
                int X = (int) ((int) event.getRawX()*0.7);
                int Y = (int) ((int) event.getRawY()*0.7);
                if(event.getPointerCount() == 1){
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            AbsoluteLayout.LayoutParams Params = (AbsoluteLayout.LayoutParams) v
                                    .getLayoutParams();
                            x = X - Params.x;
                            y = Y - Params.y;
                            return true;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) v
                                    .getLayoutParams();

                            layoutParams.x = X - x;
                            layoutParams.y = Y - y;
                            v.setLayoutParams(layoutParams);
                            break;
                        }
                    }
                }else{


                }
                return false;
            }
        };

        layout.setOnTouchListener(u);


    }

    private void rotate() {
        layout.setRotation(1f);
    }

    @Override
    protected void onResume() {
        videoView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoView.pause();
        super.onPause();
    }

    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (videoView == null)
                return false;
            if (padlock.getVisibility() == View.GONE) {
                mostrar();
            }
            rotate();
            return true;
        }
    }

    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private int mW, mH;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mW *= detector.getScaleFactor();
            mH *= detector.getScaleFactor();

            if (mW < MIN_WIDTH) {
                mW = videoView.getWidth();
                mH = videoView.getHeight();
            }

            videoView.setFixedVideoSize(mW, mH);
            mRootParam.width = mW;
            mRootParam.height = mH;
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    public void block(View v){
        if(sw){
            layout.setOnTouchListener(t);
            padlock.setImageResource(R.drawable.ic_locked);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            sw = false;
        }else{
            layout.setOnTouchListener(u);
            padlock.setImageResource(R.drawable.ic_unlocked);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            sw = true;
        }
    }

    private void mostrar(){
        padlock.setVisibility(View.VISIBLE);
        padlock.postDelayed(new Runnable() {
            @Override
            public void run() {
                padlock.setVisibility(View.GONE);
            }
        },3000);
    }
}
