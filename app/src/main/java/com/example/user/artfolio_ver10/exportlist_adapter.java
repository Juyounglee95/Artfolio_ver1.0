package com.example.user.artfolio_ver10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * Created by user on 2017-12-09.
 */

public class exportlist_adapter extends RecyclerView.Adapter<exportlist_adapter.ViewHolder>{


        Context context;
        ArrayList<exportlist_item> exportlist_items;
        int itemLayout;
        ImageView pic_image;
        TextView pic_name;
        com.example.user.artfolio_ver10.PicmoreList_Adapter.ViewHolder viewHolder;
        RequestManager mRequestManager;
        String name;
        String path;
        String memo;
        // View item;
        public exportlist_adapter(Context context, ArrayList<exportlist_item> exportlist_items , int itemLayout, RequestManager requestManager){
            this.context = context;
            this.exportlist_items = exportlist_items;
            this.itemLayout = itemLayout;
            mRequestManager= requestManager;



        }
        public ArrayList<exportlist_item> getExport_list(){
            return exportlist_items;
        }
        public exportlist_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
            return new ViewHolder(view);
        }

        public void setMemo(){
            pic_detail pic_detail = new pic_detail();
            try{
                memo=pic_detail.execute(path).get();

            }catch (Exception e){
                e.printStackTrace();
            }
            //   String memo = data;
            Intent intent = new Intent(context, pic_detailActivity.class);
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
            final exportlist_item item =exportlist_items.get(position);

            mRequestManager
                    .load(exportlist_items.get(position).getImage_url())
                    .into(viewHolder.img);

            viewHolder.text.setText(item.getImage_name());

            viewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // piclist_item item =exportlist_items.get(position);

                    // Toast.makeText(context, "Recycle Click" , Toast.LENGTH_SHORT).show();
                    name = exportlist_items.get(position).getImage_name();
                    path = exportlist_items.get(position).getImage_url();
                    setMemo();


                }
            });


                viewHolder.cb.setOnCheckedChangeListener(null);

//if true, your checkbox will be selected, else unselected
                viewHolder.cb.setChecked(item.isSelected());

                viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //set your object's last status
                        item.setSelected(isChecked);
                    }
                });







        }

        @Override
        public int getItemCount() {
            return exportlist_items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{

            public ImageView img;
            public TextView text;
            public CheckBox cb;
            public CardView cv;
            //  public TextView textTitle;

            public ViewHolder(View itemView){
                super(itemView);
                //   item = itemView;
                cv = (CardView)itemView.findViewById(R.id.cvItems);
                cb = (CheckBox)itemView.findViewById(R.id.export_check);
                img = (ImageView) itemView.findViewById(R.id.picmore_item_image);
                text= (TextView) itemView.findViewById(R.id.picmore_item_text);
            }

        }
}







