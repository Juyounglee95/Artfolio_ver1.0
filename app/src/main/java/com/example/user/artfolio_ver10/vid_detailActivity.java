package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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

public class vid_detailActivity extends AppCompatActivity {
    String path, name, memo_text, mode;
    VideoView video ;
    TextView memo;
    TextView videoname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid_detail);
        Intent intent = getIntent();
        path = intent.getExtras().getString("path");
        name = intent.getExtras().getString("name");
        memo_text = intent.getExtras().getString("memo");

        mode = intent.getExtras().getString("mode");
        if(mode.equals("otheruser")){
            ImageButton bt = (ImageButton)findViewById(R.id.editmemo);
            bt.setVisibility(View.GONE);
        }
        //    setMemo();
        video= (VideoView)findViewById(R.id.detail_video);
        memo =(TextView)findViewById(R.id.memo);
        videoname=(TextView)findViewById(R.id.video_name);

        videoname.setText(name);
        System.out.println(memo_text);
        memo.setText(memo_text);
       // path="https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+path;

                //
       // String videoFile = "//";
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
        video.setBackgroundDrawable(bitmapDrawable);
        video.setVideoPath(path);
        final MediaController mediaController =
                new MediaController(vid_detailActivity.this);
        video.setMediaController(mediaController);

        video.start();

        video.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaController.show(0);
                video.pause();
            }
        }, 100);

    }


    public void edit_memo(View view){
        Intent intent = new Intent(this, Editmemo_Activity.class);
        intent.putExtra("path", path);
        intent.putExtra("name",name);
        intent.putExtra("memo", memo_text);
        intent.putExtra("mode", "video");
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
