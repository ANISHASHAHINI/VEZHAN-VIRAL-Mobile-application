package com.example.farmerscart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import com.example.farmerscart.R;

public class Dashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);



    }

    public void callLoginScreen(View view){

        Intent intent=new Intent(getApplicationContext(), Login.class);

        Pair[] pairs=new Pair[1];

        pairs[0]=new Pair<View,String>(findViewById(R.id.loginbutton),"transition_login");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Dashboard.this,pairs);
        startActivity(intent,options.toBundle());
    }

    public void callRegisterScreen(View view){

        Intent intent=new Intent(getApplicationContext(), RegisterUserActivity.class);

        Pair[] pairs=new Pair[1];

        pairs[0]=new Pair<View,String>(findViewById(R.id.regbutton),"transition_login");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Dashboard.this,pairs);
        startActivity(intent,options.toBundle());
    }

}

