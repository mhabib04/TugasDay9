package com.example.tugasday9.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tugasday9.api.ApiClient;
import com.example.tugasday9.api.ApiInterface;
import com.example.tugasday9.databinding.ActivityRegisterBinding;
import com.example.tugasday9.model.register.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    String usernameRegister, passwordRegister, nameRegister;
    ApiInterface apiInterface;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#03AED2"));
        actionBar.setBackgroundDrawable(colorDrawable);

        binding.btnRegister.setOnClickListener(v -> {
            usernameRegister = binding.etUsernameRegister.getText().toString().trim();
            passwordRegister = binding.etPasswordRegister.getText().toString().trim();
            nameRegister = binding.etNameRegister.getText().toString().trim();
            if(usernameRegister.isEmpty() || nameRegister.isEmpty() || passwordRegister.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Please fill in all fields completely", Toast.LENGTH_SHORT).show();
            } else {
                register(usernameRegister, passwordRegister, nameRegister);
            }
        });

        binding.tvAlreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void register(String username, String password, String name) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Register> registerCall = apiInterface.registerResponse(username,password, name);
        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(@NonNull Call<Register> call, @NonNull Response<Register> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Register> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}