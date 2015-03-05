package com.jonny.mascaras.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonny.mascaras.R;

import static com.jonny.mascaras.R.color.grid;

/**
 * Created by jonny on 21/02/2015.
 */
public class ImgenAdapter extends BaseAdapter {

    private Context context;
    private Integer[] mThumbIds = { R.drawable.ic_cyborg,R.drawable.ic_fish_scuba,R.drawable.ic_cyclops,R.drawable.ic_zombie_1,R.drawable.ic_clown,R.drawable.ic_zombie_2,R.drawable.ic_clown_rotten};
    private String [] mTextIds = {"CYBORG", "FISH SCUBA","CYCLOPS","ZOMBIE 1","EYE CLOWN","ZOMBIE 2","CLOWN ROTTEN"};

    public ImgenAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.video, null);

            ImageView img = (ImageView) convertView.findViewById(R.id.images);
            img.setImageResource(mThumbIds[position]);

            TextView text = (TextView) convertView.findViewById(R.id.texto);
            text.setText(mTextIds[position]);
        }

        return convertView;
    }
}
