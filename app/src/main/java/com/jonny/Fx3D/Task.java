package com.jonny.Fx3D;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


import com.jonny.Fx3D.Touch.MoveGestureDetector;
import com.jonny.Fx3D.Touch.RotateGestureDetector;
import com.jonny.Fx3D.Touch.ShoveGestureDetector;

import java.io.IOException;

public class Task extends Activity  implements TextureView.SurfaceTextureListener{

    private GestureDetector mGestureDetector;
    private ImageButton padlock;
    private boolean sw = true;
    private TextureView mTextureView;
    private View.OnTouchListener t,u;
    private MediaPlayer mMediaPlayer;

    private int mVideoWidth;
    private int mVideoHeight;

    private float mScaleFactor = 1f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private float mAlpha = 255;

    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;
    private ShoveGestureDetector mShoveDetector;

    String video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        int img = getIntent().getIntExtra("img",0);
        String[] videos = {"anim_cyborg.mp4","agua.mp4","anim_cyclops.mp4","anim_frankenstein.mp4","anim_cyclops.mp4","generic_eyeball.mp4","gusamos.mp4"};
        video = videos[img];
        calculateVideoSize();
        initView();
    }

    private void initView(){
        mScaleDetector 	= new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
        mMoveDetector 	= new MoveGestureDetector(getApplicationContext(), new MoveListener());
        mShoveDetector 	= new ShoveGestureDetector(getApplicationContext(), new ShoveListener());
        mGestureDetector = new GestureDetector(this, new MySimpleOnGestureListener());

        padlock = (ImageButton) findViewById(R.id.padlock);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);


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
                mScaleDetector.onTouchEvent(event);
                mRotateDetector.onTouchEvent(event);
                mMoveDetector.onTouchEvent(event);
                mShoveDetector.onTouchEvent(event);

                float scaledImageCenterX = (mVideoHeight * mScaleFactor) / 2;
                float scaledImageCenterY = (mVideoWidth * mScaleFactor) / 2;
                Matrix mMatrix = new Matrix();
                mMatrix.reset();
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
                mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);

                TextureView view = (TextureView) v;
                view.setTransform(mMatrix);
                view.setAlpha(mAlpha);
                return true;
            }
        };

        mTextureView.setOnTouchListener(u);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }



    public void block(View v){
        if(sw){
            mTextureView.setOnTouchListener(t);
            padlock.setImageResource(R.drawable.ic_locked);
            sw = false;
        }else{
            mTextureView.setOnTouchListener(u);
            padlock.setImageResource(R.drawable.ic_unlocked);
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

    private void calculateVideoSize() {
        try {
            AssetFileDescriptor afd = getAssets().openFd(video);
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(
                    afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String height = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String width = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            mVideoHeight = Integer.parseInt(height);
            mVideoWidth = Integer.parseInt(width);

        } catch (IOException | NumberFormatException e) {
            Log.d("", e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface1, int width, int height) {
        Surface surface = new Surface(surface1);

        try {
            AssetFileDescriptor afd = getAssets().openFd(video);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IllegalArgumentException | SecurityException | IOException | IllegalStateException e) {
            Log.d("", e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mTextureView == null)
                return false;
            if (padlock.getVisibility() == View.GONE) {
                mostrar();
            }
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            return true;
        }
    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;
            return true;
        }
    }

    private class ShoveListener extends ShoveGestureDetector.SimpleOnShoveGestureListener {
        @Override
        public boolean onShove(ShoveGestureDetector detector) {
            mAlpha += detector.getShovePixelsDelta();
            if (mAlpha > 255)
                mAlpha = 255;
            else if (mAlpha < 0)
                mAlpha = 0;

            return true;
        }
    }
}
