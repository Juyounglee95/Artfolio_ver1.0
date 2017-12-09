package com.example.user.artfolio_ver10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class usersetting_activity extends AppCompatActivity {
    String id;
    String profile;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    ImageView profile_view;
    TransferUtility transferUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersetting_frag);
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        profile = intent.getExtras().getString("profilepath");
        TextView userid = (TextView) findViewById(R.id.user_id);
        profile_view = (ImageView)findViewById(R.id.profile);
        Button btnChange = (Button) findViewById(R.id.change);
        Button btnSave = (Button) findViewById(R.id.save);
        if(!profile.equals("null")){
        Glide.with(this).load(profile).into(profile_view);}
        userid.setText(id);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(usersetting_activity.this);
                builder.setTitle("변경할 이미지 선택")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                act_camera();
                            }
                        })
                        .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                act_gallery();
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "USER SETTING COMPLETE", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("profile", profile);
                setResult(200, intent);
                finish();
            }
        });
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:e511d75d-2f61-4459-9ea1-e388bfaa5be0", // Identity pool ID
                Regions.AP_NORTHEAST_1 // Region
        );
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    public void act_gallery() {
        Toast.makeText(this, "Pick Image from your gallery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(usersetting_activity.this, getprofile.class);
        intent.putExtra("index", PICK_FROM_GALLERY);
        intent.putExtra("user_id", id);
        startActivityForResult(intent, 100);
    }

    public void act_camera() {
        Toast.makeText(this, "Pick Image from your camera", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(usersetting_activity.this, getprofile.class);
        intent.putExtra("index", PICK_FROM_CAMERA);
        intent.putExtra("user_id", id);
        startActivityForResult(intent, 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 100) {
            if (data!=null) {
                profile = data.getStringExtra("profile");
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.kakao_default_profile_image);
                Glide.with(this).setDefaultRequestOptions(requestOptions).load(profile).into(profile_view);
                System.out.println("#####");

            }
        }
    }

}
