package com.example.user.artfolio_ver10;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 2017-12-04.
 */

public class favorite_db extends AsyncTask<String, Integer, String>{

    /**
     * Created by user on 2017-12-04.
     */


        protected String doInBackground(String... param){

            String data = "";
            String [] strings = param[0].split(",");
            String id = strings[0];
            String fa_id = strings[1];
            String value = "ID="+id+"&favorite_id="+fa_id+"";
            Log.e("POST",value);
            try {
                URL url = new URL("http://54.226.200.206/update_favorite.php");
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

            //System.out.print(data);
            // data = data.toString();

            //   String memo = data;
//        Intent intent = new Intent(context, pic_detailActivity.class);
//        intent.putExtra("path", path);
//        String name = path.substring(61);
//        intent.putExtra("name",name);
//        intent.putExtra("memo", memo);
//        context.startActivity(intent);
//        //   // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();
            //   return data;
        }


}
