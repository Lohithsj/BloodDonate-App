package com.example.blooddonate;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MoreActivity extends AppCompatActivity {

    TextView eRaktKosh;
    TextView nbtc,nhp,nib,mohfw,cdsco,nabh,history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //quick links
        eRaktKosh = findViewById(R.id.e_rakt_kosh);
        nbtc = findViewById(R.id.nbtc);
        nhp = findViewById(R.id.nhp);
        nib = findViewById(R.id.nib);
        mohfw = findViewById(R.id.mohfw);
        cdsco = findViewById(R.id.cdsco);
        nabh = findViewById(R.id.nabh);
        history = findViewById(R.id.history);

        //history
        history.setMovementMethod(LinkMovementMethod.getInstance());
        history.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("http://nbtc.naco.gov.in/page/history/"));
                startActivity(in);
            }
        });


        //nabh
        nabh.setMovementMethod(LinkMovementMethod.getInstance());
        nabh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("https://nabh.co/"));
                startActivity(in);
            }
        });


        //cdsco
        cdsco.setMovementMethod(LinkMovementMethod.getInstance());
        cdsco.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("https://cdsco.gov.in/opencms/opencms/en/Home/"));
                startActivity(in);
            }
        });

        //mohfw
        mohfw.setMovementMethod(LinkMovementMethod.getInstance());
        mohfw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mohfw.gov.in/"));
                startActivity(in);
            }
        });


        //nib
        nib.setMovementMethod(LinkMovementMethod.getInstance());
        nib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("http://nib.gov.in/"));
                startActivity(in);
            }
        });


        //nhp
        nhp.setMovementMethod(LinkMovementMethod.getInstance());
        nhp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nhp.gov.in/"));
                startActivity(in);
            }

        });


        //nbtc
        nbtc.setMovementMethod(LinkMovementMethod.getInstance());
        nbtc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("http://nbtc.naco.gov.in/"));
                startActivity(in);
            }

        });

        //eRaktKosh
        eRaktKosh.setMovementMethod(LinkMovementMethod.getInstance());
        eRaktKosh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eraktkosh.in/BLDAHIMS/bloodbank/transactions/bbpublicindex.html"));
                startActivity(in);
            }

        });

    }

    public void callUserDashboardScreen(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),DonationProcess.class);
        startActivity(mainIntent);
        finish();
    }

    public void callDonateHistory(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),LoadingActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(mainIntent);
        finish();
    }

}