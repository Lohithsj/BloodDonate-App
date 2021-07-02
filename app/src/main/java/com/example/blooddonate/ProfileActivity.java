package com.example.blooddonate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    TextView firstName,lastName;
    TextView phoneNo,eMail;
    TextView bloodGroup,avs;
    TextView aLine1,aLine2,pinCode;

   // String pfirstName,plastName,pPhoneNo,pMail,pbloodGroup,pavs,paLine1,paLine2,pPinCode;

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.f_name);
        lastName = findViewById(R.id.l_name);
        phoneNo = findViewById(R.id.phone_no);
        eMail = findViewById(R.id.email);
        bloodGroup = findViewById(R.id.blood_group);
        avs = findViewById(R.id.area);
        aLine1 = findViewById(R.id.address_line1);
        aLine2 = findViewById(R.id.address_line2);
        pinCode = findViewById(R.id.pincode);

       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        */
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference docRef =fStore.collection("UserDetails").document(firebaseAuth.getCurrentUser().getUid());

        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));
                phoneNo.setText(documentSnapshot.getString("userPhoneNumber"));
                eMail.setText(documentSnapshot.getString("email"));
                bloodGroup.setText(documentSnapshot.getString("blood_group"));
                avs.setText(documentSnapshot.getString("address_village"));
                aLine1.setText(documentSnapshot.getString("address_lin1"));
                aLine2.setText(documentSnapshot.getString("address_line2"));
                pinCode.setText(documentSnapshot.getString("address_zipcode"));


            }
        });
        /*docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    pfirstName = documentSnapshot.getString("firstName");
                    plastName =  documentSnapshot.getString("lastName");
                    pMail = documentSnapshot.getString("email");
                    pPhoneNo = firebaseAuth.getCurrentUser().getPhoneNumber();
                    pbloodGroup = documentSnapshot.getString("email");
                    pavs = documentSnapshot.getString("address_village");
                    paLine1 = documentSnapshot.getString("address_line1");
                    paLine2 = documentSnapshot.getString("address_line2");
                    pPinCode = documentSnapshot.getString("address_zipcode");

                    firstName.setText(pfirstName);
                    lastName.setText(plastName);
                    phoneNo.setText(pPhoneNo);
                    eMail.setText(pMail);
                    bloodGroup.setText(pbloodGroup);
                    avs.setText(pavs);
                    aLine1.setText(paLine1);
                    aLine2.setText(paLine2);
                    pinCode.setText(pPinCode);

                }else {
                    //Log.d(TAG, "Retrieving Data: Profile Data Not Found ");
                }
            }
        });*/

    }

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