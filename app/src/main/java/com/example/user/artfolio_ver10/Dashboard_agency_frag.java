package com.example.user.artfolio_ver10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by user on 2017-11-25.
 */

public class Dashboard_agency_frag extends android.support.v4.app.Fragment {
    picmoreListner picmoreListner;
    TransferUtility transferUtility;

    String user_id;
    String user_email;
    String memo;
    String mode;
    String profile;
    String introduction;
    String path;
    String intro_image;
    String pic_names[];
    AmazonS3 s3;

    public interface picmoreListner{ // list view 에 array 전달
        void delieverPicList(String[] piclist);
    }
    public static Dashboard_agency_frag newInstance() {
        Dashboard_agency_frag fragment = new Dashboard_agency_frag();
        return fragment;
    }

    public Dashboard_agency_frag() {
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
        Log.e("RESUME", "update");
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //introduction = get_introduction.

        mode = getArguments().getString("initial");
        profile = getArguments().getString("profile");
        user_id = getArguments().getString("id");
        user_email = getArguments().getString("email");
        get_introduction get_introduction = new get_introduction();
        try{
            introduction=get_introduction.execute().get();


            if(introduction.equals("ERROR")){
                introduction="\n\n\n\n\nCOMPANY INTRODUCTION";
            }else{
                String intro [] = introduction.split("%");
                introduction = intro[0];
                intro_image= intro[1];
            }

            Log.e("알고싶다", introduction);
        }catch (Exception e){
            e.getStackTrace();
        }
        try{
            Log.e("try >", mode);
            Log.e("try >", profile);
            Log.e("try >", user_id);
            Log.e("try >", user_email);
            Log.e("try >", memo);
        }catch (NullPointerException e){
            Log.e("catch >", e.toString());
        }


        if (mode.equals("notFirst")) {
            pic_names = getArguments().getStringArray("piclist");
        }


        //   pic_names= ((MainActivity_user)getActivity()).update_list();
        Log.e("onCreateView", "View Create");
        View view = inflater.inflate(R.layout.dashboard_agency_frag, null);
        ImageView profile_view = (ImageView) view.findViewById(R.id.profile_picture);
        if (profile.equals("null")) {

        } else {
            Glide.with(getContext()).load(profile).into(profile_view); //수정
        }

        Button btnModify = (Button) view.findViewById(R.id.modify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), dash_modify_agency.class);
                intent.putExtra("id", user_id);
                intent.putExtra("memo", introduction);
                intent.putExtra("image", intro_image);
                startActivity(intent);
            }
        });
        ImageView main_image = (ImageView) view.findViewById(R.id.intro_image);

        TextView userid = (TextView) view.findViewById(R.id.ID);
        TextView useremail = (TextView) view.findViewById(R.id.email);
        TextView introduction_view = (TextView) view.findViewById(R.id.text_introduction);

        userid.setText(user_id);
        useremail.setText(user_email);
        introduction_view.setText(introduction);
        System.out.println("INTROOOOOO"+intro_image);
        if (intro_image.equals("null")) {

        } else {
            Glide.with(getContext()).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+intro_image).into(main_image); //서버 패스로 수정
        }
        return view;
    }

    class get_introduction extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... unuesed) {
            String data = "";
            String value = "ID=" + user_id + "";

            try {
                URL url = new URL("http://54.226.200.206/get_introduction.php");
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
                while ((line = in.readLine()) != null) {
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
            super.onPostExecute(data);
        }
    }
}
