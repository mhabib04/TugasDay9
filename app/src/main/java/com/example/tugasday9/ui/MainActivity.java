package com.example.tugasday9.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tugasday9.R;
import com.example.tugasday9.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private ActivityMainBinding binding;

    String usernameMain, nameMain;

    ActionBar actionBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#03AED2"));
        actionBar.setBackgroundDrawable(colorDrawable);

        sessionManager = new SessionManager(MainActivity.this);
        if(!sessionManager.isLoggedIn()){
            moveToLogin();
        }

        usernameMain = sessionManager.getUserDetail().get(SessionManager.USERNAME);
        nameMain = sessionManager.getUserDetail().get(SessionManager.NAME);

        binding.etMainUsername.setText("Username : " + usernameMain);
        binding.etMainName.setText("Name : " + nameMain);

    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionLogout){
            sessionManager.logoutSession();
            moveToLogin();
        }
        return super.onOptionsItemSelected(item);
    }
}