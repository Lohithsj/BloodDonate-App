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
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Updateprofile extends AppCompatActivity {

    public static final String LOGIN_USER_DETAILS = "loginUserDetails";
    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static final String FIRE_AUTH_UID = "FIRE_AUTH_UID";
    public static final String LOCATION_ADMIN = "false";

    //Variables
    Button next;
    TextView titleText, slideText;

    //spinner
    Spinner spinner;
    String[] paths = {"A+","O+","B+","AB+","A-","O-","B-","AB-"};

    //log
    // For Logging
    public static final String LSJ = "Data saving";


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
    String userID, userPhoneNumber,fsUserID,currentDateTime,blood_grp;  /* --- Added blood_grp string By sushma --*/

    RadioGroup gender_group;
    RadioButton selectedGender;

    TextView  age_picker,blood_group;
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
        //spinner
        //blood_group = findViewById(R.id.blood_grp);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Updateprofile.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /* --- Added By sushma --*/
                blood_grp= (String) parent.getItemAtPosition(position);
                Log.v("item", (String) parent.getItemAtPosition(position));
                return;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        gender_group = findViewById(R.id.signup_gender_group);
        age_picker = findViewById(R.id.display_Date);

        Address_line1 = findViewById(R.id.signup_Address_line1);
        Address_line2 = findViewById(R.id.signup_Address_line2);
        Address_village = findViewById(R.id.signup_Address_village);
        Address_zipcode = findViewById(R.id.signup_Address_zipcode);
        Address_city = findViewById(R.id.signup_Address_city);
        Address_state = findViewById(R.id.signup_Address_state);
        phone_number = findViewById(R.id.signup_phone_number);
        profile_edit_button = findViewById(R.id.profile_next_button);

        // Setting CurrentDateTime /*-- Added by Sushma*/

        currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        //Calender logic  /*-- Added by Sushma*/
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

    } //on create function ends here

    ///nextButton

    public void callNextScreen(View view) {

        if (!validateFirstName() | !validateLastName() | !validateEmail() |!validateGender() | !validateAge() | !validateAddress()| !validateZipCode()) {
            Log.d(LSJ, " User details are validated successfully");
            return;
        }


        Log.d(LSJ, "test1");
        //variable from this page
        String _first_name = first_name.getEditText().getText().toString().trim();
        Log.d(LSJ, "firstName"+_first_name);
        String _last_name = last_name.getEditText().getText().toString().trim();
        Log.d(LSJ, "_last_name"+_last_name);
        String _email = email.getEditText().getText().toString().trim();
        Log.d(LSJ, "_email"+_email);

        /*-- Added by Sushma*/
        Log.d(LSJ, "_blood_group"+blood_grp);


        selectedGender = findViewById(gender_group.getCheckedRadioButtonId());

        String gender = selectedGender.getText().toString();

        Log.d(LSJ, "selectedGender"+gender);



        String dateOfBirth = age_picker.getText().toString();
        Log.d(LSJ, "dateOfBirth"+dateOfBirth);

        //Variables address
        String address_line1= Address_line1.getEditText().getText().toString().trim();
        Log.d(LSJ, "address_line1"+address_line1);

        String address_line2= Address_line2.getEditText().getText().toString().trim();
        Log.d(LSJ, "address_line2"+address_line2);

        String address_village= Address_village.getEditText().getText().toString().trim();
        Log.d(LSJ, "address_village"+address_village);

        String address_zipcode= Address_zipcode.getEditText().getText().toString().trim();
        Log.d(LSJ, "address_zipcode"+address_zipcode);

        String address_city= Address_city.getEditText().getText().toString().trim();
        Log.d(LSJ, "address_city"+address_city);

        String address_state= Address_state.getEditText().getText().toString().trim();
        Log.d(LSJ, "address_state"+address_state);


        //Log.d(LSJ, "test2"+fsUserID);
        final DocumentReference docRefUserDetails = fStore.collection("UserDetails").document(fsUserID);


        Map<String,Object> userDetails = new HashMap<>();
        userDetails.put("firstName",_first_name);
        userDetails.put("lastName",_last_name);
        userDetails.put("email",_email );
        userDetails.put("blood_group",blood_grp);
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


        //Log.d(LSJ, "test3"+currentDateTime);

        docRefUserDetails.set(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(LSJ, " UserDetails Inserted calling save data");

                    saveData();

                    Log.d(LSJ, " User details is saved sucessfully");

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

    /*
    private boolean validateBloodGroup(){
        if (blood_grp.trim().isEmpty()) {
            try {
                blood_group.setError("Enter Your Blood group");
            }
            catch (Exception e)
            {}
            next.requestFocus();
            return false;
        }
        return true;
    }*/

    //validateFirstName
    private boolean validateFirstName() {
        String val = first_name.getEditText().getText().toString().trim();


        //String checkspaces = "^[aA-zZ]{3,20}$";


        if (val.isEmpty()) {
            first_name.setError("Name can not be empty");
            return false;
        } else if (val.length() > 20) {
            first_name.setError("Name is too large!");
            return false;
        } else {
            first_name.setError(null);
            Log.d(LSJ, " User first name validate successfully");
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
            Log.d(LSJ, " User last name validate sucessfully");
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
            Log.d(LSJ, " User first email validate successfully");
            return true;
        }

    }

    //VALIDATE GENDER
    private boolean validateGender() {
        if (gender_group.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Log.d(LSJ, " User gender validate successfully");
            return true;
        }

    }

    // VALIDATE Age

    private boolean validateAge() {

        if(age_picker.getText().toString().length()!=10){
            dob_layout.setError("Please enter DOB in DD/MM/YYYY format");
            return false;

        }
        Log.d(LSJ,  "ls:  validateAge:furst line"+age_picker.getText().toString().trim().substring(6));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = Integer.parseInt(age_picker.getText().toString().trim().substring(6));
//        Log.d(LSJ,  "validateAge:"+userAge);
        int isAgeValid = currentYear - userAge;
        Log.d(LSJ,  "isAgeValid:"+isAgeValid);
        if (isAgeValid < 18) {
            dob_layout.setError("You are not eligible to apply");
            return false;
        } else
            return true;
    }

    // Validate Full Address
    private boolean validateAddress() {
        String val = Address_line1.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            Address_line1.setError("Field can not be empty");
            return false;
        }
        else if (val.length() > 50) {
            Address_line1.setError("Address is too large!");
            return false;
        }
        else {
            Address_line1.setError(null);
            Log.d(LSJ, " User address validate sucessfully");
            return true;
        }
    }

    // Validate Zip Code
    private boolean validateZipCode() {
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
            Log.d(LSJ, " User zipcode validated sucessfully");
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
        Log.d(LSJ, " User data saved successfully");

        /*-- Added by Sushma--*/

        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();


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
        //super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(mainIntent);
        finish();
        super.onBackPressed();
    }

}