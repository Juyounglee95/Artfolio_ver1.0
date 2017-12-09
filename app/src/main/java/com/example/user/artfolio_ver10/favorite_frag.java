package com.example.user.artfolio_ver10;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link favorite_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favorite_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String id;
    String [] fa_list;
    String [] profile;
    ArrayList<myfavorite_list_item> data;
    RecyclerView myfavorite_view;
    LinearLayoutManager linearLayoutManager;
    RequestManager mGlideRequestManager;
    myfavorite_list_adapter myfavoriteListAdapter;
    // TODO: Rename and change types of parameters




    public favorite_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment favorite_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static favorite_frag newInstance() {
        favorite_frag fragment = new favorite_frag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favorite_frag, null);
        myfavorite_view = (RecyclerView)view.findViewById(R.id.favorite_view);
        fa_list= getArguments().getStringArray("falist");
        id = getArguments().getString("id");
        profile_path profile_path = new profile_path();
        profile = new String[fa_list.length-1];

            try {
                String string = profile_path.execute(id).get();
                profile = string.split("<br>"); //php 에서 path에 "https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/" 넣어줌
            }catch (Exception e){
                e.printStackTrace();
            }

        data = new ArrayList<>();
        for(int i=0; i<profile.length; i++){
            data.add(new myfavorite_list_item(profile[i], fa_list[i]));
        }
        linearLayoutManager= new LinearLayoutManager( getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        myfavorite_view.setLayoutManager(linearLayoutManager);
        mGlideRequestManager = Glide.with(this);
        if(data!=null) {
            myfavoriteListAdapter = new myfavorite_list_adapter(getContext(),data, mGlideRequestManager);
            myfavoriteListAdapter.setLoginID(id);
            myfavorite_view.setAdapter(myfavoriteListAdapter);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

}
