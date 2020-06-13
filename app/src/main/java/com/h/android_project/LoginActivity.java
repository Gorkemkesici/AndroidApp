package com.h.android_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.h.android_project.models.LoginResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    EditText et_username, et_password;
    Button btn_login;
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        transparentActionBar();

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (!email.equals("") && !password.equals("")) {
                    Utils.showProgress(LoginActivity.this, "YÜKLENİYOR...");
                    APIClient.getInstanceNonAuthorize().login(email,password).enqueue(loginResponseCallback);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                Utils.putStringPrefs(LoginActivity.this, "email", email);
                Utils.putStringPrefs(LoginActivity.this, "password", password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Utils.showErrorMessage(LoginActivity.this, response);
            }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            Utils.dismissProgress();
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
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
