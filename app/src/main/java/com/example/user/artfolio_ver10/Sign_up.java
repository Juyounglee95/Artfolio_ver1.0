package com.example.user.artfolio_ver10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Sign_up extends AppCompatActivity {
    EditText edit_name, edit_id, edit_pw, edit_phone, edit_email;
    String user_name,user_id, user_pw, user_phone, user_email, profile_path;
  //  ImageView profile;
    String userprofile;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static final int MY_PERMISSION_CAMERA = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_signup);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_pw = (EditText)findViewById(R.id.edit_pw);
        edit_id = (EditText)findViewById(R.id.edit_id);
        edit_phone = (EditText)findViewById(R.id.edit_phone);
        edit_email = (EditText)findViewById(R.id.edit_email);
       // profile =(ImageView)findViewById(R.id.profile_picture);
        Intent intent = getIntent();

    }
    public void signup(View view){
        user_name = edit_name.getText().toString();
        user_id = edit_id.getText().toString();
        user_pw = edit_pw.getText().toString();
        user_phone = edit_phone.getText().toString();
        user_email =edit_email.getText().toString();
       // userprofile="_profile";
        registUser registUser = new registUser();
        registUser.execute();


    }
//    public void select_profile(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("업로드할 이미지 선택")
//                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        act_camera();
//                    }
//                })
//                .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        act_gallery();
//                    }
//                })
//                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .show();
//    }
//    public void act_gallery() {
//        Toast.makeText(this, "Pick Image from your gallery", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, getprofile.class);
//        intent.putExtra("index", PICK_FROM_GALLERY);
//     //   intent.putExtra("user_id", user_id);
//        startActivityForResult(intent, PICK_FROM_GALLERY);
//    }
//
//    public void act_camera() {
//        Toast.makeText(this, "Pick Image from your camera", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, getprofile.class);
//        intent.putExtra("index", PICK_FROM_CAMERA);
//       // intent.putExtra("user_id", user_id);
//        startActivityForResult(intent, PICK_FROM_CAMERA);
//    }
//    protected void onActivityResult(int requestCode, int resultcode, Intent data){
//        super.onActivityResult(requestCode, resultcode, data);
//        if(requestCode==PICK_FROM_CAMERA||requestCode ==PICK_FROM_GALLERY){
//            if(data!=null) {
//
//                Uri profileuri =data.getParcelableExtra("Uri");
//                profile_path= data.getStringExtra("imagename");
//                profile.setImageURI(profileuri);
//            }
//        }
//    }



    class registUser extends AsyncTask<String, Integer, String> {


            protected String doInBackground(String... unuesed){

                String data = "";
                String value = "Name="+user_name+"&ID="+user_id+"&PW="+user_pw+"&Phone="+user_phone+"&Email="+user_email+"";
                try {
                    URL url = new URL("http://54.226.200.206/join.php");
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
                    while ( ( line = in.readLine() ) != null )
                    {
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
                //Log.e("RECV DATA",data);

                if(data.equals("SUCCESS"))
                {
                 Toast.makeText(getApplicationContext(),"Join Success", Toast.LENGTH_SHORT).show();
                 finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Join Fail", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"입력사항을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }




        }
}
