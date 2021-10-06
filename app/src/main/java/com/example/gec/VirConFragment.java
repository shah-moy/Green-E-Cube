package com.example.gec;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class VirConFragment extends Fragment {

    Button tempe, blow, water, cam;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_vir_con, container, false);


        tempe = root.findViewById(R.id.tempbtn);
        blow = root.findViewById(R.id.blowbtn);
        water = root.findViewById(R.id.waterbtn);
        cam = root.findViewById(R.id.camerabtn);
        return root;
    }
}