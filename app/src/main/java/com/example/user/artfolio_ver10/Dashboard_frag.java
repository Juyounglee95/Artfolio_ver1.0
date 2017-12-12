package com.example.user.artfolio_ver10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;



/**
 * Created by user on 2017-11-25.
 */

public class Dashboard_frag extends android.support.v4.app.Fragment {
    picmoreListner picmoreListner;
    TransferUtility transferUtility;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static final int MY_PERMISSION_CAMERA = 3;

    private static final int VIDEO_PICK_FROM_GALLERY = 4;
    private static final int VIDEO_PICK_FROM_CAMERA = 5;
    String user_id;
    String user_email;
    String imgUrl;
    String mode;
    String profile;
    int picnum;
    int vidnum;
    ImageView picture1, picture2, picture3, picture4;
    Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    Download_thumnail thumnailtask;
    String pic_names[];
    String vid_names[];
    String thum_piclist[];
    String thum_vidlist[];
    String picmore_list[];
    String vidmore_list[];
    ArrayList<dash_thum_pic> data;
    ArrayList<dash_thum_vid> vid_data;
    AmazonS3 s3;
    private RecyclerView lecyclerView, vidRecyclerView;
    public RequestManager mGlideRequestManager;


    public interface picmoreListner{ // list view 에 array 전달
        void delieverPicList(String[] piclist);
    }
    public static Dashboard_frag newInstance() {
        Dashboard_frag fragment = new Dashboard_frag();
        return fragment;
    }

    public Dashboard_frag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            picmoreListner=(picmoreListner)activity;
        }catch (ClassCastException e){
            System.out.println("Error");
        }
    }
    @Override
    public void onResume(){
        super.onResume();
       // android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
       // transaction.detach(this).attach(this).commit();
        //pic_names= ((MainActivity_user)getActivity()).update_list();

        Log.e("RESUME", "update");
        picmore_list= ((MainActivity_user)getActivity()).update_list();
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                "ap-northeast-1:e511d75d-2f61-4459-9ea1-e388bfaa5be0", // Identity pool ID
                Regions.AP_NORTHEAST_1 // Region
        );
        s3 = new AmazonS3Client(credentialsProvider);

        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        transferUtility = new TransferUtility(s3, getContext());
       //
       // set_thumnail();
      //  picmore_list= ((MainActivity_user)getActivity()).update_list();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mode = getArguments().getString("initial");
        profile=getArguments().getString("profile");
        user_id = getArguments().getString("id");
        user_email = getArguments().getString("email");
        picnum= getArguments().getInt("picnum");
        vidnum = getArguments().getInt("vidnum");
        if(mode.equals("notFirst_vidnull")) {
            pic_names = getArguments().getStringArray("piclist");

        }else if(mode.equals("notFirst")){
            pic_names = getArguments().getStringArray("piclist");
            vid_names = getArguments().getStringArray("vidlist");
        }else if(mode.equals("First")){
            vid_names = getArguments().getStringArray("vidlist");
        }
     //   pic_names= ((MainActivity_user)getActivity()).update_list();
        Log.e("onCreateView", "View Create");
        picmore_list= ((MainActivity_user)getActivity()).update_list();
        vidmore_list= ((MainActivity_user)getActivity()).update_vidlist();

        View view = inflater.inflate(R.layout.dashboard_frag, null);
        ImageView profile_view = (ImageView)view.findViewById(R.id.profile_picture);
        if(profile.equals("null")){

        }else {
            Glide.with(getContext()).load( profile).into(profile_view);
        }
        ImageButton pic_add = (ImageButton) view.findViewById(R.id.pic_add);
        ImageButton list_pic = (ImageButton)view.findViewById(R.id.pic_list);
        ImageButton vid_add = (ImageButton) view.findViewById(R.id.vid_add);
        ImageButton list_vid = (ImageButton)view.findViewById(R.id.vid_list);
        //Button refresh = (Button)view.findViewById(R.id.btn_refresh);
        TextView userid = (TextView) view.findViewById(R.id.ID);
        TextView useremail = (TextView) view.findViewById(R.id.email);
        TextView picnum_view = (TextView) view.findViewById(R.id.pic_totalnum);
