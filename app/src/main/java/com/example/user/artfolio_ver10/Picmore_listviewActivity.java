package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.androidquery.util.AQUtility.getContext;

public class Picmore_listviewActivity extends AppCompatActivity  {
    String image_list[];
    String name_list[];
    //String path, memo;
    RequestManager  mGlideRequestManager;
    TransferUtility transferUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_list_pic);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.picmore_listview);
        ArrayList<piclist_item> data = new ArrayList<>();
        PicmoreList_Adapter picmoreListAdapter;

        Intent intent = getIntent();
        name_list=  intent.getExtras().getStringArray("picmorelist");
//        for (int i = 0; i < name_list.length; i++) {
//
//           // image_list[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + piclist[i];
//            // System.out.println(image_list[i]);
//             System.out.println(name_list[i]);
//
//        }
        image_list= new String[name_list.length];
        //setToUrl(name_list);
        if(name_list!=null) {
            for (int i = 0; i < name_list.length; i++) {

                image_list[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + name_list[i];
                //System.out.println(image_list[i]);
                //System.out.println("name/////"+name_list[i]);

            }
        }
        for(int i=0; i<image_list.length; i++) {
            data.add(new piclist_item(image_list[i], name_list[i]));
            System.out.println("data: "+data.get(i).getImage_url());
        }

        if(data!=null) {
//            picmoreListAdapter = new PicmoreList_Adapter(Picmore_listviewActivity.this,
//                    data, mGlideRequestManager);
            //listView.setAdapter(picmoreListAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(layoutManager);

            mGlideRequestManager = Glide.with(this);
            //  Collections.reverse(data);
            recyclerView.setAdapter(new PicmoreList_Adapter(this, data, R.layout.picmore_item,mGlideRequestManager));
         //   finish();
        }


    }
    private String saveImage(Bitmap image, String filename) {
        String savedImagePath = null;
        String name = filename;

        String imageFileName = name ;
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/Pictures", "Artfolio");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
          //  galleryAddPic(savedImagePath);
//            Toast.makeText(getContext(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


}
