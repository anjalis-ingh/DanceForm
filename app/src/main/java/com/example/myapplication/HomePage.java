package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    ImageView createBtn, profileBtn;

    private TextView profileMenu, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        createBtn = findViewById(R.id.plus_btn);
        tabLayout = findViewById(R.id.home_tab);
        viewPager2 = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        profileBtn = findViewById(R.id.profile_icon);
        profileMenu = findViewById(R.id.profile_pop_out);
        username = findViewById(R.id.user_name);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Display User Name in Profile Pop Up
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("name", "");
        username.setText(name);

        // Profile Button
        profileBtn.setOnClickListener(new View.OnClickListener() {
            int check = 1;

            @Override
            public void onClick(View v) {
                if (check == 1) {
                    profileMenu.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    check = 0;
                }
                else {
                    profileMenu.setVisibility(View.INVISIBLE);
                    username.setVisibility(View.INVISIBLE);
                    check = 1;
                }

            }
        });

        // User logs out
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signOut(); }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        // Create Button
        createBtn.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent intent = new Intent(HomePage.this, SettingUpChoreo.class);
                startActivity(intent);
                finish();
            }
       });
    }

    // User logs out
    private void signOut() {
        // Facebook
        LoginManager.getInstance().logOut();

        // Twitter
        FirebaseAuth.getInstance().signOut();

        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();

        GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(this,gso);
        googleSignInClient.signOut();

        Intent intent = new Intent(HomePage.this, Login.class);
        startActivity(intent);
        finish();
    }

}