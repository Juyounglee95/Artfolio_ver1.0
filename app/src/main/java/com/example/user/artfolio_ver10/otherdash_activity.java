package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class otherdash_activity extends AppCompatActivity {
        String id;
        String loginID;
        int listsize;
        Boolean added = false;
        String [] piclist;
        String [] image_list;
        String [] vidlist;
        String [] vidpath_list;
        String profile_path;
        TextView id_view, num_view, fa_num_view;
        ImageView other_profile;
        ArrayList<otherdash_item> data;
        ArrayList<otherdash_item> viddata;
        RecyclerView otherdash_view;
        RecyclerView otherdash_vidview;
        GridLayoutManager gridLayoutManager;
        RequestManager mGlideRequestManager;
        otherdash_adapter otherdash_adapter;
        ImageButton bt_fa;
        String fa_num;
        int vidlistsize;
        TextView vidnum_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherdash_activity);
        Intent intent = getIntent();
        loginID= intent.getExtras().getString("loginID");
        id = intent.getExtras().getString("id");
        piclist = intent.getExtras().getStringArray("piclist");
        listsize= intent.getExtras().getInt("picnum");
        vidlistsize = intent.getExtras().getInt("vidnum");
        added = intent.getExtras().getBoolean("fa_added");
        fa_num = intent.getExtras().getString("fa_num");
        profile_path = intent.getExtras().getString("profile");
        vidlist = intent.getExtras().getStringArray("vidlist");
        id_view= (TextView)findViewById(R.id.other_ID);
        num_view=(TextView)findViewById(R.id.other_totalnum);
        bt_fa=(ImageButton)findViewById(R.id.bt_addfa);
        fa_num_view=(TextView)findViewById(R.id.fa_num);
        other_profile=(ImageView)findViewById(R.id.other_profile);
        vidnum_view=(TextView)findViewById(R.id.other_vidnum);
        if(profile_path.equals("null")){

        }else {
            Glide.with(otherdash_activity.this).load( profile_path).into(other_profile);
        }
        if(added==true){
            bt_fa.setEnabled(false);
            bt_fa.setImageResource(R.drawable.heart_pink);
            fa_num_view.setText(fa_num);
        }

        id_view.setText(id);
        num_view.setText(Integer.toString(listsize));
        vidnum_view.setText(Integer.toString(vidlistsize));
        image_list= new String[piclist.length];
        data = new ArrayList<>();
        //setToUrl(name_list);
        if(!piclist[0].equals("null")) {
            for (int i = 0; i < piclist.length; i++) {

                image_list[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + piclist[i];
                //System.out.println(image_list[i]);
                //System.out.println("name/////"+name_list[i]);

            }
            for(int i=0; i<image_list.length; i++) {
                data.add(new otherdash_item(image_list[i]));
                System.out.println("data: "+data.get(i).getImage_url());
            }
        }else{
            num_view.setText(Integer.toString(0));
        }


//       // data = new ArrayList<>();
//        for(int i=0; i<piclist.length; i++){
//            data.add(new otherdash_item(piclist[i]));
//            //   System.out.println(profilelist[i] + "///"+ userlist[i]);
//        }
        otherdash_view = (RecyclerView)findViewById(R.id.other_dashthumb);
        gridLayoutManager = new GridLayoutManager(this,3);

        otherdash_view.setLayoutManager(gridLayoutManager);
        mGlideRequestManager = Glide.with(this);
        if(data!=null) {
            otherdash_adapter = new otherdash_adapter("pic",data, mGlideRequestManager);
            otherdash_view.setAdapter(otherdash_adapter);
        }
        ///vid list
        vidpath_list= new String[vidlist.length];
        viddata = new ArrayList<>();
        if(!vidlist[0].equals("null")) {
            for (int i = 0; i < vidlist.length; i++) {

                vidpath_list[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + vidlist[i];
                //System.out.println(image_list[i]);
                //System.out.println("name/////"+name_list[i]);

            }
            for(int i=0; i<vidpath_list.length; i++) {
                viddata.add(new otherdash_item(vidpath_list[i]));
                System.out.println("data: "+viddata.get(i).getImage_url());
            }
        }else{vidnum_view.setText(Integer.toString(0));}



//       // data = new ArrayList<>();
//        for(int i=0; i<piclist.length; i++){
//            data.add(new otherdash_item(piclist[i]));
//            //   System.out.println(profilelist[i] + "///"+ userlist[i]);
//        }
        otherdash_vidview = (RecyclerView)findViewById(R.id.other_dashvidthumb);
        gridLayoutManager = new GridLayoutManager(this,3);

        otherdash_vidview.setLayoutManager(gridLayoutManager);
        mGlideRequestManager = Glide.with(this);
        if(viddata!=null) {
            otherdash_adapter = new otherdash_adapter("vid",viddata, mGlideRequestManager);
            otherdash_vidview.setAdapter(otherdash_adapter);
        }


    }
    public void add_favorite(View view){
        favorite_db favorite_db = new favorite_db();
        String value = loginID+","+id;
        try{

          String fa_num=  favorite_db.execute(value).get();
          bt_fa.setEnabled(false);
          bt_fa.setImageResource(R.drawable.heart_pink);
          fa_num_view.setText(fa_num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
