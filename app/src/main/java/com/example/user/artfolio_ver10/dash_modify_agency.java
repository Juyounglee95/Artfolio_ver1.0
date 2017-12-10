package com.example.user.artfolio_ver10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by sr092 on 2017-12-10.
 */

public class dash_modify_agency extends AppCompatActivity {
    String path, memo_text;
    ImageView image;
    TextView intro;
    String user_id;
    Button intro_new;
    Button intro_modify;
    String image_name;
    Button btnBack;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agency_modify);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        memo_text = intent.getExtras().getString("memo");
        image_name = intent.getExtras().getString("image");
        intro_new = (Button) findViewById(R.id.introduction_new);
        intro_modify = (Button) findViewById(R.id.introduction_modify);

        image = (ImageView) findViewById(R.id.imageView7);
        if(image_name.equals("null")){

        }
        else{
            Glide.with(this).load("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/"+image_name).into(image); //서버 패스로 수정
        }
        intro = (TextView) findViewById(R.id.text_introduction);

        System.out.println(memo_text);
        try{
            intro.setText(memo_text);
        }catch (NullPointerException e){
            e.printStackTrace();;
        }
        Log.e(
                "TextView 뭐냐","-"+intro.getText().toString()+"-");
        if(memo_text.equals("\n\n\n\n\nCOMPANY INTRODUCTION")){
            intro.setText("\n\n\n\n\nCOMPANY INTRODUCTION");
            intro_new.setVisibility(View.VISIBLE);
        }else{
            intro_modify.setVisibility(View.VISIBLE);
        }

        Button btnPicChange = (Button) findViewById(R.id.pic_change);
        btnPicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), getImage.class);
//                startActivity(intent);
                //((getImage)getActivity()).checkPermission();
                AlertDialog.Builder builder = new AlertDialog.Builder(dash_modify_agency.this);
                builder.setTitle("업로드할 이미지 선택")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                act_camera();
                            }
                        })
                        .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                act_gallery();
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public void act_gallery() {
        Toast.makeText(this, "Pick Image from your gallery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, getintro_image.class);
        intent.putExtra("index", PICK_FROM_GALLERY);
        intent.putExtra("user_id", user_id);
        startActivityForResult(intent,100);
    }

    public void act_camera() {
        Toast.makeText(this, "Pick Image from your camera", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, getintro_image.class);
        intent.putExtra("index", PICK_FROM_CAMERA);
        intent.putExtra("user_id", user_id);
        startActivityForResult(intent,100);
    }
    public void new_introduction(View view){
            Intent intent = new Intent(this, edit_introduction.class);
            intent.putExtra("path", path);
            intent.putExtra("id", user_id);
            intent.putExtra("mode", 1);
            intent.putExtra("memo", memo_text);
            startActivityForResult(intent, 0);
    }

    public void modify_introduction(View view){
        Intent intent = new Intent(this, edit_introduction.class);
        intent.putExtra("path", path);
        intent.putExtra("id", user_id);
        intent.putExtra("mode", 2);
        intent.putExtra("memo", memo_text);
        startActivityForResult(intent, 0);
    }

    public void modify_back(View view){
        Bundle modify_bundle = new Bundle();
        modify_bundle.putString("id",user_id);
        Log.e("text>", intro.getText().toString());
        modify_bundle.putString("memo", intro.getText().toString());
        finish();
    }


    protected void onActivityResult(int requestCode, int resultcode, Intent data){
        super.onActivityResult(requestCode, resultcode, data);
        if(requestCode==0){
            if(data!=null) {
                memo_text = data.getStringExtra("memo");
                Log.e("뭐가 오냐>",memo_text);
                intro.setText(memo_text);
                if(!intro.getText().toString().equals("\n\n\n\n\nCompany Introduction")){
                    intro_modify.setVisibility(View.VISIBLE);
                    intro_new.setVisibility(View.INVISIBLE);
                }
            }

        }
        if (requestCode == 100) {
            if (data!=null) {
                image_name = data.getStringExtra("image");
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.kakao_default_profile_image);
                Glide.with(this).setDefaultRequestOptions(requestOptions).load(image_name).into(image);
                System.out.println("#####");

            }
        }

    }
}
