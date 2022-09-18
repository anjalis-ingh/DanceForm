package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;

public class SignUp extends AppCompatActivity {

    EditText userName, emailAddress, userPassword, confirmPass;
    Button createBtn;
    String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView showPassBtn, showPassBtn2, backBtn;
    ImageView googleBtn, facebookBtn, twitterBtn;

    CallbackManager callbackManager;
    OAuthProvider.Builder provider;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email_address);
        userPassword = findViewById(R.id.user_password);
        confirmPass = findViewById(R.id.confirm_password);

        createBtn = findViewById(R.id.create_btn);
        showPassBtn = findViewById(R.id.show_pass_btn);
        showPassBtn2 = findViewById(R.id.show_pass_btn2);
        backBtn = findViewById(R.id.back_btn);

        googleBtn = findViewById(R.id.google);
        facebookBtn = findViewById(R.id.facebook);
        twitterBtn = findViewById(R.id.twitter);

        mAuth = FirebaseAuth.getInstance();

        // Twitter Sign In
        provider = OAuthProvider.newBuilder("twitter.com");
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { twitterAuth(); }
        });

        // Facebook Sign In
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onError(@NonNull FacebookException e) { }

                    @Override
                    public void onCancel() { }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        sendToHomePage();
                    }
                });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignUp.this, Arrays.asList("public_profile"));
            }
        });

        // Google Sign Up
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            sendToHomePage();
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Show/Hide Password using Eye Icon
        showPassBtn.setImageResource(R.drawable.hide_password);
        showPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHidePass();
            }
        });

        // Eye Icon 2
        showPassBtn2.setImageResource(R.drawable.hide_password);
        showPassBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHidePass2();
            }
        });

        // Create Account
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAcctValidation();
            }
        });

        // Go back to Login Page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Show/Hide Eye Icon
    private void showHidePass() {
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

    private void showHidePass2() {
        if (confirmPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
            // hide password
            confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

            // change the icon
            showPassBtn2.setImageResource(R.drawable.hide_password);

        }
        else {
            confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showPassBtn2.setImageResource(R.drawable.show_password);
        }
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                sendToHomePage();
            }
            catch (ApiException exception) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Account Validation
    private void createAcctValidation() {
        String name = userName.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmPassword = confirmPass.getText().toString().trim();

        if (!email.matches(emailValidation)) {
            emailAddress.setError("Please enter a valid email address");
            emailAddress.requestFocus();
            return;
        }
        else if (email.isEmpty()) {
            emailAddress.setError("This field cannot be blank");
            emailAddress.requestFocus();
            return;
        }
        else if (password.isEmpty()) {
            userPassword.setError("This field cannot be blank");
            userPassword.requestFocus();
            return;
        }
        else if (password.length() < 6) {
            userPassword.setError("Password must be more than 6 characters long");
            userPassword.requestFocus();
            return;
        }
        else if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUp.this, "Password not matching", Toast.LENGTH_SHORT).show();
        }
        else if (name.isEmpty()) {
            userName.setError("This field cannot be blank");
            userName.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SignUp.this, "User has been signed up!", Toast.LENGTH_LONG).show();
                        sendToLogin();
                    }
                    else {
                        Toast.makeText(SignUp.this, "User sign up failed, try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    // Twitter Auth
    private void twitterAuth() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) { }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
        } else {
            firebaseAuth
                    .startActivityForSignInWithProvider(SignUp.this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) { }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
                                }
                            });
        }
    }

    // Successful Account Creation
    private void sendToLogin() {
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();
    }

    // Successful Login
    private void sendToHomePage() {
        Intent intent = new Intent(SignUp.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}