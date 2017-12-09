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
 * Created by user on 2017-12-10.
 */

public class vidList_db extends AsyncTask<String, Integer, String> {


    protected String doInBackground(String... params){

        String data = "";
        String value = "ID="+params[0]+"";
        Log.e("POST",value);
        try {
            URL url = new URL("http://54.226.200.206/down_vidInfo.php"); //picture list 정보랑 video list정보 다운로드

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
//        if(data.equals("null")){
//            //Toast.makeText(getApplicationContext(), "There is no files on server", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            getPic_List(data);
//            // Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();
//
//        }

    }




}
