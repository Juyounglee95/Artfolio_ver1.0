package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.artfolio_ver10.R;

public class First_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
    }

    public void Login(View v){
        Intent intent = new Intent(this, main_login.class);
        startActivity(intent);
       // finish();
    }

    public void act_back(View v){
        setContentView(R.layout.first_page);
    }


    public void Sign_up(View v){
        Intent intent = new Intent(this, Sign_up.class);
        startActivity(intent);
        //finish();
    }


}