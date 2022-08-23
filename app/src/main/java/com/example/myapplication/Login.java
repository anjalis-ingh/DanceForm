package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    // variables
    EditText emailAddress, userPassword;
    Button signinBtn, signupBtn;
    String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView showPassBtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_in);

        // controls
        emailAddress = findViewById(R.id.email_address);
        userPassword = findViewById(R.id.user_password);
        signinBtn = findViewById(R.id.sign_in_btn);
        signupBtn = findViewById(R.id.sign_up_btn);
        showPassBtn = findViewById(R.id.show_pass_btn);

        mAuth = FirebaseAuth.getInstance();

        // Show/Hide Password using Eye Icon
        showPassBtn.setImageResource(R.drawable.hide_password);
        showPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // hide password
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    // change the icon
                    showPassBtn.setImageResource(R.drawable.hide_password);

                }
                else {
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassBtn.setImageResource(R.drawable.show_password);
                }
            }
        });

        // Sign In
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginUser();
            }
        });

        // Sign Up
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // Google Sign In



        }

    // Login Validation
    private void loginUser() {
        String email = emailAddress.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if(!email.matches(emailValidation)){
            emailAddress.setError("Please enter a valid email address");
            emailAddress.requestFocus();
            return;
        }
        else if(email.isEmpty()){
            emailAddress.setError("This field cannot be blank");
            emailAddress.requestFocus();
            return;
        }
        else if(password.isEmpty()){
            userPassword.setError("This field cannot be blank");
            userPassword.requestFocus();
            return;
        }
        else if(password.length() < 6){
            userPassword.setError("Password must be more than 6 characters long");
            userPassword.requestFocus();
            return;
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this, "User logged in!", Toast.LENGTH_LONG).show();
                        sendToHomePage();
                    }
                    else{
                        Toast.makeText(Login.this, "User log in failed. Please check your credentials", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    // Successful Login
    private void sendToHomePage() {
        Intent intent = new Intent(Login.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }




    }
