package com.example.user.artfolio_ver10;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-12-03.
 */

public class homeAdapter_popular extends RecyclerView.Adapter<homeAdapter_popular.ViewHolder>{
    String loginID;
    Context context;
    ArrayList<userlist_item> userlist_items;
   // int itemLayout;
    ImageView popular_profile;
    TextView popular_name;
    homeAdapter_popular.ViewHolder viewHolder;
    RequestManager mRequestManager;
    String id;
    String []list;
    int picList_size;
   // String name;
   // String path;

    public homeAdapter_popular(Context context, ArrayList<userlist_item> userlist_items , RequestManager requestManager){
        this.context = context;
        this.userlist_items= userlist_items;

        mRequestManager= requestManager;



    }
    public String setLoginID(String loginID){
        return this.loginID = loginID;
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
        if((userlist_items.get(position).getImage_url()).equals("null")){
            viewHolder.img.setImageResource(R.drawable.kakao_default_profile_image);
        }else {
            mRequestManager
                    .load(userlist_items.get(position).getImage_url())
                    .into(viewHolder.img);
        }
        viewHolder.text.setText(item.getUser_name());

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlist_item item =userlist_items.get(position);

               //  Toast.makeText(context, "Recycle Click" , Toast.LENGTH_SHORT).show();
                id = userlist_items.get(position).getUser_name();
//                path = piclist_itemArrayList.get(position).getImage_url();
                get_piclist();


            }
        });
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
    public void get_piclist(){
        picInfo_DB picInfo_db = new picInfo_DB();
        picInfo_db.execute();
    }
    class picInfo_DB extends AsyncTask<String, Integer, String> {


        ProgressDialog asyncDialog = new ProgressDialog(
                context);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Loading..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... unuesed){

            String data = "";
            String value = "ID="+id+"";
            Log.e("POST",value);
            try {


                URL url = new URL("http://54.226.200.206/down_picInfo.php");
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
            super.onPostExecute(data);
                 /* 서버에서 응답 */
            //Log.e("RECV DATA",data);
            asyncDialog.dismiss();
            //System.out.print(data);
            // data = data.toString();
            if(data.equals("null")){
                Toast.makeText(context, "The dashboard is empty", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    String string = data;

                    list = string.split("<br>");

                    picList_size=list.length;
                    favorite_listInfo favorite_listInfo = new favorite_listInfo();
                    Boolean added = false;
                    String fa_num ="0";
                    String value = loginID+","+id;
                    String in_falist = favorite_listInfo.execute(value).get();

                    if(!in_falist.equals("null")){
                        String [] list = in_falist.split("/");
                        for(int i =0; i<list.length-1; i++){
                            if(id.equals(list[i])){
                                added = true;
                                fa_num = list[list.length-1];
                                break;
                            }
                        }

                    }
                    Intent intent = new Intent(context,otherdash_activity.class);
                    intent.putExtra("piclist", list);
                    intent.putExtra("picnum", picList_size);
                    intent.putExtra("id", id);
                    intent.putExtra("fa_num", fa_num);
                    intent.putExtra("fa_added", added);
                    intent.putExtra("loginID", loginID);
                    context.startActivity(intent);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //Log.d(TAG, "showResult : ", e);
                }catch (Exception e ){
                    e.printStackTrace();
                }
                // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();

            }

        }




    }



    @Override
    public int getItemCount() {
        return userlist_items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView text;
        public CardView cv;
        //  public TextView textTitle;

        public ViewHolder(View itemView){
            super(itemView);

            //   item = itemView;
            cv = (CardView) itemView.findViewById(R.id.cardview);
            img = (ImageView) itemView.findViewById(R.id.popular_profile);
            text= (TextView) itemView.findViewById(R.id.popular_name);
        }

    }
}
