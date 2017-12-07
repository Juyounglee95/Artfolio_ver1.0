package com.example.user.artfolio_ver10;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class main_login extends AppCompatActivity {
    EditText edit_id, edit_pw;
    String user_id, user_pw;

    String [] wholeuser_name;
    String [] whole_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);
        edit_id = (EditText)findViewById(R.id.edit_id);
        edit_pw = (EditText)findViewById(R.id.edit_pw);
        Intent intent = getIntent();


    }

    public void act_login(View view){
        try {
            user_id = edit_id.getText().toString();
            user_pw = edit_pw.getText().toString();
            loginDB loginDB = new loginDB();
            loginDB.execute();


        }
        catch (NullPointerException ei) {
            ei.printStackTrace();
        }

    }
    public void act_back(View v){
        this.finish();
    }

    public void act_signup(View view){
        Intent intent = new Intent(this, Sign_up.class);
        startActivity(intent);
        //finish();
    }
    public void getUserInfo(String data){
        try {
            String string = data;
            String[] userInfo = string.split(",");
            String name = userInfo[0]; // name
            String id = userInfo[1]; // ID
            String phone = userInfo[2]; // phone
            String email = userInfo[3]; // email
            String profile = userInfo[4];
//            for(int i =0 ; i <userInfo.length; i++){
//                System.out.println(userInfo[i]);
//            }
            String [] list = userInfo[5].split("<br>");
            wholeuser_name= new String[list.length];
            whole_profile= new String[list.length];
            for(int i =0; i<list.length; i++){
                String[] strings = list[i].split("/");
                wholeuser_name[i]=strings[0];
                whole_profile[i]= strings[1];
            }

            Intent intent= new Intent(main_login.this, MainActivity_user.class);
            intent.putExtra("name", name);
            intent.putExtra("id", id);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("profile", profile);
            intent.putExtra("wholelist", wholeuser_name);
            intent.putExtra("wholeprofile", whole_profile);
            startActivity(intent);

            finish();


        } catch (NullPointerException e) {
            e.printStackTrace();
            //Log.d(TAG, "showResult : ", e);
        }
    }

    class loginDB extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... unuesed){

            String data = "";
            String value = "ID="+user_id+"&PW="+user_pw+"";
            Log.e("POST",value);
            try {
                URL url = new URL("http://54.226.200.206/login.php");
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
                Log.e("RECV DATA",data);



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

         //   System.out.print(data);
           // data = data.toString();
            if(data.equals("Error")){
                Toast.makeText(getApplicationContext(), "Login Fail. Insert corrent info.", Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(getApplicationContext(), "Login Success. Welcome", Toast.LENGTH_SHORT).show();
                getUserInfo(data);
            }

        }




    }
}
