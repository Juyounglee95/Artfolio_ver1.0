package com.example.user.artfolio_ver10;

import android.app.AlertDialog;
import android.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by user on 2017-11-25.
 */

public class Dashboard_frag extends android.support.v4.app.Fragment {
    TransferUtility transferUtility;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static final int MY_PERMISSION_CAMERA = 3;
    String user_id;
    String user_email;
    String imgUrl;
    int picnum;
    ImageView picture1, picture2, picture3, picture4;
    Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    Download_thumnail thumnailtask;
    String pic_names[];
    private ArrayList<HashMap<String, Object>> transferRecordMaps;
    AmazonS3 s3;

    public static Dashboard_frag newInstance() {
        Dashboard_frag fragment = new Dashboard_frag();
        return fragment;
    }

    public Dashboard_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:e511d75d-2f61-4459-9ea1-e388bfaa5be0", // Identity pool ID
                Regions.AP_NORTHEAST_1 // Region
        );
        s3 = new AmazonS3Client(credentialsProvider);

        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        transferUtility = new TransferUtility(s3, getApplicationContext());
       //
       // set_thumnail();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user_id = getArguments().getString("id");
        user_email = getArguments().getString("email");
        picnum= getArguments().getInt("picnum");
        pic_names = getArguments().getStringArray("piclist");

        View view = inflater.inflate(R.layout.dashboard_frag, null);
        ImageButton pic_add = (ImageButton) view.findViewById(R.id.pic_add);
        ImageButton list_pic = (ImageButton)view.findViewById(R.id.list1);
        TextView userid = (TextView) view.findViewById(R.id.ID);
        TextView useremail = (TextView) view.findViewById(R.id.email);
        TextView picnum_view = (TextView) view.findViewById(R.id.pic_totalnum);
        picture1 = (ImageView) view.findViewById(R.id.picture1);
        picture2 = (ImageView) view.findViewById(R.id.picture2);
        picture3 = (ImageView) view.findViewById(R.id.picture3);
        picture4 = (ImageView) view.findViewById(R.id.picture4);



        userid.setText(user_id);
        useremail.setText(user_email);
        picnum_view.setText(Integer.toString(picnum));

            Glide.with(getActivity()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[0]).into(picture1);
        Glide.with(getActivity()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[1]).into(picture2);

        // ((액티비티 클래스이름)getActivity()).액티비티의public메소드(); 이런식으로 하시면됩니다.
        pic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), getImage.class);
//                startActivity(intent);
                //((getImage)getActivity()).checkPermission();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("업로드할 이미지 선택")
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
//        list_pic.setOnClickListener(
//
//        );
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.dashboard_frag, container, false);
        return view;
    }
//    public void set_thumnail(){
//        Download_thumnail download_thumnail = new Download_thumnail();
//        download_thumnail.execute();
//    }
    public void act_gallery() {
        Toast.makeText(getActivity(), "Pick Image from your gallery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), getImage.class);
        intent.putExtra("index", PICK_FROM_GALLERY);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void act_camera() {
        Toast.makeText(getActivity(), "Pick Image from your camera", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), getImage.class);
        intent.putExtra("index", PICK_FROM_CAMERA);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    class Download_thumnail extends AsyncTask<String, Integer, Bitmap> {

       // private List<S3ObjectSummary> s3ObjList;

        protected Bitmap doInBackground(String... urls) {

            for(int i=0 ;i<pic_names.length; i++) {
                Glide.with(getActivity()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[i]).into(picture1);
            }
            // TODO Auto-generated method stub
//            s3ObjList = s3.listObjects("artfolio-imageupload").getObjectSummaries();
//            transferRecordMaps.clear();
//            for (S3ObjectSummary summary : s3ObjList) {
//                HashMap<String, Object> map = new HashMap<String, Object>();
//                map.put("key", summary.getKey());
//                transferRecordMaps.add(map);
//            }
            return null;

        }

        protected void onPostExecute(Bitmap img) {

        }


//    public void act_getImage(View v){
//
//        new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택")
//                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        act_camera();
//                    }
//                })
//                .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        act_gallery();
//                    }
//                })
//                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .show();
//    }
    }

}
