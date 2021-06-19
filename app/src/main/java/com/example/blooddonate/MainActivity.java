package com.example.blooddonate;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView logo,iv_top,iv_bottom;
    TextView  welcome;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        iv_top = findViewById(R.id.iv_top);
        iv_bottom = findViewById(R.id.iv_bottom);
        welcome = findViewById(R.id.text_view);

        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.top_wave);
        iv_top.setAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);
        iv_bottom.setAnimation(animation2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, PhoneAuth.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        },4000);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                logo,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f));
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

    }


}