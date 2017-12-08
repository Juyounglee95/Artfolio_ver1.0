package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.androidquery.util.AQUtility.getContext;

public class export_zip extends AppCompatActivity {
    String email;
    String [] path;
    /**
     * Created by user on 2017-12-09.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File storageDir = new File(getApplicationContext().getFilesDir(), "/artfolio");
        setContentView(R.layout.activity_export_zip);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        path = intent.getExtras().getStringArray("path");
        EditText email_edit = (EditText)findViewById(R.id.email_edit);
        email_edit.setText(email);



    for(int i=0; i<path.length; i++) {
                final String file = path[i];
                System.out.println("path @@@@@@@@2"+file);
                BaseTarget target = new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        // do something with the bitmap
                        // for demonstration purposes, let's set it to an imageview
                        saveImage(bitmap, file);
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {}
                };

                Glide.with(this)
                        .asBitmap()
                        .load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+path[i])
                        .into(target);

            }

        }
    private String saveImage(Bitmap image, String filename) {
        String savedImagePath = null;
        String name = filename;

        String imageFileName = name ;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                +"/artfolio");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
            System.out.println("mkdirs :"+success);
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
         //   System.out.println("path$$$$$$$$$"+savedImagePath);
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();


                System.out.println(savedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            //  galleryAddPic(savedImagePath);
//
        }
        return savedImagePath;
    }

    public void send_zip(View v){
        try {


            ZipFile zipfile = new ZipFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/artfolio.zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipfile.addFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/artfolio", parameters);

            File filein = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                    + "/artfolio.zip");
            Uri u =  FileProvider.getUriForFile(this,"com.example.user.artfolio_ver10", filein);
            //email send
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("plain/text");


            String[] address = {"dongry30@gmail.com"}; //주소를 넣어두면 미리 주소가 들어가 있다.

            intent.putExtra(Intent.EXTRA_EMAIL, address);

            intent.putExtra(Intent.EXTRA_SUBJECT, "test123");

            intent.putExtra(Intent.EXTRA_TEXT, "testttt");

            intent.putExtra(Intent.EXTRA_STREAM, u); //파일 첨부

            startActivityForResult(intent,0);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    }

