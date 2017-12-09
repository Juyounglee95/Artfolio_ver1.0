package com.example.user.artfolio_ver10;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Usersetting_frag extends Fragment {
    public View view;
    String user_id;
    String getUri;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    public static Usersetting_frag newInstance() {
        Usersetting_frag fragment = new Usersetting_frag();
        return fragment;
    }

    public Usersetting_frag() {
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_id = getArguments().getString("id");
        View view = inflater.inflate(R.layout.usersetting_frag, null);

        TextView userid = (TextView) view.findViewById(R.id.user_id);

        Button btnChange = (Button) view.findViewById(R.id.change);
        Button btnSave = (Button) view.findViewById(R.id.save);

        userid.setText(user_id);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("변경할 이미지 선택")
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "사진은 먼저 선택해주십시오",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void act_gallery() {
        Toast.makeText(getActivity(), "Pick Image from your gallery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), getprofile.class);
        intent.putExtra("index", PICK_FROM_GALLERY);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void act_camera() {
        Toast.makeText(getActivity(), "Pick Image from your camera", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), getprofile.class);
        intent.putExtra("index", PICK_FROM_CAMERA);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

}
