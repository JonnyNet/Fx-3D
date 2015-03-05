package com.jonny.mascaras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class PreView extends Activity {

    private Integer[] mThumbIds = { R.drawable.ic_cyborg,R.drawable.ic_fish_scuba,R.drawable.ic_cyclops,R.drawable.ic_zombie_1,R.drawable.ic_clown,R.drawable.ic_zombie_2,R.drawable.ic_clown_rotten};
    private String [] mTextIds = {"CYBORG", "FISH SCUBA","CYCLOPS","ZOMBIE 1","EYE CLOWN","ZOMBIE 2","CLOWN ROTTEN"};

    private ImageView mascara;
    private TextView titulo;
    private int img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_view);
        img = getIntent().getIntExtra("img",0);

        mascara = (ImageView) findViewById(R.id.mascara);
        titulo = (TextView) findViewById(R.id.title);
        titulo.setText(mTextIds[img]);

        mascara.setImageResource(mThumbIds[img]);


    }

    public void volver(View e){
        Intent v = new Intent(this,Main.class);
        startActivity(v);
        finish();
    }

    public void ver(View v){
        Intent video = new Intent(this,Task.class);
        video.putExtra("img", img);
        startActivity(video);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            volver(null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
