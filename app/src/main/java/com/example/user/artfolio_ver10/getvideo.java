package com.example.user.artfolio_ver10;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getvideo extends AppCompatActivity {
    private static final int SELECT_VIDEO = 1;
    private static final int ACTION_TAKE_VIDEO = 2;
    private static final int MY_PERMISSION_CAMERA =3;
    private static final int VIDEO_PICK_FROM_GALLERY = 4;
    private static final int VIDEO_PICK_FROM_CAMERA = 5;
    private VideoView vv;

    private String selectedVideoPath, videopath;
    String username;
    private String absolutePath;
    String video_name;
    TextView videoName;

    private Uri mVideoUri, videoUri, albumURI;
    private Bitmap mImageBitmap;

    @ Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_video);
        vv = (VideoView)findViewById(R.id.picked_video);
        videoName = (TextView)findViewById(R.id.video_name);

        Intent intent = getIntent();
        int index = intent.getExtras().getInt("index");
        username = intent.getExtras().getString("user_id");

        setMode(index);
    }

    public void setMode(int index){
        checkPermission();
        if(index==VIDEO_PICK_FROM_GALLERY){
            video_gallery();
        }else if(index ==VIDEO_PICK_FROM_CAMERA){
            video_camera();
        }
    }

    public void video_gallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        i.setType(MediaStore.Video.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(i,"Select Video"), SELECT_VIDEO);
    }

    public void video_camera() {
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (i.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createvideoFile();
            } catch (IOException ex) {
                Log.e("captureCamera Error", ex.toString());
            }
            if (photoFile != null) {
                // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와
                // 일치해야 함

                Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                mVideoUri = providerURI;

                // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                i.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                startActivityForResult(i, ACTION_TAKE_VIDEO);
            }
        }
    }

    @ Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                try{
                    selectedVideoPath = getPath(data.getData());
                    if(selectedVideoPath == null) {
                        System.out.println("selected video path = null!");
                        finish();
                    } else {
                        File albumFile = null;
                        albumFile = createvideoFile();
                        videoUri = data.getData();
                        albumURI = Uri.fromFile(albumFile);

                        Log.e("path >>", selectedVideoPath);
                        videopath = selectedVideoPath;
                        gallery_video();
                    }
                }catch (Exception e){
                    Toast.makeText(getvideo.this, "동영상 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == ACTION_TAKE_VIDEO){
                try{
                    handleCameraVideo(data);
                }catch(Exception e){
                    Log.e("REQUEST_TAKE_VIDEO", e.toString());
                }
            }

        }
        vv.start();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    public void gallery_video(){
        galleryAddVid();
        videoName.setText(video_name);
        vv.setVisibility(View.VISIBLE);

        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);

        Uri uri = Uri.parse(videopath);
        vv.setVideoURI(uri);
    }

    private void handleCameraVideo(Intent intent) {
        galleryAddVid();
        videoName.setText(video_name);
        vv.setVisibility(View.VISIBLE);

        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);

        mVideoUri = intent.getData();
        vv.setVideoURI(mVideoUri);

        Log.e("path ->", getPath(intent.getData()));
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(getvideo.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }

    public File createvideoFile() throws IOException {
        // Create an video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "MPEG_" + username+"_" + timeStamp + ".mp4";
        video_name = videoFileName;
        File videoFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "Artfolio");

        if (!storageDir.exists()) {
            Log.i("mCurrentVideoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        videoFile = new File(storageDir, videoFileName);
        absolutePath = videoFile.getAbsolutePath();

        return videoFile;
    }

    private void galleryAddVid(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(absolutePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
//        TransferObserver observer = transferUtility.upload(
//                "artfolio-imageupload",     *//* 업로드 할 버킷 이름 *//*
//                f.getName(),    *//* 버킷에 저장할 파일의 이름 *//*
//                f        *//* 버킷에 저장할 파일  *//*
//        );
        //  mediaScanIntent.setDataAndType(contentUri, "image*//*");
        Log.e("take video >>", absolutePath);
        Toast.makeText(getvideo.this, "Video Saved", Toast.LENGTH_SHORT).show();
        //  startActivityForResult(mediaScanIntent, PICK_FROM_CAMERA);
    }
}