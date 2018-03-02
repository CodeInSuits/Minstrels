package com.minstrels.neurosky.minstrels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MusicPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        TextView title = (TextView) findViewById(R.id.music_title);
        Intent intent = getIntent();
        String activityType = intent.getStringExtra("Activity");
        title.setText("Playing " + activityType + " music...");

    }
}
