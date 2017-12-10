package com.example.user.artfolio_ver10;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sr092 on 2017-12-10.
 */

public class edit_introduction extends AppCompatActivity {
    EditText editText;

    String memo_text;
    int mode;
    String username, introduction;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_introduction);

        editText = (EditText) findViewById(R.id.company_introduction);

        Intent intent = getIntent();
        username = intent.getExtras().getString("id");
        memo_text = intent.getExtras().getString("memo");
        mode = intent.getExtras().getInt("mode");

        System.out.println(memo_text);
        try{
            Log.e("try>", memo_text);
        }catch(NullPointerException e){
            Log.e("catch >", e.toString());
        }
        editText.setText(memo_text);
        Button btn_sendServer = (Button)findViewById(R.id.sendText_server);
        btn_sendServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                introduction = editText.getText().toString();

                    editintro editintro = new editintro();
                    editintro.execute();

            }
        });
    }

    class editintro extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... unuesed) {
            String data = "";
            String value = "ID=" + username + "&memo=" + introduction + "";
            URL url;
            try {
                if(mode==1) {
                    url = new URL("http://54.226.200.206/edit_introduction.php");
                }else {
                     url = new URL("http://54.226.200.206/update_introduction.php");
                }
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
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        protected void onPostExecute(String data) {
                 /* 서버에서 응답 */
            Log.e("RECV DATA",data);

            if(data.equals("ERROR"))
            {
                Toast.makeText(getApplicationContext(),"Edit Fail", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Edit Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("memo", introduction);
                Log.e("new introduction:",introduction);
                setResult(0, intent);
                finish();
            }
        }
    }


}
