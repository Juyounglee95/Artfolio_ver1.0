package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
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
        TextView id_view, num_view, fa_num_view;
        ArrayList<otherdash_item> data;
        RecyclerView otherdash_view;
        GridLayoutManager gridLayoutManager;
        RequestManager mGlideRequestManager;
        otherdash_adapter otherdash_adapter;
        ImageButton bt_fa;
        String fa_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherdash_activity);
        Intent intent = getIntent();
        loginID= intent.getExtras().getString("loginID");
        id = intent.getExtras().getString("id");
        piclist = intent.getExtras().getStringArray("piclist");
        listsize= intent.getExtras().getInt("picnum");
        added = intent.getExtras().getBoolean("fa_added");
        fa_num = intent.getExtras().getString("fa_num");
        id_view= (TextView)findViewById(R.id.other_ID);
        num_view=(TextView)findViewById(R.id.other_totalnum);
        bt_fa=(ImageButton)findViewById(R.id.bt_addfa);
        fa_num_view=(TextView)findViewById(R.id.fa_num);

        if(added==true){
            bt_fa.setEnabled(false);
            bt_fa.setImageResource(R.drawable.heart_pink);
            fa_num_view.setText(fa_num);
        }

        id_view.setText(id);
        num_view.setText(Integer.toString(listsize));
        image_list= new String[piclist.length];
        data = new ArrayList<>();
        //setToUrl(name_list);
        if(piclist!=null) {
            for (int i = 0; i < piclist.length; i++) {

                image_list[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + piclist[i];
                //System.out.println(image_list[i]);
                //System.out.println("name/////"+name_list[i]);

            }
        }
        for(int i=0; i<image_list.length; i++) {
            data.add(new otherdash_item(image_list[i]));
            System.out.println("data: "+data.get(i).getImage_url());
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
            otherdash_adapter = new otherdash_adapter(data, mGlideRequestManager);
            otherdash_view.setAdapter(otherdash_adapter);
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
