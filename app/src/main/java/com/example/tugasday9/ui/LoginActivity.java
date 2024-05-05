package com.example.tugasday9.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tugasday9.api.ApiClient;
import com.example.tugasday9.api.ApiInterface;
import com.example.tugasday9.databinding.ActivityLoginBinding;
import com.example.tugasday9.model.login.Login;
import com.example.tugasday9.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    String usernameLogin, passwordLogin;
    ApiInterface apiInterface;
    SessionManager sessionManager;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#03AED2"));
        actionBar.setBackgroundDrawable(colorDrawable);

        binding.btnLogin.setOnClickListener(v -> {
            usernameLogin = binding.etUsernameLogin.getText().toString().trim();
            passwordLogin = binding.etPasswordLogin.getText().toString().trim();
            if(usernameLogin.isEmpty() || passwordLogin.isEmpty()){
                Toast.makeText(LoginActivity.this, "Please fill in all fields completely", Toast.LENGTH_SHORT).show();
            } else {
                login(usernameLogin, passwordLogin);
            }
        });

        binding.tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.toggleButtonShowPassword.setOnClickListener(v -> {
            int selectionStart = binding.etPasswordLogin.getSelectionStart();
            int selectionEnd = binding.etPasswordLogin.getSelectionEnd();
            if (binding.toggleButtonShowPassword.isChecked()) {
                binding.etPasswordLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.etPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.etPasswordLogin.setSelection(selectionStart, selectionEnd);
        });

    }

    private void login(String username, String password) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.loginResponse(username,password);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    sessionManager = new SessionManager(LoginActivity.this);
                    LoginData loginData = response.body().getLoginData();
                    sessionManager.createLoginSession(loginData);

                    Toast.makeText(LoginActivity.this, response.body().getLoginData().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}