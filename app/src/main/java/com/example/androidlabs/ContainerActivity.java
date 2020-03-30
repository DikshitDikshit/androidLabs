package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Bundle dataToPass = getIntent().getExtras();

        // add a fragment
        DetailsFragment detailFragment = new DetailsFragment();
        detailFragment.setArguments(dataToPass);
        detailFragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, detailFragment)
                .addToBackStack(null)
                .commit();
    }


}
