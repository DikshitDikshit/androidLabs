package com.example.androidlabs;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton takePictureBtn;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent loginPage = getIntent();

        String emailTyped = loginPage.getStringExtra("EMAIL");

        EditText enterText = findViewById(R.id.EditText2);
        enterText.setText(emailTyped);


        takePictureBtn = findViewById(R.id.imageButton);
        takePictureBtn.setOnClickListener(c -> {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        });
        Log.d(ACTIVITY_NAME, "In function: onCreate()");

        Button buttongotochat = findViewById(R.id.buttongotochat);

        buttongotochat.setOnClickListener(v -> {
            Intent chatIntent = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(chatIntent);
        });
        goToWeatherBtn = findViewById(R.id.buttongotoweather);
        goToWeatherBtn.setOnClickListener(c -> {
            seeWeather();
        });
    }
    Button goToWeatherBtn;
    public void seeWeather(){

        Intent goToMenuPage = new Intent(ProfileActivity.this, WeatherForecast.class);

        startActivityForResult(goToMenuPage, 234);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            takePictureBtn.setImageBitmap(imageBitmap);
        }
        Log.d(ACTIVITY_NAME, "In function: onActivityResult()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ACTIVITY_NAME, "In function: onStart()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTIVITY_NAME, "In function: onResume()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ACTIVITY_NAME, "In function: onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ACTIVITY_NAME, "In function: onStop()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ACTIVITY_NAME, "In function: onDestroy()");
    }
}