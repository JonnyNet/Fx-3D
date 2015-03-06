package com.jonny.Fx3D;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jonny.Fx3D.model.ImgenAdapter;


public class Main extends ActionBarActivity {

    private static long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView grid = (GridView) findViewById(R.id.gridView);
        grid.setAdapter(new ImgenAdapter(this));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent video = new Intent(getApplicationContext(), PreView.class);
                video.putExtra("img", position);
                startActivity(video);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            //super.onBackPressed();
            finish();
            System.exit(0);
        }
        else Toast.makeText(getBaseContext(), "Presione una vez más para salir!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
