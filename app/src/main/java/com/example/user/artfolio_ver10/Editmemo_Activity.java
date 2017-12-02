package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Editmemo_Activity extends AppCompatActivity {
    String path, name, memo_text;
    ImageView image ;
    EditText memo;
    TextView imagename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_memo);
        Intent intent = getIntent();
        path = intent.getExtras().getString("path");
        name = intent.getExtras().getString("name");
        memo_text = intent.getExtras().getString("memo");
        //    setMemo();
        image= (ImageView)findViewById(R.id.detail_image);
        memo =(EditText) findViewById(R.id.editor_memo);
        imagename=(TextView)findViewById(R.id.Image_name);
        Glide.with(this)
                .load(path)
                .into(image);
        imagename.setText(name);
        System.out.println(memo_text);
        memo.setText(memo_text);


    }
    public void save_memo(View view){
        memo_text= memo.getText().toString();
        editmemo editmemo = new editmemo();
        editmemo.execute();
    }
    class editmemo extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... unuesed){

            String data = "";
            String value = "path="+name+"&memo="+memo_text+"";
            Log.e("POST",value);
            try {
                URL url = new URL("http://54.226.200.206/update_memo.php");
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
            Log.e("RECV DATA",data);

            //System.out.print(data);
            // data = data.toString();
           if(data.equals("ERROR")){
                Toast.makeText(getApplicationContext(), "memo is empty", Toast.LENGTH_SHORT).show();

                //finish();
           }
           else{
                Toast.makeText(getApplicationContext(), "memo saved", Toast.LENGTH_SHORT).show();
                //finish();
               Intent intent = new Intent();
               intent.putExtra("memo", memo_text);
               setResult(0, intent);
               finish();

           }


        }

    }
}
