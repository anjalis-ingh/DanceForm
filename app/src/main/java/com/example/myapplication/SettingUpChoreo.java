package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SettingUpChoreo extends AppCompatActivity {

    Button createBtn, presetBtn;
    ImageView backBtn;
    EditText choreoName, numberDancers;
    Boolean isClickedDummy; // global after the declaration of your class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting_up_choreo);

        isClickedDummy = true; // in your onCreate()

        createBtn = findViewById(R.id.create_btn);
        backBtn = findViewById(R.id.back_btn2);
        choreoName = findViewById(R.id.choreo_name_input);
        numberDancers = findViewById(R.id.number_dancers);
        presetBtn = findViewById(R.id.preset_btn);


        // Create Button
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { createChoreography(); }
        });

        // Create Button
        presetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { buttonClicked(presetBtn, isClickedDummy); }
        });

        // Go back to Home Page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingUpChoreo.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Information Validation
    private void createChoreography() {
        String name = choreoName.getText().toString().trim();
        String number = numberDancers.getText().toString().trim();

        if (name.isEmpty()) {
            choreoName.setError("This field cannot be blank");
            choreoName.requestFocus();
            return;
        }
        else if (number.isEmpty()) {
            numberDancers.setError("This field cannot be blank");
            numberDancers.requestFocus();
            return;
        }
        else if (number.equals("0")) {
            numberDancers.setError("Number of dancers must be greater than 1");
            numberDancers.requestFocus();
            return;
        }
        else {
            Intent intent = new Intent(SettingUpChoreo.this, FormationCreator.class);
            startActivity(intent);
            finish();
        }
    }

    private void buttonClicked(Button button, final Boolean isClicked) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isClickedDummy) {
                    v.setBackgroundColor(Color.parseColor("#F6F6F6"));
                    isClickedDummy = false;
                } else {
                    v.setBackgroundColor(Color.parseColor("#6C63FF"));
                    isClickedDummy = true;
                }
            }
        });
    }
}