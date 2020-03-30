package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("name", Context.MODE_PRIVATE);
        String savedString = prefs.getString("R", "");

        EditText typeField = findViewById(R.id.TypeEmail);
        typeField.setText(savedString);
        Button saveButton = findViewById(R.id.logbutton);

        saveButton.setOnClickListener( v -> {
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
            String emailTyped = typeField.getText().toString();
            goToProfile.putExtra("EMAIL", emailTyped);
            saveSharedPrefs( emailTyped );
            startActivity(goToProfile);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Main Activity", "In onStart()");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main Activity", "In onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("R", stringToSave);
        editor.apply();
    }
}