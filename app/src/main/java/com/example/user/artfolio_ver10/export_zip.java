package com.example.user.artfolio_ver10;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.androidquery.util.AQUtility.getContext;

public class export_zip extends AppCompatActivity {
    String email;
    String [] path;
    String [] realpath;
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
        edit_filename = (EditText) findViewById(R.id.filename);
        edit_email = (EditText) findViewById(R.id.email_edit);
        edit_email.setText(email);
        edit_subject = (EditText) findViewById(R.id.subject_edit);
        edit_text = (EditText) findViewById(R.id.text_edit);

        DeleteDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + "/artfolio");

        realpath = new String[path.length];
        for (int i = 0; i < path.length; i++) {
            realpath[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + path[i];
        }

        for (String str : path) {

            DownloadFileAsync downloadFileAsync = new DownloadFileAsync();
            downloadFileAsync.execute(str, "1", "1");

        }
    }
    class DownloadFileAsync extends AsyncTask<String, Integer, String> {        //이미지 다운로드

    private String fileName;
    //String savePath = Environment.getExternalStorageDirectory() + File.separator + "temp/";
        ProgressDialog asyncDialog = new ProgressDialog(export_zip.this);
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("Loading..");

        // show dialog
        asyncDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/artfolio");
        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String fileUrl = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+params[0];

      //  String localPath = savePath + "/" + fileName + ".jpg";

        try {
            URL imgUrl = new URL(fileUrl);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            int response = conn.getResponseCode();

            File file = new File(storageDir,params[0]);

            InputStream is = conn.getInputStream();
            OutputStream outStream = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len = 0;

            while ((len = is.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }

            outStream.close();
            is.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String obj) {

        super.onPostExecute(obj);
        asyncDialog.dismiss();

        Toast.makeText(export_zip.this, "Create ZIP file success", Toast.LENGTH_SHORT).show();
    }
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

       // Toast.makeText(this, "Send email success", Toast.LENGTH_SHORT).show();

        finish();
    }


    }

