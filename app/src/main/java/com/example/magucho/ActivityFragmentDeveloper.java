package com.example.magucho;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ActivityFragmentDeveloper extends Fragment {

    public ActivityFragmentDeveloper(){}


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View vista= inflater.inflate(R.layout.activity_fragment_developer,container,false);
        return vista;
    }

}