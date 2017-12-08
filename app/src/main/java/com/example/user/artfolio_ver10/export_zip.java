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
    EditText edit_email, edit_subject, edit_text, edit_filename;
    String filename, emailadd, subject, text;
    String zippath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // File storageDir = new File(getApplicationContext().getFilesDir(), "/artfolio");
        setContentView(R.layout.activity_export_zip);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        path = intent.getExtras().getStringArray("path");
        edit_filename= (EditText)findViewById(R.id.filename);
        edit_email = (EditText)findViewById(R.id.email_edit);
        edit_email.setText(email);
        edit_subject=(EditText)findViewById(R.id.subject_edit);
        edit_text=(EditText)findViewById(R.id.text_edit);

        DeleteDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                +"/artfolio");
        for(int i=0; i<path.length; i++) {
                final String file = path[i];
        //        System.out.println("path @@@@@@@@2"+file);
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
        //    System.out.println("mkdirs :"+success);
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
        filename = edit_filename.getText().toString();
        emailadd = edit_email.getText().toString();
        subject = edit_subject.getText().toString();
        text = edit_text.getText().toString();
        zippath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + "/"+filename+".zip";
        try {
            File exist = new File(zippath);
            if(exist.exists()){
             Boolean delete=   exist.delete();
             System.out.println("zipdelete"+delete);
            }

            ZipFile zipfile = new ZipFile(zippath);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipfile.addFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/artfolio", parameters);


            sendEmail(zippath);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void DeleteDir(String path)
    {
        File file = new File(path);
        File[] childFileList = file.listFiles();
        for(File childFile : childFileList)
        {
            if(childFile.isDirectory()) {
                DeleteDir(childFile.getAbsolutePath());     //하위 디렉토리 루프
            }
            else {
              boolean delete=  childFile.delete();    //하위 파일삭제
            }
        }
        boolean rootdelete= file.delete();    //root 삭제

        System.out.println("rootedfdfa"+rootdelete);
    }


    public void sendEmail(String zippath){
        File filein = new File(zippath);
        Uri u =  FileProvider.getUriForFile(this,"com.example.user.artfolio_ver10", filein);
        //email send
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("plain/text");


        String[] address = {emailadd}; //주소를 넣어두면 미리 주소가 들어가 있다.

        intent.putExtra(Intent.EXTRA_EMAIL, address);

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        intent.putExtra(Intent.EXTRA_TEXT, text);

        intent.putExtra(Intent.EXTRA_STREAM, u); //파일 첨부

        startActivity(intent);

        Toast.makeText(this, "Send email success", Toast.LENGTH_SHORT).show();

        finish();
    }


    }

