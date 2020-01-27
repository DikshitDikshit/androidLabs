package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity  {

    EditText email;
    SharedPreferences preference;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        email = findViewById(R.id.Lab3editText2);
        preference = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = preference.getString("ReserveName", "Default value");

        email.setHint(savedString);

        login = findViewById(R.id.Lab3LoginBtn);
        login.setOnClickListener( c->{
            Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);
            EditText et = findViewById(R.id.Lab3editText2);
            profilePage.putExtra("emailTyped", et.getText().toString());

            startActivityForResult(profilePage, 345);
        });
    }



    @Override
    protected void onPause(){
        super.onPause();

        //get an editor object
        SharedPreferences.Editor editor = preference.edit();

        //save what was typed under the name "ReserveName"
        String whatWasTyped = email.getText().toString();
        editor.putString("ReserveName", whatWasTyped);

        //write it to disk:
        editor.commit();
    }
}
