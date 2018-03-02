package com.minstrels.neurosky.minstrels;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MusicPlayer extends AppCompatActivity {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        TextView title = (TextView) findViewById(R.id.music_title);
        Intent intent = getIntent();
        String activityType = intent.getStringExtra("Activity");
        title.setText("Playing " + activityType + " music...");

        Button endButton = (Button) findViewById(R.id.end_session);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Home.class);
                startActivity(intent);
            }
        });

    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list

            //https://code.tutsplus.com/tutorials/create-a-music-player-on-android-project-setup--mobile-22764
            //https://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
            //https://code.tutsplus.com/tutorials/create-a-music-player-on-android-user-controls--mobile-22787
            //musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
}
