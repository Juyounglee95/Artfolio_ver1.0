package com.example.user.artfolio_ver10;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * Created by user on 2017-11-28.
 */

public class dash_thum_adapter extends RecyclerView.Adapter<dash_thum_adapter.ViewHolder>
 {
     private ArrayList<dash_thum_pic> dash_thum_pics;
     private int itemLayout;
     private Context context;
     RequestManager mRequestManager;
     /**
      * 생성자
      * @param
      * @param
      */
     public dash_thum_adapter(ArrayList<dash_thum_pic> dash_thum_pics , int itemLayout, RequestManager requestManager){

         this.dash_thum_pics = dash_thum_pics;
         this.itemLayout = itemLayout;
         mRequestManager= requestManager;



     }
     public void setItems(ArrayList<dash_thum_pic> dash_thum_pics) {
         this.dash_thum_pics = dash_thum_pics;
         notifyDataSetChanged();
     }

     /**
      * 레이아웃을 만들어서 Holer에 저장
      * @param viewGroup
      * @param viewType
      * @return
      */
     @Override
     public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

         View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
         return new ViewHolder(view);
     }

     /**
      * listView getView 를 대체
      * 넘겨 받은 데이터를 화면에 출력하는 역할
      *
      * @param viewHolder
      * @param position
      */
     @Override
     public void onBindViewHolder(ViewHolder viewHolder, int position) {

         dash_thum_pic item = dash_thum_pics.get(position);

         mRequestManager
                 .load(dash_thum_pics.get(position).getImage_url())
                 .into(viewHolder.img);



     }

     @Override
     public int getItemCount() {
         return dash_thum_pics.size();
     }

     /**
      * 뷰 재활용을 위한 viewHolder
      */
     public static class ViewHolder extends RecyclerView.ViewHolder{

         public ImageView img;
       //  public TextView textTitle;

         public ViewHolder(View itemView){
             super(itemView);

             img = (ImageView) itemView.findViewById(R.id.thum_item_image);
          //   textTitle = (TextView) itemView.findViewById(R.id.textTitle);
         }

     }
 }




