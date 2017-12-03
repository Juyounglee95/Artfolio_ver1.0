package com.example.user.artfolio_ver10;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-12-03.
 */

public class homeAdapter_popular extends RecyclerView.Adapter<homeAdapter_popular.ViewHolder>{

    Context context;
    ArrayList<userlist_item> userlist_items;
   // int itemLayout;
    ImageView popular_profile;
    TextView popular_name;
    homeAdapter_popular.ViewHolder viewHolder;
    RequestManager mRequestManager;
   // String name;
   // String path;

    public homeAdapter_popular(Context context, ArrayList<userlist_item> userlist_items , RequestManager requestManager){
        this.context = context;
        this.userlist_items= userlist_items;

        mRequestManager= requestManager;



    }
    @Override
    public homeAdapter_popular.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userlist_popular_item,viewGroup,false);
       // ViewHolder vh = new ViewHolder(view);
        return new homeAdapter_popular.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
         // super.onBindViewHolder(viewHolder, position);
        userlist_item item =userlist_items.get(position);

        mRequestManager
                .load(userlist_items.get(position).getImage_url())
                .into(viewHolder.img);

        viewHolder.text.setText(item.getUser_name());

//        viewHolder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userlist_item item =userlist_items.get(position);
//
//                // Toast.makeText(context, "Recycle Click" , Toast.LENGTH_SHORT).show();
//                name = piclist_itemArrayList.get(position).getImage_name();
//                path = piclist_itemArrayList.get(position).getImage_url();
//                setMemo();
//
//
//            }
//        });
//        viewHolder.text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                piclist_item item =piclist_itemArrayList.get(position);
//
//                name = piclist_itemArrayList.get(position).getImage_name();
//                path = piclist_itemArrayList.get(position).getImage_url();
//                setMemo();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return userlist_items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView text;
        //  public TextView textTitle;

        public ViewHolder(View itemView){
            super(itemView);
            //   item = itemView;
            img = (ImageView) itemView.findViewById(R.id.popular_profile);
            text= (TextView) itemView.findViewById(R.id.popular_name);
        }

    }
}
