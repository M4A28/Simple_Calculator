package com.mohammed.calone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView nameTv = findViewById(R.id.mohammedTv);
        nameTv.animate().rotation(360f).setDuration(1000).start();
        LinearLayout main_layout = findViewById(R.id.intro_layout);
        if(MainActivity.isDayTime()){
            main_layout.setBackgroundColor(getColor(R.color.white));
        }

        if(MainActivity.isFriday()){
            main_layout.setBackgroundResource(R.drawable.grident);
        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//                    r.play();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(Intro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);

    }
}