package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class pic_detailActivity extends AppCompatActivity {
String path, name, memo_text, mode;
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

        mode = intent.getExtras().getString("mode");
        if(mode.equals("otheruser")){
            ImageButton bt = (ImageButton)findViewById(R.id.sendImage_server);
            bt.setVisibility(View.GONE);
        }
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
        intent.putExtra("mode", "pic");
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
