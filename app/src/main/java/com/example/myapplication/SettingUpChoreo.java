package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
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

    Button createBtn, presetBtn, customBtn;
    ImageView backBtn;
    EditText choreoName, numberDancers;
    Boolean isClickedDummy; // global after the declaration of your class
    int check = 1;
    int check2 = 1;


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
        customBtn = findViewById(R.id.custom_btn);

        // Create Button
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { createChoreographyValidation(); }
        });

        // Preset Button
        presetBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (check == 1) {
                    buttonClicked(presetBtn, 1);
                    check = 0;
                }
                else {
                    buttonClicked(presetBtn, 0);
                    check = 1;
                }
            }
        });

        // Custom Button
        customBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (check2 == 1) {
                    buttonClicked(customBtn, 1);
                    check2 = 0;
                }
                else {
                    buttonClicked(customBtn, 0);
                    check2 = 1;
                }
            }
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
    private void createChoreographyValidation() {
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
        else if (check == 0 && check2 == 0) {
            Toast.makeText(this, "You cannot select both Preset and Custom. Please choose only one.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Intent intent = new Intent(SettingUpChoreo.this, FormationCreator.class);
            startActivity(intent);
            finish();
        }
    }

    // Change Button color and text when clicked
    private void buttonClicked(Button button, int check) {

        if (check == 1) {
            button.setBackgroundTintList(AppCompatResources.getColorStateList(button.getContext(), R.color.mainpurple));
            button.setTextColor(Color.WHITE);
        }
        else {
            button.setBackgroundTintList(AppCompatResources.getColorStateList(button.getContext(), R.color.lightgraywhite));
            button.setTextColor(Color.BLACK);
        }
    }
}