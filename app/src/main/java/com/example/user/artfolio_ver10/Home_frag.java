package com.example.user.artfolio_ver10;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 2017-11-25.
 */

public class Home_frag extends android.support.v4.app.Fragment {
    public static Home_frag newInstance() {
        Home_frag fragment = new Home_frag();
        return fragment;
    }

    public Home_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_frag, container, false);
    }




}
