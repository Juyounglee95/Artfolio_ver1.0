package com.example.user.artfolio_ver10;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class dash_thum_adapter extends RecyclerView.Adapter<dash_thum_adapter.ViewHolder>
 {
     private ArrayList<dash_thum_pic> dash_thum_pics;
     private int itemLayout;
     private Context context;
     String path, memo;
     RequestManager mRequestManager;
     /**
      * 생성자
      * @param
      * @param
      */
     public dash_thum_adapter(Context context,ArrayList<dash_thum_pic> dash_thum_pics , int itemLayout, RequestManager requestManager){
         this.context = context;
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
     public void onBindViewHolder(ViewHolder viewHolder, final int position) {

         dash_thum_pic item = dash_thum_pics.get(position);

         mRequestManager
                 .load(dash_thum_pics.get(position).getImage_url())
                 .into(viewHolder.img);

         viewHolder.img.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dash_thum_pic item = dash_thum_pics.get(position);

                 // Toast.makeText(context, "Recycle Click" , Toast.LENGTH_SHORT).show();
                 path = dash_thum_pics.get(position).getImage_url();

                 setMemo();


             }
         });


     }

     public void setMemo(){
         pic_detail pic_detail = new pic_detail();
         pic_detail.execute();
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
     class pic_detail extends AsyncTask<String, Integer, String> {


         protected String doInBackground(String... unuesed){

             String data = "";
             String name = path.substring(61);
             String value = "path="+name+"";
             Log.e("POST",value);
             try {
                 URL url = new URL("http://54.226.200.206/get_picmemo.php");
                 HttpURLConnection con = (HttpURLConnection) url.openConnection();
                 con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                 con.setRequestMethod("POST");
                 con.setDoInput(true);
                 con.connect();

                 OutputStream outs = con.getOutputStream();
                 outs.write(value.getBytes("UTF-8"));
                 outs.flush();
                 outs.close();


                 InputStream is = null;
                 BufferedReader in = null;


                 is = con.getInputStream();
                 in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                 String line = null;
                 StringBuilder buff = new StringBuilder();
                 while ( ( line = in.readLine() ) != null )
                 {
                     buff.append(line + "\n");
                 }
                 data = buff.toString().trim();
                 //System.out.println(data);
                 Log.e("Path data",data);



             } catch (MalformedURLException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }

             return data;
         }

         protected void onPostExecute(String data) {
             //super.onPostExecute(data);
                 /* 서버에서 응답 */
             //Log.e("RECV DATA",data);

             //System.out.print(data);
             // data = data.toString();
             memo = data;
             Intent intent = new Intent(context, pic_detailActivity.class);
             intent.putExtra("path", path);
             String name = path.substring(61);
             intent.putExtra("name",name);
             intent.putExtra("memo", memo);
             context.startActivity(intent);
             //   // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();

         }

     }

 }




