package com.minstrels.neurosky.minstrels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        //workout button
        Button workoutButton = (Button) findViewById(R.id.workout);
        workoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                intent.putExtra("Activity", "workout");
                startActivity(intent);
            }
        });

        Button studyButton = (Button) findViewById(R.id.study);
        studyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                intent.putExtra("Activity", "study");
                startActivity(intent);
            }
        });

        Button sleepButton = (Button) findViewById(R.id.sleep);
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                intent.putExtra("Activity", "sleep");
                startActivity(intent);
            }
        });

    }
}
