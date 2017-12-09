package com.example.user.artfolio_ver10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-11-28.
 */

public class vidmoreList_Adapter extends RecyclerView.Adapter<vidmoreList_Adapter.ViewHolder>{

    Context context;
    ArrayList<vidlist_item> vidlist_items;
    int itemLayout;
    ImageView vid_image;
    TextView vid_name;
    ViewHolder viewHolder;
    RequestManager mRequestManager;
    String name;
    String path;
    String memo;
    // View item;
    public vidmoreList_Adapter(Context context, ArrayList<vidlist_item> vidlist_items , int itemLayout, RequestManager requestManager){
        this.context = context;
        this.vidlist_items= vidlist_items;
        this.itemLayout = itemLayout;
        mRequestManager= requestManager;



    }
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
        return new ViewHolder(view);
    }

    public void setMemo(){
        vid_detail vid_detail = new vid_detail();
        try{
            memo=vid_detail.execute(path).get();

        }catch (Exception e){
            e.printStackTrace();
        }
        //   String memo = data;
        Intent intent = new Intent(context, vid_detailActivity.class);
        intent.putExtra("path", path);
        String name = path.substring(61);
        intent.putExtra("name",name);
        intent.putExtra("memo", memo);
        intent.putExtra("mode", "mine");
        context.startActivity(intent);
//        //   // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();
        //   return data;pic_detail.execute();
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //  super.onBindViewHolder(viewHolder, position);
        final vidlist_item item =vidlist_items.get(position);

        mRequestManager
                .load(vidlist_items.get(position).getVideo_url())
                .into(viewHolder.img);

        viewHolder.text.setText(item.getVideo_name());

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vidlist_item item =vidlist_items.get(position);

                // Toast.makeText(context, "Recycle Click" , Toast.LENGTH_SHORT).show();
                name = vidlist_items.get(position).getVideo_name();
                path = vidlist_items.get(position).getVideo_url();
                setMemo();


            }
        });
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vidlist_item item =vidlist_items.get(position);
                name =vidlist_items.get(position).getVideo_name();
                path = vidlist_items.get(position).getVideo_url();
                setMemo();
            }
        });








    }

    @Override
    public int getItemCount() {
        return vidlist_items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView text;

        //  public TextView textTitle;

        public ViewHolder(View itemView){
            super(itemView);
            //   item = itemView;

            img = (ImageView) itemView.findViewById(R.id.picmore_item_image);
            text= (TextView) itemView.findViewById(R.id.picmore_item_text);
        }

    }


}


