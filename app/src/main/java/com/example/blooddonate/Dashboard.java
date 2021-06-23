package com.example.blooddonate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //decleration

    static final float END_SCALE = 0.7f;
    ViewFlipper img_flipper;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView,bloodGroup;
    WebView webView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        int[] images = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5};

        //menu hooks
        img_flipper = findViewById(R.id.iv_flipper);
        drawerLayout = findViewById(R.id.dashboard_layout);
        navigationView = findViewById(R.id.navigatin_view);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content_view);
        bloodGroup = findViewById(R.id.blood_group);


        //navigation
        navigationDrawer();

        //blood_group icon

       /* for(int i = 0;i<images.length;i++){
            flipperImages(images[i]);
        }*/

        for (int image : images) {
            flipperImages(image);
        }

    }



    private void navigationDrawer() {

        //navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.dashboard_layout);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
                break;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
                break;
            case R.id.blood_bank:
                startActivity(new Intent(getApplicationContext(), LoadingActivity.class));
                finish();
                break;

                //contact
            case R.id.view_camp:
                startActivity(new Intent(getApplicationContext(), LoadingActivity.class));
                finish();
                break;

            case R.id.support:
                startActivity(new Intent(getApplicationContext(), LoadingActivity.class));
                finish();
                break;

                //support us

            //logout
            case R.id.logout:
                logoutSaveData();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), PhoneAuth.class));
                finish();
                break;

            default:
                return true;

        }
        return true;
    }

    private void logoutSaveData() {
        //SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_USER_DETAILS, MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString(USER_PHONE_NUMBER, "NONE");
        //editor.putString(FIRE_AUTH_UID, "NONE");
        //editor.putString(USER_LANGUAGE, USER_LANGUAGE);
        //editor.apply();
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        img_flipper.addView(imageView);
        img_flipper.setFlipInterval(4000);
        img_flipper.setAutoStart(true);

        img_flipper.setInAnimation(this, android.R.anim.slide_out_right);
        img_flipper.setInAnimation(this, android.R.anim.slide_in_left);

    }


    public void callBloodGroup(View view) {

        Intent mainIntent = new Intent(getApplicationContext(),BloodGroup.class);
        startActivity(mainIntent);
        finish();

    }

    public void callDonationType(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),DonationTypes.class);
        startActivity(mainIntent);
        finish();
    }

    public void ElegibilityLayout(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),Elegibility.class);
        startActivity(mainIntent);
        finish();
    }

    public void callProfile(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),Updateprofile.class);
        startActivity(mainIntent);
        finish();
    }

    public void callDonationProcess(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),DonationProcess.class);
        startActivity(mainIntent);
        finish();
    }

    public void callMoreActivity(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),MoreActivity.class);
        startActivity(mainIntent);
        finish();
    }
}