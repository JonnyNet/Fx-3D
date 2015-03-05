package com.jonny.mascaras;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;


public class Splash extends ActionBarActivity {

private static final long SPLASH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent video = new Intent(getApplicationContext(),Main.class);
                startActivity(video);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,SPLASH);
    }
}
