package com.example.user.artfolio_ver10;

import android.content.Intent;
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

public class vid_detail extends AsyncTask<String, Integer, String> {


    protected String doInBackground(String... path){

        String data = "";
        String name = path[0].substring(61);
        String value = "path="+name+"";
        Log.e("POST",value);
        try {
            URL url = new URL("http://54.226.200.206/get_vidmemo.php");
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

    }

}