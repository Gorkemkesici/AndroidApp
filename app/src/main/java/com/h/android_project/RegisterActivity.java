package com.h.android_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.h.android_project.models.LoginResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {
    EditText et_username, et_password;
    Button btn_register, btn_login;
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        transparentActionBar();

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (!email.equals("") && !password.equals("")) {
                    Utils.showProgress(RegisterActivity.this, "YÜKLENİYOR...");
                    APIClient.getInstanceNonAuthorize().login(email,password).enqueue(loginResponseCallback);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error !");
                    builder.setMessage("Kullanıcı adı veya şifre alanı boş geçilemez !");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                }
            }
        });
    }

    Callback<LoginResponse> loginResponseCallback = new Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            Utils.dismissProgress();
            if (response.isSuccessful()) {
                Utils.AuthInfo = response.body();
                Utils.putStringPrefs(RegisterActivity.this, "email", email);
                Utils.putStringPrefs(RegisterActivity.this, "password", password);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Utils.showErrorMessage(RegisterActivity.this, response);
            }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            Utils.dismissProgress();
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText(t.getLocalizedMessage())
                    .show();
        }
    };

    public void transparentActionBar() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
