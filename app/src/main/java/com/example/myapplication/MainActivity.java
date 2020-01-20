package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
       // setContentView(R.layout.activity_main_linear);
        // setContentView(R.layout.activity_main_grid);
        //  setContentView(R.layout.activity_main_relative);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();

            } });


        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged( CompoundButton buttonView, boolean b) {
                // update your model (or other business logic) based on isChecked

                Snackbar.make(cb, "Checkbox is " + b, Snackbar.LENGTH_LONG)
                        .setAction("Undo", clickView -> buttonView.setChecked( !b ))
                        .show();

            }
        });
        Switch box = (Switch)findViewById(R.id.switch1);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean a) {
                // update your model (or other business logic) based on isChecked

                Snackbar.make(box, "Switch " + a, Snackbar.LENGTH_LONG)
                        .setAction("Undo", clickView -> buttonView.setChecked( !a ))
                        .show();

            }
        });



    }
}