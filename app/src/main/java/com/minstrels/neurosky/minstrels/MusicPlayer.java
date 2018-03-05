package com.minstrels.neurosky.minstrels;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.mtechviral.mplaylib.MusicFinder;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends AppCompatActivity {

    ImageView albumArt = null;
    ArrayList entries;
    private ProgressDialog pd;

    TextView songTitle = null;
    TextView songArtist = null;

    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createPlayer();
        }
        else {
            Toast.makeText(this, "Permission not granted. Shutting down.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        pd = new ProgressDialog(MusicPlayer.this);
        pd.setMessage("loading");
        entries = new ArrayList<>();
        loadDataForRadarChart();

        TextView title = (TextView) findViewById(R.id.music_type);
        Intent intent = getIntent();
        String activityType = intent.getStringExtra("Activity");
        title.setText("Playing " + activityType + " music...");


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        else {
            createPlayer();
        }


        final ImageButton playButton = (ImageButton) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPlaying = mediaPlayer.isPlaying();
                if(isPlaying) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.play);
                }
                else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.pause);
                }
                //playOrPause();
            }
        });

        ImageButton stopButton = (ImageButton) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                Intent intent = new Intent(view.getContext(), Home.class);
                startActivity(intent);
            }
        });

    }

    private void loadDataForRadarChart(){
        pd.show();
        entries.add(new com.github.mikephil.charting.data.RadarEntry(4, 0));
        entries.add(new com.github.mikephil.charting.data.RadarEntry(5, 1));
        entries.add(new com.github.mikephil.charting.data.RadarEntry(6, 2));
        entries.add(new com.github.mikephil.charting.data.RadarEntry(7, 3));
        entries.add(new com.github.mikephil.charting.data.RadarEntry(8, 4));
        RadarChart chart = (RadarChart) findViewById(R.id.album_art);

        RadarDataSet dataset_comp1 = new RadarDataSet(entries, "Initial Value");
        dataset_comp1.setColor(Color.BLUE);
        dataset_comp1.setDrawFilled(true);

        ArrayList dataSets = new ArrayList();
        dataSets.add(dataset_comp1);
        ArrayList labels = new ArrayList();
        labels.add("Target");
        labels.add("Passing");
        labels.add("Skills");
        labels.add("Dribbling");
        labels.add("Penalty");


        RadarData data = new RadarData(dataSets);
        chart.setData(data);

        chart.invalidate();
        chart.animate();
        pd.hide();
    }

    private void createPlayer() {
        MusicFinder musicFinder = new MusicFinder(getContentResolver());
        musicFinder.prepare();
        Toast.makeText(this, "Retrieving all songs", Toast.LENGTH_LONG).show();
        List<MusicFinder.Song> songs = musicFinder.getAllSongs();

        Intent intent = getIntent();
        String activityType = intent.getStringExtra("Activity");

        switch (activityType) {
            case "workout":
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(0).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "size" + songs.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "sleep":
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(1).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "size" + songs.get(1).getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "study":
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(2).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "size" + songs.get(2).getTitle(), Toast.LENGTH_SHORT).show();
                break;
            default:
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(2).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "size" + songs.get(2).getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

}
