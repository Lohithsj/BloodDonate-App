package com.example.blooddonate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    private Button mSendOTPBtn;
    private TextView processText;
    private CountryCodePicker countryCode;
    private TextInputEditText phoneNumber;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_auth);

        mSendOTPBtn = findViewById(R.id.otp_btn);
        processText = findViewById(R.id.invisible_btn);
        countryCode = findViewById(R.id.country_code);
        phoneNumber = findViewById(R.id.phone_number);

        auth = FirebaseAuth.getInstance();

        mSendOTPBtn.setOnClickListener(view -> {
            String country_code = countryCode.getSelectedCountryCode();
            String phone = phoneNumber.getText().toString();

            String phoneNumber = "+" + country_code +"" + phone;

            if(!country_code.isEmpty() || (!phone.isEmpty() && phone.length() == 10)){

                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(PhoneAuth.this)
                        .setCallbacks(mCallBacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

            }else{
                processText.setText("Please Enter your Phone Number");
                processText.setTextColor(Color.RED);
                processText.setVisibility(View.VISIBLE);
            }

        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                processText.setText(e.getMessage());
                processText.setTextColor(Color.RED);
                processText.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                processText.setText("OTP has been sent");
                processText.setVisibility(View.VISIBLE);

                new Handler().postDelayed(() -> {
                    Intent otpIntent = new Intent(PhoneAuth.this, Verify.class);
                    otpIntent.putExtra("auth", s);
                    startActivity(otpIntent);

                },5000);
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =  auth.getCurrentUser();
        if(user!= null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(PhoneAuth.this,Dashboard.class);
        startActivity(mainIntent);
        finish();
    }
    private void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                sendToMain();
            }else{
                processText.setText(task.getException().getMessage());
                processText.setTextColor(Color.RED);
                processText.setVisibility(View.VISIBLE);
            }
        });
    }

}