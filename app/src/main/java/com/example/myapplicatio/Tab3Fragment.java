package com.example.myapplicatio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.greenwhite.lib.view_setup.ViewSetup;

public class Tab3Fragment extends Fragment {
    private ViewSetup vs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vs = new ViewSetup(inflater.inflate(R.layout.fragment_one, container, false));
        return vs.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vs.textView(R.id.tvTab).setText("Tab 3");
    }
}