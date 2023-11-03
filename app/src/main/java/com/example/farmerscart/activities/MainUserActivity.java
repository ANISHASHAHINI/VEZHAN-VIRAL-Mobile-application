package com.example.farmerscart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.farmerscart.R;
import com.example.farmerscart.adapters.AdapterOrderUser;
import com.example.farmerscart.adapters.AdapterShop;
import com.example.farmerscart.models.ModelOrderUser;
import com.example.farmerscart.models.ModelShop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity {

    private TextView nametv, emailtv, phonetv, tabShopsTv, tabOrdersTv;
    private ImageButton editprofile;
    private RelativeLayout shopsRl, ordersRl;
    private ImageView profileIv;
    private RecyclerView shopsRv,ordersRv;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<ModelShop> shopsList;
    private AdapterShop adapterShop;

    private ArrayList<ModelOrderUser> ordersList;
    private AdapterOrderUser adapterOrderUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        nametv = findViewById(R.id.nametv);
        emailtv = findViewById(R.id.emailtv);
        phonetv = findViewById(R.id.phonetv);
        tabShopsTv = findViewById(R.id.tabShopsTv);
        tabOrdersTv = findViewById(R.id.tabOrdersTv);
        editprofile = findViewById(R.id.editprofile);
        profileIv = findViewById(R.id.profileIv);
        shopsRl = findViewById(R.id.shopsRl);
        ordersRl = findViewById(R.id.ordersRl);
        shopsRv = findViewById(R.id.shopsRv);
        ordersRv = findViewById(R.id.ordersRv);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        showShopsUi();

        editprofile.setOnClickListener((v) -> {

            startActivity(new Intent(MainUserActivity.this, ProfileEditUserActivity.class));
        });

        tabShopsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShopsUi();
            }
        });

        tabOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrdersUi();
            }
        });

    }


        private void showShopsUi () {

            shopsRl.setVisibility(View.VISIBLE);
            ordersRl.setVisibility(View.GONE);

            tabShopsTv.setTextColor(getResources().getColor(R.color.black));
            tabShopsTv.setBackgroundResource(R.drawable.shape_rect04);

            tabOrdersTv.setTextColor(getResources().getColor(R.color.white));
            tabOrdersTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        }

        private void showOrdersUi () {

            shopsRl.setVisibility(View.GONE);
            ordersRl.setVisibility(View.VISIBLE);

            tabShopsTv.setTextColor(getResources().getColor(R.color.white));
            tabShopsTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            tabOrdersTv.setTextColor(getResources().getColor(R.color.black));
            tabOrdersTv.setBackgroundResource(R.drawable.shape_rect04);

        }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user==null){
            startActivity(new Intent(MainUserActivity.this, Login.class));
            finish();
        }
        else {
            loadMyInfo();
        }
    }

    private void loadMyInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String phone =""+ds.child("phone").getValue();
                            String email =""+ds.child("email").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();


                            nametv.setText(name);
                            emailtv.setText(email);
                            phonetv.setText(phone);

                            loadShops(city);
                            loadOrders();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadOrders() {
        ordersList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String uid = "" +ds.getRef().getKey();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Orders");
                    ref.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            ModelOrderUser modelOrderUser = ds.getValue(ModelOrderUser.class);

                                            ordersList.add(modelOrderUser);
                                        }

                                        adapterOrderUser = new AdapterOrderUser(MainUserActivity.this,ordersList);
                                        ordersRv.setAdapter(adapterOrderUser);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadShops(String city) {

        shopsList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        shopsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String shopCity = ""+ds.child("city").getValue();

                            if(shopCity.equals(city)){
                                shopsList.add(modelShop);

                            }

                            //if you want to display all shops, skip the if statement and add
                            //shopsList.add(modelshop);
                        }
                        adapterShop = new AdapterShop(MainUserActivity.this,shopsList);
                        shopsRv.setAdapter(adapterShop);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}




