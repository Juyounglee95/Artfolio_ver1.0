package com.example.user.artfolio_ver10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by user on 2017-11-28.
 */

public class PicmoreList_Adapter extends BaseAdapter{

    Context context;
    ArrayList<piclist_item> piclist_itemArrayList;
    ImageView pic_image;
    TextView pic_name;
    ViewHolder viewHolder;
    public PicmoreList_Adapter(Context context, ArrayList<piclist_item> piclist_itemArrayList) {
        if(piclist_itemArrayList!= null) {
            this.context = context;
            this.piclist_itemArrayList = piclist_itemArrayList;
        }else{
            System.out.print("null error");
        }
    }

    @Override
    public int getCount() {
       return  this.piclist_itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return piclist_itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=
                    LayoutInflater.from(context).inflate(R.layout.picmore_item, null);
            viewHolder = new ViewHolder();
           viewHolder.pic_image = (ImageView)convertView.findViewById(R.id.picmore_item_image);
            viewHolder.pic_name = (TextView)convertView.findViewById(R.id.picmore_item_text);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
       // viewHolder.pic_image.setImageResource(piclist_itemArrayList.get(i).getImage_url());
        Glide.with(context).load(piclist_itemArrayList.get(i).getImage_url()).into(viewHolder.pic_image);
        viewHolder.pic_name.setText(piclist_itemArrayList.get(i).getImage_name());

        return convertView;
    }
    class ViewHolder{
        ImageView pic_image;
        TextView pic_name;
    }
}
