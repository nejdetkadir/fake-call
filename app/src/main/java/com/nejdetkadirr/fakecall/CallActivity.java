package com.nejdetkadirr.fakecall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

public class CallActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable runnable;
    TextView timeText;
    int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Intent intent = getIntent();
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        TextView name = findViewById(R.id.nameTextView);
        timeText = findViewById(R.id.timerTextView);
        name.setText(intent.getStringExtra("name"));
        number = 0;
    }

    public void accept(View view) {
        mediaPlayer.stop();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (number < 10) {
                    timeText.setText( "00:0" + number);
                } else if (number > 10 && number < 60) {
                    timeText.setText( "00:" + number);
                } else if (number > 59 && number % 60 < 10) {
                    int second = number / 60;
                    timeText.setText( second+":0" + number % 60);
                } else {
                    int second = number / 60;
                    timeText.setText( second+":" + number % 60);
                }


                number++;
                handler.postDelayed(runnable,1000);
            }
        };
        handler.post(runnable);

    }

    public void decline(View view) {
        mediaPlayer.stop();
    }
}
