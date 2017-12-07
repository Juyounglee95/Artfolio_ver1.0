package com.example.user.artfolio_ver10;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
    String loginID;
    String userlist[];
    String profilelist[];

    private RecyclerView mRecyclerView_popular;
    private homeAdapter_popular mAdapter_popular;
    private GridLayoutManager gridLayoutManager;
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
        loginID = getArguments().getString("loginID");
        userlist = getArguments().getStringArray("userlist");
        profilelist = getArguments().getStringArray("wholeprofile");
        data = new ArrayList<>();

        for(int i=0; i<userlist.length; i++){
            data.add(new userlist_item(profilelist[i],userlist[i]));
         //   System.out.println(profilelist[i] + "///"+ userlist[i]);
        }
            mRecyclerView_popular = (RecyclerView) view.findViewById(R.id.popular_view);
            gridLayoutManager = new GridLayoutManager(getContext(),4);

            mRecyclerView_popular.setLayoutManager(gridLayoutManager);
            mGlideRequestManager = Glide.with(Home_frag.this);
            if(data!=null) {
                mAdapter_popular = new homeAdapter_popular(getContext(), data, mGlideRequestManager);
                mAdapter_popular.setLoginID(loginID);
                mRecyclerView_popular.setAdapter(mAdapter_popular);
            }
        return this.view=view;
    }



}
