package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.user.artfolio_ver10.R;

public class First_Page extends AppCompatActivity {
    String Mode ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);


    }

    public void Login(View v){
        mode_select();
        Intent intent = new Intent(this, main_login.class);
        intent.putExtra("mode", Mode);
        startActivity(intent);
        // finish();
    }
//
//    public void Login_agency(View v){
//        Intent intent = new Intent(this, main_login_agency.class);
//        intent.putExtra("mode", Mode);
//        startActivity(intent);
//        // finish();
//    }


    public void act_back(View v){
        setContentView(R.layout.first_page);
    }


    public void Sign_up(View v){
        mode_select();
        Intent intent = new Intent(this, Sign_up.class);
        intent.putExtra("mode", Mode);
        startActivity(intent);
        //finish();
    }

    public void mode_select(){
        RadioGroup rbt_group = (RadioGroup)findViewById(R.id.radioGroup);
        try{
            if(rbt_group.getCheckedRadioButtonId() == R.id.user_mode){
                Mode ="user";
            } else{
                Mode ="agency";
            }
        }catch (Exception e){
            e.getMessage();
        }
    }


}