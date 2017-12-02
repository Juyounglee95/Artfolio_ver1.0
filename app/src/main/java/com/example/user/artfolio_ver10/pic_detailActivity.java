package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class pic_detailActivity extends AppCompatActivity {
String path, name, memo_text;
ImageView image ;
TextView memo;
TextView imagename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_detail);
        Intent intent = getIntent();
        path = intent.getExtras().getString("path");
        name = intent.getExtras().getString("name");
        memo_text = intent.getExtras().getString("memo");
    //    setMemo();
        image= (ImageView)findViewById(R.id.detail_image);
        memo =(TextView)findViewById(R.id.memo);
        imagename=(TextView)findViewById(R.id.Image_name);
        Glide.with(this)
                .load(path)
                .into(image);
        imagename.setText(name);
        System.out.println(memo_text);
        memo.setText(memo_text);


    }
    public void edit_memo(View view){
        Intent intent = new Intent(this, Editmemo_Activity.class);
        intent.putExtra("path", path);
        intent.putExtra("name",name);
        intent.putExtra("memo", memo_text);
        startActivityForResult(intent, 0);
    }
    protected void onActivityResult(int requestCode, int resultcode, Intent data){
        super.onActivityResult(requestCode, resultcode, data);
        if(requestCode==0){
            if(data!=null) {
                memo_text = data.getStringExtra("memo");
                memo.setText(memo_text);
            }
        }
    }


}
