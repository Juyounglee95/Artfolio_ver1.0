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
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * Created by user on 2017-11-28.
 */

public class PicmoreList_Adapter extends RecyclerView.Adapter<PicmoreList_Adapter.ViewHolder>{

    Context context;
    ArrayList<piclist_item> piclist_itemArrayList;
    int itemLayout;
    ImageView pic_image;
    TextView pic_name;
    ViewHolder viewHolder;
    RequestManager mRequestManager;
    public PicmoreList_Adapter(ArrayList<piclist_item> piclist_items , int itemLayout, RequestManager requestManager){

        this.piclist_itemArrayList= piclist_items;
        this.itemLayout = itemLayout;
        mRequestManager= requestManager;



    }
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //  super.onBindViewHolder(viewHolder, position);
        piclist_item item =piclist_itemArrayList.get(position);

        mRequestManager
                .load(piclist_itemArrayList.get(position).getImage_url())
                .into(viewHolder.img);

        viewHolder.text.setText(item.getImage_name());

    }

    @Override
    public int getItemCount() {
        return piclist_itemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView text;
        //  public TextView textTitle;

        public ViewHolder(View itemView){
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.picmore_item_image);
               text= (TextView) itemView.findViewById(R.id.picmore_item_text);
        }

    }
}
