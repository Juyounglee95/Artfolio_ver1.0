package com.example.user.artfolio_ver10;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getintro_image extends AppCompatActivity  {

    private static final int PICK_FROM_CAMERA =0;
    private static final int PICK_FROM_GALLERY =1;
    private static final int CROP_FROM_IMAGE =2;
    private static final int MY_PERMISSION_CAMERA = 3;
    private static final int GALLERY_ADD_PIC = 4;
    private String absolutePath;
    String image_name;
    String username;
    ImageView imageView;
    TextView imageName;
    EditText edit_memo;
    String  pic_memo;
    private Uri imageUri;
    private Uri photoURI, albumURI;
    TransferUtility transferUtility;
    TextView memoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        imageView= (ImageView)findViewById(R.id.picked_image);
        imageName = (TextView)findViewById(R.id.Image_name);
        edit_memo=(EditText)findViewById(R.id.pic_memo);
        memoview =(TextView)findViewById(R.id.memoview);
        //checkPermission();
        Intent intent = getIntent();
        int index = intent.getExtras().getInt("index");
        username = intent.getExtras().getString("user_id");
        setMode(index);
//        String name = intent.getExtras().getString("name");
//        userName= (TextView)findViewById(R.id.user_name);
//        userName.setText(name);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:e511d75d-2f61-4459-9ea1-e388bfaa5be0", // Identity pool ID
                Regions.AP_NORTHEAST_1 // Region
        );
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        transferUtility = new TransferUtility(s3, getApplicationContext());

        edit_memo.setVisibility(View.INVISIBLE);
        memoview.setVisibility(View.INVISIBLE);

    }
    public void sendPic_server(View v){
//        TransferObserver observer = transferUtility.upload(
//                "artfolio-imageupload",     /* 업로드 할 버킷 이름 */
//                OBJECT_KEY,    /* 버킷에 저장할 파일의 이름 */
//                MY_FILE        /* 버킷에 저장할 파일  */
//        );
      //  pic_memo=edit_memo.getText().toString();
        intro_image_upload picInfo_upload = new intro_image_upload();
        picInfo_upload.execute();


    }
    private void send_server(String filePath) {
        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload("artfolio-imageupload", file.getName(),
                file);
        Toast.makeText(this, "Picture uploaded on Server SUCCESS!",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        image_name= "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+image_name;
        intent.putExtra("image", image_name);
        setResult(100, intent);
        finish();
        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());
    }
    public void setMode(int index){
        checkPermission();
        if(index ==PICK_FROM_CAMERA){
            act_camera();
        }
        else if(index==PICK_FROM_GALLERY){
            act_gallery();
        }
    }

    public void act_camera(){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //  startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와
                    // 일치해야 함

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + username+"_" + timeStamp + ".jpg";
        image_name = imageFileName;
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "Artfolio");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        absolutePath = imageFile.getAbsolutePath();

        return imageFile;
    }

    public void act_gallery(){
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_GALLERY);

    }
    public void galleryAddPic(View v){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(absolutePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
//        TransferObserver observer = transferUtility.upload(
//                "artfolio-imageupload",     /* 업로드 할 버킷 이름 */
//                f.getName(),    /* 버킷에 저장할 파일의 이름 */
//                f        /* 버킷에 저장할 파일  */
//        );
        //  mediaScanIntent.setDataAndType(contentUri, "image/*");
        Toast.makeText(getintro_image.this, "Picture Saved", Toast.LENGTH_SHORT).show();
        //  startActivityForResult(mediaScanIntent, PICK_FROM_CAMERA);
    }
    private void storeCropImage(){
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + " / albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");
        // cropIntent.putExtra("outputX", 20); // crop한 이미지의 x축 크기, 결과물의 크기
        // cropIntent.putExtra("outputY", 20); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        startActivityForResult(cropIntent, CROP_FROM_IMAGE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){


        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        try {
                            File albumFile = null;
                            albumFile = createImageFile(); //imagefile return
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            Log.i("pick image from gallery", absolutePath);

                            storeCropImage();

                        } catch (Exception e) {
                            Log.e("REQUEST_TAKE_PHOTO", e.toString());
                        }
                    }
                }else {
                    Toast.makeText(getintro_image.this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case PICK_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {

                    //if(data.getData() != null){
                    try {
                        //  galleryAddPic();

                        //  imageView.setImageURI(imageUri);
                        //imageName.setText(image_name);
                        imageView.setImageURI(imageUri);
                        System.out.println("set Image ok");
                        imageName.setText(image_name);
                        System.out.println("set text ok");
                        Log.i("pick image from gallery", absolutePath);

                        // storeCropImage();

                    }catch (Exception e){
                        Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                    }
                    // }
                }
                break;

            case CROP_FROM_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

                    //   galleryAddPic();
                    imageView.setImageURI(albumURI);
                    System.out.println("set Image ok");
                    imageName.setText(image_name);
                    System.out.println("set text ok");
                }
                break;
//            case GALLERY_ADD_PIC:
//                if (resultCode == Activity.RESULT_OK) {
//
//                    //   galleryAddPic();
//                    //imageView.setImageURI(imageUri);
//                    imageName.setText(image_name);
//                }
//                break;
        }
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
                        Toast.makeText(getintro_image.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }


    class intro_image_upload extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... unuesed){
            System.out.println(pic_memo);
            String data = "";
            String value ="ID="+username+"&introimage_name="+image_name+"";
            try {
                URL url = new URL("http://54.226.200.206/update_introimage.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.connect();

                OutputStream outs = con.getOutputStream();
                outs.write(value.getBytes("UTF-8"));
                outs.flush();
                outs.close();
                InputStream is = null;
                BufferedReader in = null;


                is = con.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        protected void onPostExecute(String data) {

                 /* 서버에서 응답 */

            Log.e("%%%%%%%%%%%%%%%%",data);
            if(data.equals("ERROR"))
            {
                Toast.makeText(getApplicationContext(),"Upload Fail", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"Upload Success", Toast.LENGTH_SHORT).show();
//                send_server(absolutePath);
                //dashboard fragment picnum update 시키기
                //   finish();
            }
            else
            {

                //   Toast.makeText(getApplicationContext(),"Upload Fail", Toast.LENGTH_SHORT).show();
                //  Toast.makeText(getApplicationContext(),"입력사항을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Upload Success", Toast.LENGTH_SHORT).show();
                send_server(absolutePath);
            }
        }




    }
}