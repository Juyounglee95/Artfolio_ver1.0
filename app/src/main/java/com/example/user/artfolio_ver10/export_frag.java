package com.example.user.artfolio_ver10;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link export_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class export_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String initial;
    String [] piclist;
    String [] imagelist;
    String [] vidlist;
    String [] vidpathlist;
    String id;
    String email;
    private RecyclerView recyclerView;
    public RequestManager mGlideRequestManager;
    exportlist_adapter exportlist_adapter;
    public export_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment export_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static export_frag newInstance() {
        export_frag fragment = new export_frag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initial=getArguments().getString("initial");
        id = getArguments().getString("id");
        email = getArguments().getString("email");
            piclist = getArguments().getStringArray("piclist");

            vidlist = getArguments().getStringArray("vidlist");
        View view = inflater.inflate(R.layout.export_frag, null);
        // Inflate the layout for this fragment
        Button bt_zip = (Button)view.findViewById(R.id.bt_zip);

        recyclerView = (RecyclerView)view.findViewById(R.id.export_piclist);
        ArrayList<exportlist_item> data = new ArrayList<>();
        if(piclist!=null) {
            imagelist = new String[piclist.length];

            for (int i = 0; i < piclist.length; i++) {
                imagelist[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + piclist[i];
                data.add(new exportlist_item(imagelist[i], piclist[i], false));

            }
            if(vidlist!=null){
                vidpathlist = new String[vidlist.length];
                for (int i= 0; i < vidlist.length; i++) {
                    vidpathlist[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + vidlist[i];
                    data.add(new exportlist_item(vidpathlist[i], vidlist[i], false));

                }
            }
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(layoutManager);
//            mGlideRequestManager = Glide.with(this);
//            exportlist_adapter = new exportlist_adapter(getContext(), data, R.layout.export_item, mGlideRequestManager);
//            recyclerView.setAdapter(exportlist_adapter);
        }else{

            if(vidlist!=null) {
                vidpathlist = new String[vidlist.length];

                for (int i = 0; i < vidlist.length; i++) {
                    vidpathlist[i] = "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" + vidlist[i];
                    data.add(new exportlist_item(vidpathlist[i], vidlist[i], false));

                }
        }



        }
        if(data!=null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            mGlideRequestManager = Glide.with(this);
            exportlist_adapter = new exportlist_adapter(getContext(), data, R.layout.export_item, mGlideRequestManager);
            recyclerView.setAdapter(exportlist_adapter);
        }
        bt_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string="";
                int select_num=0;
                ArrayList<exportlist_item> export_list =exportlist_adapter.getExport_list();

                for (int i = 0; i < export_list.size(); i++) {
                    exportlist_item item = export_list.get(i);
                    if (item.isSelected() == true) {

                        string = string +item.getImage_name()+",";


                    }

                }
                String[] data = string.split(",");

//                Toast.makeText(getContext(),
//                        "Selected Students: \n" + string, Toast.LENGTH_LONG)
//                        .show();
                Intent intent = new Intent(getContext(), export_zip.class);
                intent.putExtra("email", email);
                intent.putExtra("path", data);
                startActivity(intent);
            }

//            }
        });
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event

}
