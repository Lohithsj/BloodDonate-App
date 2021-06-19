package com.example.blooddonate;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


public class DonationTypes extends AppCompatActivity {

    ImageView backBttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donation_types);

        backBttn = findViewById(R.id.back_bttn);

        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(mainIntent);
        finish();
    }
}