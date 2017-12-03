package com.example.user.artfolio_ver10;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-11-25.
 */

public class Home_frag extends android.support.v4.app.Fragment {
    ArrayList<userlist_item> data;
    String userlist[];
    private RecyclerView mRecyclerView_popular;
    private RecyclerView.Adapter mAdapter_popular;
    private LinearLayoutManager linearLayoutManager;
    public RequestManager mGlideRequestManager;
    public View view;
    public static Home_frag newInstance() {
        Home_frag fragment = new Home_frag();
        return fragment;
    }

    public Home_frag() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_frag, null);
      //  getuserDB();
       // pic_names = getArguments().getStringArray("piclist");
        userlist = getArguments().getStringArray("userlist");
        data = new ArrayList<>();
        for(int i=0; i<userlist.length; i++){
            data.add(new userlist_item("https://s3.ap-northeast-2.amazonaws.com/artfolio-imageupload/JPEG_xyz_20171129_200639.jpg",userlist[i]));
        }
            mRecyclerView_popular = (RecyclerView) view.findViewById(R.id.popular_view);
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView_popular.setLayoutManager(linearLayoutManager);
            mGlideRequestManager = Glide.with(Home_frag.this);
            if(data!=null) {
                mAdapter_popular = new homeAdapter_popular(getContext(), data, mGlideRequestManager);
                mRecyclerView_popular.setAdapter(mAdapter_popular);
            }
        return this.view=view;
    }

}
