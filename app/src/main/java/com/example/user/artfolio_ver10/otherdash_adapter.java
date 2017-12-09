package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * Created by user on 2017-12-04.
 */

public class otherdash_adapter extends RecyclerView.Adapter<otherdash_adapter.ViewHolder> {
    ArrayList<otherdash_item> otherdash_items;
    ImageView popular_profile;
    String memo, path;
    otherdash_adapter.ViewHolder viewHolder;
    RequestManager mRequestManager;
    public View view;
    String pic;
    public otherdash_adapter(String pic, ArrayList<otherdash_item> otherdash_items, RequestManager requestManager ) {
        this.pic = pic;
        this.otherdash_items = otherdash_items;

        mRequestManager= requestManager;

    }

    @Override
    public otherdash_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.otherdash_item, parent,false);
        // ViewHolder vh = new ViewHolder(view);
        return new otherdash_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(otherdash_adapter.ViewHolder holder, final int position) {
        otherdash_item item =otherdash_items.get(position);
//        if((userlist_items.get(position).getImage_url()).equals("null")){
//            viewHolder.img.setImageResource(R.drawable.kakao_default_profile_image);
//        }else {
            mRequestManager
                    .load(otherdash_items.get(position).getImage_url())
                    .into(holder.img);
//        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = otherdash_items.get(position).getImage_url();
                setMemo();
            }
        });

    }
    public void setMemo(){
        if(pic.equals("pic")) {
            pic_detail pic_detail = new pic_detail();
            try {
                memo = pic_detail.execute(path).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //   String memo = data;

            Intent intent = new Intent(view.getContext(), pic_detailActivity.class);
            intent.putExtra("path", path);
            String name = path.substring(61);
            intent.putExtra("name", name);
            intent.putExtra("memo", memo);
            intent.putExtra("mode", "otheruser");
            view.getContext().startActivity(intent);
        }else{
            vid_detail vid_detail = new vid_detail();
            try {
                memo = vid_detail.execute(path).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //   String memo = data;

            Intent intent = new Intent(view.getContext(), vid_detailActivity.class);
            intent.putExtra("path", path);
            String name = path.substring(61);
            intent.putExtra("name", name);
            intent.putExtra("memo", memo);
            intent.putExtra("mode", "otheruser");
            view.getContext().startActivity(intent);
        }
//        //   // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();
        //   return data;pic_detail.execute();
    }

    @Override
    public int getItemCount() {
        return otherdash_items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public CardView cv;


        public ViewHolder(View itemView){
            super(itemView);
            view=itemView;
            //   item = itemView;
            cv = (CardView) itemView.findViewById(R.id.other_cardview);
            img = (ImageView) itemView.findViewById(R.id.other_pic);

        }
    }
}
