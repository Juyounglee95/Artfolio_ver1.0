package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import static com.androidquery.util.AQUtility.getContext;

public class Picmore_listviewActivity extends AppCompatActivity  {
    String image_list[];
    String name_list[];
    RequestManager  mGlideRequestManager;
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
            recyclerView.setAdapter(new PicmoreList_Adapter(data, R.layout.picmore_item,mGlideRequestManager));
         //   finish();
        }


    }
//    public void delieverPicList(String[] piclist){
//       // for(int i=0; i<)
//     image_list= piclist;
//     setToUrl(image_list);
//     if(image_list.length>0){
//         for(int i=0; i<image_list.length; i++){
//             System.out.println(image_list[i]);
//         }
//     }
//
//    }
    public void setToUrl(String[] piclist){
       // String[] copylist =piclist;
        if(piclist!=null) {
            for (int i = 0; i < piclist.length; i++) {

                image_list[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + piclist[i];
                //System.out.println(image_list[i]);
                //System.out.println("name/////"+name_list[i]);

            }
        }


    }
}
