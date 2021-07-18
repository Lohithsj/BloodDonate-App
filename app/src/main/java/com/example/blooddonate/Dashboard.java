package com.example.blooddonate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonate.HomeAdapter.WithDescAdapter;
import com.example.blooddonate.HomeAdapter.WithDescHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static final String LOGIN_USER_DETAILS = "loginUserDetails";
    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static final String FIRE_AUTH_UID = "FIRE_AUTH_UID";
    private static final String LSJ = "Dashbord6+";


    private com.example.blooddonate.HomeAdapter.DonarCount cAdapter;
    private WithDescAdapter cdAdapter;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;

    RecyclerView donarCountRecycler;

    //firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //decleration

    static final float END_SCALE = 0.7f;
    ViewFlipper img_flipper;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView,bloodGroup;

    String donar_count_str,userPhoneNumber, currentDateTime,donar_phone_num ,vig_name,dis_name,state_name,vig_donar_count,donar_count_dis,donar_count_state;
    int dist_donar_count,state_donar_count,donar_count;

    //collection refrence
    CollectionReference colRef;

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

        donarCountRecycler=findViewById(R.id.donar_count_recycler);


        //Instantiate firebase variables

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initialise counters
        dist_donar_count = 0;
        state_donar_count = 0;
        donar_count = 0;

        //Get current date and Time to set the updated_date variable in firebase

        currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Instantiate the collection reference for the required collection data to fetch from firebase

        colRef = fStore.collection("UserDetails");

        //getUserDetailsFromSharedPreferences();

        userPhoneNumber = fAuth.getCurrentUser().getPhoneNumber().substring(1);


        Log.d(LSJ, "User phone number "+ userPhoneNumber);

        donar_phone_num = userPhoneNumber;
        //Log.d(VGV, "onCreate: "+famr_phone_num);



        //navigation
        navigationDrawer();


        //getting user profile
        getUserProfile();

       /* for(int i = 0;i<images.length;i++){
            flipperImages(images[i]);
        }*/

        for (int image : images) {
            flipperImages(image);
        }

    }

    //donar count details
    private void getUserProfile(){

        DocumentReference docRef = fStore.collection("UserDetails").document(donar_phone_num);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d(LSJ,  "getUserProfile:"+donar_phone_num);

                    vig_name = documentSnapshot.getString("address_village");
                    dis_name=documentSnapshot.getString("address_city");
                    dis_name=dis_name.toLowerCase();
                    state_name=documentSnapshot.getString("address_state");
                    state_name=state_name.toLowerCase();
                    Log.d(LSJ, "getUserProfile 1"+ vig_name+":"+dis_name+":"+state_name);

                    // Call getDonarCountPerVillage

                    getDonarCountPerVillage();


                }else{

                    // log error if details does not exists

                    Log.d(LSJ, "Error getting Farmer count from database");

                    Toast.makeText(Dashboard.this,"Something went wrong. Kindly contact your Location Admin for assistance",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getDonarCountPerVillage() {
        DocumentReference docRef = fStore.collection("address_village").document(vig_name.toLowerCase());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    vig_donar_count = documentSnapshot.getString("donarCount");
                    if(vig_donar_count == null){
                        vig_donar_count ="0";
                    }
                    Log.d(LSJ, "Donars count/Village "+ vig_donar_count);
                    // Call getDonarCountPerDistrict

                    getDonarCountPerDistrict();

                }else{

                    // log error if details does not exists

                    Log.d(LSJ, "Error getting Donar count per Village from database");
                    vig_donar_count="0";
                    getDonarCountPerDistrict();

                    // Toast.makeText(UserDashboard.this,"Something went wrong. Kindly contact your Location Admin for assistance",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getDonarCountPerDistrict() {
        // get Farmers count per district from Database

        colRef.whereEqualTo("address_city",dis_name).

                get().

                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete (@NonNull Task< QuerySnapshot > task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Data initialisation
                                donar_count_str = (String) document.getData().get("donarCount");
                                if(donar_count_str == null){
                                    donar_count_str ="0";
                                }
                                dist_donar_count = dist_donar_count + Integer.parseInt(donar_count_str);


                            }
                            donar_count_dis = Integer.toString(dist_donar_count);

                            Log.d(LSJ, "Donarss count/district task successfull" + dist_donar_count);

                            // Call getDonarCountPerState

                            getDonarCountPerState();


                        } else {
                            Log.d(LSJ, "Error getting donar count per District: ", task.getException());
                            donar_count_dis="0";
                            getDonarCountPerState();
                            //Toast.makeText(UserDashboard.this, "Something went wrong. Kindly contact your Location Admin for assistance", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getDonarCountPerState() {

        // get Farmers count per state from Database

        colRef.whereEqualTo("address_state", state_name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // Data initialisation
                        donar_count_str = (String)document.getData().get("donarCount");
                        if(donar_count_str == null){
                            donar_count_str ="0";
                        }
                        state_donar_count=state_donar_count+Integer.parseInt(donar_count_str);

                    }
                    donar_count_state=Integer.toString(state_donar_count);
                    Log.d(LSJ, "Farmers count/state task successfull"+ state_donar_count);
                    donarDetailsRecycler();


                } else {
                    Log.d(LSJ, "Error getting Farmers count per state: ", task.getException());
                    donar_count_state="0";
                    donarDetailsRecycler();
                    // Toast.makeText(UserDashboard.this, "Something went wrong. Kindly contact your Location Admin for assistance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void donarDetailsRecycler() {

        //All Gradients
        int[] gradient_color1 = {0xff7adccf, 0xff7adccf};
        int[] gradient_color2 = {0xffd4cbe5, 0xffd4cbe5};
        int[] gradient_color3 = {0xfff7c59f, 0xFFf7c59f};

        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient_color1);
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient_color2);
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient_color3);

        Log.d(LSJ, "farmerDetailsRecycler: "+vig_name+":"+vig_donar_count+":"+dis_name+":"+donar_count_dis+":"+state_name+":"+donar_count_state);

        ArrayList<WithDescHelperClass> donarHelperClasses = new ArrayList<>();
        donarHelperClasses.add(new WithDescHelperClass(gradient1, R.drawable.village_icon, vig_name.toUpperCase(),vig_donar_count));
        donarHelperClasses.add(new WithDescHelperClass(gradient2, R.drawable.city_icon, dis_name.toUpperCase(),donar_count_dis));
        donarHelperClasses.add(new WithDescHelperClass(gradient3, R.drawable.state_icon, state_name.toUpperCase(),donar_count_state));


        donarCountRecycler.setHasFixedSize(true);
        cdAdapter = new WithDescAdapter(donarHelperClasses);
        donarCountRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        donarCountRecycler.setAdapter(cdAdapter);

        //intent = new Intent(getApplicationContext(), ComingSoon.class);

        /*cdAdapter.setOnCardClickListener(new CategoriesWithDescAdapter.OnCardClickListener() {
            @Override
            public void OnCardClick(int position) {
                String pos= Integer.toString(position);
                intent.putExtra("position", pos);
                startActivity(intent);
            }
        });
        */

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

        /*if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }*/
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
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_USER_DETAILS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PHONE_NUMBER, "NONE");
        editor.putString(FIRE_AUTH_UID, "NONE");
        editor.apply();
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
        Intent mainIntent = new Intent(getApplicationContext(),ProfileActivity.class);
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

    //user deatails
   /* public void getUserDetailsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginUserDetails", MODE_PRIVATE);
        userPhoneNumber = sharedPreferences.getString("USER_PHONE_NUMBER", "");
        fireAuthUID = sharedPreferences.getString("FIRE_AUTH_UID", "");
    }*/

}