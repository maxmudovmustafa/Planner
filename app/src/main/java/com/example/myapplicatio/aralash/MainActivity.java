package com.example.myapplicatio.aralash;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplicatio.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FragmentTransaction f = getFragmentManager().beginTransaction();
//        f.add(R.id.container, MyInfos(), "List");
//        f.commit();
    }
}
