package com.example.farmerscart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmerscart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegiserSellerActivity extends AppCompatActivity {

    private EditText fullnameEt,farmnameEt,phoneEt,countryEt,stateEt,cityEt,address,emailEt,passwordEt,cpasswordEt,deliveryfeeEt;
    private Button registerbtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser_seller);

        fullnameEt = findViewById(R.id.fullnameEt);
        farmnameEt = findViewById(R.id.farmnameEt);
        phoneEt = findViewById(R.id.phoneEt);
        deliveryfeeEt = findViewById(R.id.deliveryfeeEt);
        countryEt = findViewById(R.id.countryEt);
        stateEt = findViewById(R.id.stateEt);
        cityEt = findViewById(R.id.cityEt);
        address = findViewById(R.id.address);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        cpasswordEt = findViewById(R.id.cpasswordEt);
        registerbtn = findViewById(R.id.registerbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register seller
                inputData();
            }
        });

    }

    private String fullName,farmName,phoneNumber,deliveryfee,country,state,city,completeaddress,email,password,confirmpassword;

    private void inputData() {

        fullName = fullnameEt.getText().toString().trim();
        farmName = farmnameEt.getText().toString().trim();
        phoneNumber = phoneEt.getText().toString().trim();
        deliveryfee = deliveryfeeEt.getText().toString().trim();
        country = countryEt.getText().toString().trim();
        state = stateEt.getText().toString().trim();
        city = cityEt.getText().toString().trim();
        completeaddress = address.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmpassword = cpasswordEt.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter name...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(farmName)){
            Toast.makeText(this, "Enter farmname...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Enter phonenumber...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(deliveryfee)){
            Toast.makeText(this, "Enter deliveryfee...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(country)){
            Toast.makeText(this, "Enter country...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(state)){
            Toast.makeText(this, "Enter state...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(city)){
            Toast.makeText(this, "Enter city...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(completeaddress)){
            Toast.makeText(this, "Enter completeaddress...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Pattern...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length()<6){
            Toast.makeText(this, "Password must be atleast 6 characters long...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmpassword)){
            Toast.makeText(this, "Password doesn't match...", Toast.LENGTH_SHORT).show();
            return;
        }
        createAccount();

    }

    private void createAccount() {
        progressDialog.setMessage("Createing Account...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account created
                        saveFirebaseData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed creating account
                        progressDialog.dismiss();
                        Toast.makeText(RegiserSellerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void saveFirebaseData() {
        progressDialog.setMessage("Saving Account Info...");

        String timestamp = ""+System.currentTimeMillis();


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("email",""+email);
        hashMap.put("name",""+fullName);
        hashMap.put("farmname",""+farmName);
        hashMap.put("phone",""+phoneNumber);
        hashMap.put("deliveryfee",""+deliveryfee);
        hashMap.put("country",""+country);
        hashMap.put("state",""+state);
        hashMap.put("city",""+city);
        hashMap.put("address",""+completeaddress);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("accountType","Seller");
        hashMap.put("online","true");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //db updated
                        progressDialog.dismiss();
                        startActivity(new Intent(RegiserSellerActivity.this, MainSellerActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating db
                      progressDialog.dismiss();
                      startActivity(new Intent(RegiserSellerActivity.this,MainSellerActivity.class));
                      finish();
                    }
                });


    }


}