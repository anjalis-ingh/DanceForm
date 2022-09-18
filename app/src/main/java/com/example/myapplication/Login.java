package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
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

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    EditText emailAddress, userPassword;
    Button signinBtn, signupBtn;
    String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView showPassBtn, googleBtn, fbBtn, twitterBtn;

    CallbackManager callbackManager;
    OAuthProvider.Builder provider;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_in);

        emailAddress = findViewById(R.id.email_address);
        userPassword = findViewById(R.id.user_password);
        signinBtn = findViewById(R.id.sign_in_btn);
        signupBtn = findViewById(R.id.sign_up_btn);
        showPassBtn = findViewById(R.id.show_pass_btn);

        googleBtn = findViewById(R.id.google);
        fbBtn = findViewById(R.id.facebook);
        twitterBtn = findViewById(R.id.twitter);

        mAuth = FirebaseAuth.getInstance();

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

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile"));
            }
        });
        
        // Twitter Sign In
        provider = OAuthProvider.newBuilder("twitter.com");
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { twitterAuth(); }
        });

        // Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
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

        // Regular Sign In
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
                    .startActivityForSignInWithProvider(Login.this, provider.build())
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

    // Enable Hide/Show Eye Icon
    private void showHidePass() {
        if (userPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
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

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

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

    // Login Validation
    private void loginUser() {
        String email = emailAddress.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

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
        else {
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
        startActivity(intent);
        // finish();
    }

}
