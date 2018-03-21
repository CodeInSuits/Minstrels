package com.minstrels.neurosky.minstrels;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mtechviral.mplaylib.MusicFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayer extends AppCompatActivity {

    private ProgressDialog pd;

    MediaPlayer mediaPlayer = new MediaPlayer();


    private RadarChart mChart;
    private SparseIntArray factors = new SparseIntArray(5);
    private SparseIntArray scores = new SparseIntArray(5);
    private ArrayList<RadarEntry> entries = new ArrayList<>();
    private ArrayList<IRadarDataSet> dataSets = new ArrayList<>();
    private SparseIntArray currentScores = new SparseIntArray(5);
    private ArrayList<RadarEntry> currentEntries = new ArrayList<>();
    private ArrayList<IRadarDataSet> currentDataSets = new ArrayList<>();
    RadarDataSet currentDataSet;
    View view;

    private TextView textView;

    int counter = 0;

    Handler h = new Handler();
    int delay = 10*1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;

    private ProgressBar progressBar;

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

        textView = (TextView) findViewById(R.id.textView);


        loadDataForRadarChart();

        TextView title = (TextView) findViewById(R.id.music_type);
        Intent intent = getIntent();
        String activityType = intent.getStringExtra("Activity");
        title.setText("Playing " + activityType + " music...");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        view = findViewById(R.id.progressBar_cyclic);
        view.setVisibility(View.GONE);


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
                Toast.makeText(this, "Playing workout music", Toast.LENGTH_SHORT).show();
                break;
            case "sleep":
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(1).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "Playing sleep music", Toast.LENGTH_SHORT).show();
                break;
            case "study":
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(2).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "Playing study music", Toast.LENGTH_SHORT).show();
                break;
            default:
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(this, songs.get(2).getURI());
                mediaPlayer.start();
                Toast.makeText(this, "Playing random music", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

    private void loadDataForRadarChart(){
        pd.show();
        factors.append(1, R.string.Delta);
        factors.append(2, R.string.Theta);
        factors.append(3, R.string.Alpha);
        factors.append(4, R.string.Beta);
        factors.append(5, R.string.Gamma);
        mChart = (RadarChart) findViewById(R.id.radar_chart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextSize(11f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mFactors = new String[]{getString(factors.get(1)), getString(factors.get(2)),
                    getString(factors.get(3)), getString(factors.get(4)), getString(factors.get(5))};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFactors[(int) value % mFactors.length];
            }
        });
        YAxis yAxis = mChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(50f);
        yAxis.setTextSize(12f);
        yAxis.setLabelCount(5, false);
        yAxis.setDrawLabels(false);

        mChart.getLegend().setEnabled(true);
        mChart.getDescription().setEnabled(false);
        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        scores.clear();
        // Or hardcode some test data:
        scores.append(1, 18);
        scores.append(2, 26);
        scores.append(3, 35);
        scores.append(4, 40);
        scores.append(5, 60);

        currentScores.clear();
        // Or hardcode some test data:
        currentScores.append(1, 38);
        currentScores.append(2, 23);
        currentScores.append(3, 42);
        currentScores.append(4, 23);
        currentScores.append(5, 22);

        drawChart();
        pd.hide();
    }



    private void drawChart() {
        entries.clear();

        for (int i = 1; i <= 5; i++) {
            entries.add(new RadarEntry(scores.get(i)));
        }

        currentEntries.clear();
        for (int i = 1; i <= 5; i++) {
            currentEntries.add(new RadarEntry(currentScores.get(i)));
        }


        RadarDataSet dataSet = new RadarDataSet(entries, "Target range");
        dataSet.setColor(Color.rgb(103, 255, 129));
        dataSet.setFillColor(Color.rgb(103, 255, 129));
        dataSet.setDrawFilled(true);

        currentDataSet = new RadarDataSet(currentEntries, "Current range");
        currentDataSet.setColor(Color.rgb(235, 101, 101));
        currentDataSet.setFillColor(Color.rgb(235, 101, 101));
        currentDataSet.setDrawFilled(true);

        dataSets.add(dataSet);
        dataSets.add(currentDataSet);

        RadarData data = new RadarData(dataSets);
        data.setValueTextSize(11f);

        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }

        });
        mChart.setData(data);
        mChart.invalidate();
    }

    private void updateCurrentScore() {
        Random generator = new Random();
        currentScores.clear();
        for (int i = 1; i <= 5; i++) {
            currentScores.append(i, generator.nextInt(60));
        }
        if(counter == 5) {
            currentScores.put(5, 60);
        }
        counter++;
    }


    private void updateCurrentData() {
        dataSets.remove(currentDataSet);

        currentEntries.clear();
        for (int i = 1; i <= 5; i++) {
            currentEntries.add(new RadarEntry(currentScores.get(i)));
        }
        currentDataSet = new RadarDataSet(currentEntries, "Current range");
        currentDataSet.setColor(Color.rgb(235, 101, 101));
        currentDataSet.setFillColor(Color.rgb(235, 101, 101));
        currentDataSet.setDrawFilled(true);
        dataSets.add(currentDataSet);
        mChart.invalidate();
    }


    @Override
    protected void onResume() {
        //start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {
                view.setVisibility(View.VISIBLE);
                view.postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Reading new data from headset", Toast.LENGTH_SHORT).show();
                        view.setVisibility(View.GONE);
                    }
                }, 3000);
                updateCurrentScore();
                updateCurrentData();
                int target = 60;
                double current = currentScores.get(5);
                int progressStatus = (int)(current/target*100);
                if(progressStatus == 100) {
                    mediaPlayer.pause();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MusicPlayer.this);
                    builder1.setMessage("Goal reached! You are ready to work out!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Go back to home",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            });

                    builder1.setNegativeButton(
                            "Keep listening to music",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    mediaPlayer.start();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                textView.setText(progressStatus+"/"+progressBar.getMax());
                progressBar.setProgress(progressStatus);
                Toast.makeText(getApplicationContext(), "Updated chart", Toast.LENGTH_SHORT).show();
                runnable=this;
                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

}
