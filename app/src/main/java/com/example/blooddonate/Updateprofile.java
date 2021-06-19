package com.example.blooddonate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Updateprofile extends AppCompatActivity {

    public static final String LOGIN_USER_DETAILS = "loginUserDetails";
    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static final String FIRE_AUTH_UID = "FIRE_AUTH_UID";
    public static final String LOCATION_ADMIN = "false";

    //Variables
    Button next;
    TextView titleText, slideText;


    ImageView backBtn;
    ImageView selectDate;
    Calendar calendar;

    //DOB
    private DatePicker datePicker;
    private int dayOfMonth;
    private int year;
    private int month;
    int dob_year;

    // Firebase Variables
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userID, userPhoneNumber,fsUserID,currentDateTime;

    RadioGroup gender_group;
    RadioButton selectedGender;
    TextView  age_picker;
    TextInputLayout first_name, last_name, email,dob_layout;
    TextInputLayout Address_line1, Address_line2, Address_village, Address_zipcode;
    TextInputLayout Address_city, Address_state, phone_number;
    Button profile_edit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_updateprofile);

        //hooks
        backBtn =  findViewById(R.id.general_back_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        userPhoneNumber= firebaseAuth.getCurrentUser().getPhoneNumber();
        fsUserID = userPhoneNumber.substring(1);

        //Hooks for Getting Data

        selectDate = findViewById(R.id.select_Date);
        first_name = findViewById(R.id.signup_first_name);
        last_name = findViewById(R.id.signup_last_name);
        email = findViewById(R.id.signup_email);
        gender_group = findViewById(R.id.signup_gender_group);
        age_picker = findViewById(R.id.display_Date);
        //dob_layout  = findViewById(R.id.signup_dob_layout);

        Address_line1 = findViewById(R.id.signup_Address_line1);
        Address_line2 = findViewById(R.id.signup_Address_line2);
        Address_village = findViewById(R.id.signup_Address_village);
        Address_zipcode = findViewById(R.id.signup_Address_zipcode);
        Address_city = findViewById(R.id.signup_Address_city);
        Address_state = findViewById(R.id.signup_Address_state);
        phone_number = findViewById(R.id.signup_phone_number);
        profile_edit_button = findViewById(R.id.profile_next_button);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dob_year = year;

        //backBtn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(mainIntent);
                finish();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Updateprofile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String dayofmonth=Integer.toString(dayOfMonth);
                        String mnth=Integer.toString(month);
                        if(dayofmonth.length() == 1){
                            dayofmonth="0"+dayofmonth;
                        }
                        if(mnth.length() == 1){
                            mnth="0"+mnth;
                        }

                        String start_date = dayofmonth+"/"+mnth+"/"+year;
                        dob_year = year;
                        age_picker.setText(start_date);
                    }
                },year,month,dayOfMonth);
                datePickerDialog.show();
            }
        });

    }

    ///nextButton

    public void callNextScreen(View view) {

        if (!validateFirstName() | !validateLastName() | !validateGender() | !validateAge() | !validateAddaress() | !validateZipcode() ) {
            return;
        }
        //variable from this page
        String _first_name = first_name.getEditText().getText().toString().trim();
        String _last_name = last_name.getEditText().getText().toString().trim();
        String _email = email.getEditText().getText().toString().trim();
        selectedGender = findViewById(gender_group.getCheckedRadioButtonId());
        String gender = selectedGender.getText().toString();

        String dateOfBirth = age_picker.getText().toString();



        //Variables address
        String address_line1= Address_line1.getEditText().getText().toString().trim();
        String address_line2= Address_line2.getEditText().getText().toString().trim();
        String address_village= Address_village.getEditText().getText().toString().trim();
        String address_zipcode= Address_zipcode.getEditText().getText().toString().trim();
        String address_city= Address_city.getEditText().getText().toString().trim();
        String address_state= Address_state.getEditText().getText().toString().trim();



        //Intent intent = new Intent(getApplicationContext(), Dashboard.class);

        //Add variables to intent so that it can be passed to next screen

        //intent.putExtra("firstName", _first_name);
        //intent.putExtra("lastName", _last_name);
        //intent.putExtra("email", _email);
        //intent.putExtra("gender", gender);
        //intent.putExtra("dateOfBirth", dateOfBirth);





        final DocumentReference docRefUserDetails = fStore.collection("UserDetails").document(fsUserID);


        Map<String,Object> userDetails = new HashMap<>();
        userDetails.put("firstName",_first_name);
        userDetails.put("lastName",_last_name);
        userDetails.put("email",_email );
        userDetails.put("gender",gender );
        userDetails.put("dateOfBirth",dateOfBirth);
        userDetails.put("address_line1",address_line1 );
        userDetails.put("address_line2",address_line2 );
        userDetails.put("address_village",address_village );
        userDetails.put("address_zipcode",address_zipcode );
        userDetails.put("address_city",address_city );
        userDetails.put("address_state",address_state );
        userDetails.put("userPhoneNumber",userPhoneNumber);
        userDetails.put("location_admin","false" );
        userDetails.put("registered_by","self" );
        userDetails.put("registered_on",currentDateTime);

        docRefUserDetails.set(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Log.d(SMK, " UserDetails Inserted calling save data");

                    saveData();

                    //Log.d(SMK, " UserDetails Inserted calling DonarCountDetails");
                    //donarCountDetails();

                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Updateprofile.this,"Data is not inserted",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //validateFirstName
    private boolean validateFirstName() {
        String val = first_name.getEditText().getText().toString().trim();


        String checkspaces = "^[aA-zZ]{3,20}$";


        if (val.isEmpty()) {
            first_name.setError("Name can not be empty");
            return false;
        } else if (val.length() > 20) {
            first_name.setError("Name is too large!");
            return false;
        } else if (!val.matches(checkspaces)) {
            first_name.setError("No White spaces are allowed!");
            return false;
        } else {
            first_name.setError(null);
            first_name.setErrorEnabled(false);
            return true;
        }
    }

    // Validate Full Name
    private boolean validateLastName() {
        String val = last_name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            last_name.setError("Field can not be empty");
            return false;
        }
        else if (val.length() > 20) {
            first_name.setError("Last is too large!");
            return false;
        }
        else {
            last_name.setError(null);
            last_name.setErrorEnabled(false);
            return true;
        }
    }

    //Validate Email
    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();


        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    //VALIDATE GENDER
    private boolean validateGender() {
        if (gender_group.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // VALIDATE Age

    private boolean validateAge() {

        if(age_picker.getText().toString().length()!=18){
           dob_layout.setError("Please enter DOB in DD/MM/YYYY format");
           return false;

        }


        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = Integer.parseInt(age_picker.getText().toString().trim().substring(6));
//        Log.d(VGV,  "VGV:  validateAge:"+userAge);
        int isAgeValid = currentYear - userAge;
        if (isAgeValid < 18) {
            dob_layout.setError("You are not eligible to donate blood");
            return false;
        } else
            return true;
    }

    // Validate Full Address
    private boolean validateAddaress() {
        String val = Address_line1.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            Address_line1.setError("Field can not be empty");
            return false;
        }
        else if (val.length() > 50) {
            Address_line1.setError("Last is too large!");
            return false;
        }
        else {
            Address_line1.setError(null);
            Address_line1.setErrorEnabled(false);
            return true;
        }
    }

    // Validate Zip Code
    private boolean validateZipcode() {
        String val = Address_zipcode.getEditText().getText().toString().trim();

        String zipcode = "^\\d{6}$";

        if (val.isEmpty()) {
            Address_zipcode.setError("Zipcode can not be empty");
            return false;
        } else if (val.length() > 20) {
            Address_zipcode.setError("Zipcode is too large!");
            return false;
        } else if (!val.matches(zipcode)) {
            Address_zipcode.setError("No White spaces are allowed!");
            return false;
        } else {
            Address_zipcode.setError(null);
            Address_zipcode.setErrorEnabled(false);
            return true;
        }
    }


    // To set Values to Shared Preferences
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_USER_DETAILS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PHONE_NUMBER, fsUserID);
        editor.putString(FIRE_AUTH_UID, firebaseAuth.getCurrentUser().getUid());
        //     editor.putBoolean(LOCATION_ADMIN, switch1.isChecked());
        editor.apply();
//        editor.commit();

    }

    //saving a data
   // public void saveData() {
     //   SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_USER_DETAILS, MODE_PRIVATE);
     //   SharedPreferences.Editor editor = sharedPreferences.edit();
     //   editor.putString(USER_PHONE_NUMBER, fsUserID);
     //   editor.putString(FIRE_AUTH_UID, firebaseAuth.getCurrentUser().getUid());
        //     editor.putBoolean(LOCATION_ADMIN, switch1.isChecked());
      //  editor.apply();
//        editor.commit();

    //}





    public void callUserDashboardScreen(View view) {
        Intent mainIntent = new Intent(getApplicationContext(),Dashboard.class);
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