//        picture1 = (ImageView) view.findViewById(R.id.picture1);
//        picture2 = (ImageView) view.findViewById(R.id.picture2);
//        picture3 = (ImageView) view.findViewById(R.id.picture3);
//        picture4 = (ImageView) view.findViewById(R.id.picture4);
        //HorizontalListView thum_picList = (HorizontalScrollView)view.findViewById(R.id.thum_piclist);
        lecyclerView = (RecyclerView)view.findViewById(R.id.dash_thum_piclist);
         data = new ArrayList<>(); //picture thumbnail 설정
        if(pic_names!=null) {
            thum_piclist = new String[pic_names.length];
            for (int i = 0; i < pic_names.length; i++) {

                thum_piclist[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[i];

            }

            String[] reverse_thumlist = new String[thum_piclist.length];
            for (int i = thum_piclist.length; i > 0; i--) {
                reverse_thumlist[thum_piclist.length - i] = thum_piclist[i - 1].toString();

            }
            if (thum_piclist.length > 4) {
                for (int i = 0; i < 4; i++) {
                    data.add(new dash_thum_pic(reverse_thumlist[i]));

                }

            } else {
                for (int i = 0; i < thum_piclist.length; i++) {
                    data.add(new dash_thum_pic(reverse_thumlist[i]));

                }
            }
        }
        if(data!=null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
          //  thum_adapter.setItems(data);
            mGlideRequestManager = Glide.with(Dashboard_frag.this);
            dash_thum_adapter thum_adapter = new dash_thum_adapter(getContext(), data, R.layout.dash_thum_picitem,mGlideRequestManager);
            lecyclerView.setLayoutManager(layoutManager);


          //  Collections.reverse(data);
            thum_adapter.setItems(data);
        //   thum_adapter.notifyDataSetChanged();
            lecyclerView.setAdapter(thum_adapter);
        }
        //lecyclerView.setItemAnimator(new DefaultItemAnimator());
        vidRecyclerView = (RecyclerView)view.findViewById(R.id.dash_thum_vidlist);
        vid_data = new ArrayList<>(); //picture thumbnail 설정
        if(vid_names!=null) {
            thum_vidlist = new String[vid_names.length];
            for (int i = 0; i <vid_names.length; i++) {

                thum_vidlist[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + vid_names[i];

            }

            String[] reverse_vidthumlist = new String[thum_vidlist.length];
            for (int i = thum_vidlist.length; i > 0; i--) {
                reverse_vidthumlist[thum_vidlist.length - i] = thum_vidlist[i - 1].toString();

            }
            if (thum_vidlist.length > 4) {
                for (int i = 0; i < 4; i++) {
                    vid_data.add(new dash_thum_vid(reverse_vidthumlist[i]));

                }

            } else {
                for (int i = 0; i < thum_vidlist.length; i++) {
                    vid_data.add(new dash_thum_vid(reverse_vidthumlist[i]));

                }
            }
        }
        if(vid_data!=null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //  thum_adapter.setItems(data);
            mGlideRequestManager = Glide.with(Dashboard_frag.this);
            dash_thum_vidadapter thum_vidadapter = new dash_thum_vidadapter(getContext(), vid_data, R.layout.dash_thum_viditem,mGlideRequestManager);
            vidRecyclerView.setLayoutManager(layoutManager);


            //  Collections.reverse(data);
            thum_vidadapter.setItems(vid_data);
            //   thum_adapter.notifyDataSetChanged();
            vidRecyclerView.setAdapter(thum_vidadapter);
        }






        userid.setText(user_id);
        useremail.setText(user_email);
        picnum_view.setText(Integer.toString(picnum));
        TextView vidnumview = (TextView)view.findViewById(R.id.vid_num);
        vidnumview.setText(Integer.toString(vidnum));

      //      Glide.with(getActivity()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[0]).into(picture1);
       // Glide.with(getActivity()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[1]).into(picture2);

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

////
        list_pic.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            picmore_list= get_updateList();
                                            if(picmore_list!=null) {
                                                Intent intent = new Intent(getActivity(), Picmore_listviewActivity.class);
                                                intent.putExtra("picmorelist", picmore_list);
                                                startActivity(intent);
                                            }
                                        }
                                    }

        );
        vid_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), getImage.class);
//                startActivity(intent);
                //((getImage)getActivity()).checkPermission();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("업로드할 비디오 선택")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                video_camera();
                            }
                        })
                        .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                video_gallery();
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
        list_vid.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            vidmore_list= get_updatevidList();
                                            if(vidmore_list!=null) {
                                                Intent intent = new Intent(getActivity(), vidmore_listviewActivity.class);
                                                intent.putExtra("vidmorelist", vidmore_list);
                                                startActivity(intent);
                                            }
                                        }
                                    }

        );

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
    public void video_gallery(){
        Toast.makeText(getActivity(), "Pick video from your gallery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), getvideo.class);
        intent.putExtra("index", VIDEO_PICK_FROM_GALLERY);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void video_camera(){
        Toast.makeText(getActivity(), "Pick video from your gallery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), getvideo.class);
        intent.putExtra("index", VIDEO_PICK_FROM_CAMERA);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
    public String[] get_updateList(){
        return this.picmore_list;
    }
    public String[] get_updatevidList(){
        return this.vidmore_list;
    }

    class Download_thumnail extends AsyncTask<String, Integer, Bitmap> {

       // private List<S3ObjectSummary> s3ObjList;

        protected Bitmap doInBackground(String... urls) {

            for(int i=0 ;i<pic_names.length; i++) {
                Glide.with(getActivity()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + pic_names[i]).into(picture1);
            }
            // TODO Auto-generated method stub

            return null;

        }

        protected void onPostExecute(Bitmap img) {

        }


    }

}
