package com.h.android_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.h.android_project.models.LoginResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static LoginResponse AuthInfo = null;
    //public static UserResponse User = null;

    public static void putStringPrefs(Context Activty, String Key, String value){
        SharedPreferences.Editor sharedPreferences = Activty.getSharedPreferences("gk", MODE_PRIVATE).edit();
        sharedPreferences.putString(Key,value);
        sharedPreferences.commit();
    }

    public static String getStringPrefs(Context Activity, String Key, String DefaultValue){
        SharedPreferences sharedPreferences = Activity.getSharedPreferences("gk", MODE_PRIVATE);
        return sharedPreferences.getString(Key, DefaultValue);
    }


    private static ProgressDialog progressDialog;

    public static void showProgress(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgress() {
        if (progressDialog == null) {
            return;
        }

        progressDialog.dismiss();
    }

    public static void showErrorMessage(Context context, Response<?> response) {
        if (response.errorBody() != null) {
            try {
                JsonObject errorBody = new Gson().fromJson(response.errorBody().string(), JsonObject.class);
                String error_dsc = (errorBody.get("error_description") != null) ? errorBody.get("error_description").getAsString() : "";
                String error = (errorBody.get("error") != null) ? errorBody.get("error").getAsString() : "";

                if (!error_dsc.equals("")) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setContentText(error_dsc)
                            .show();
                } else if (!error.equals("")) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setContentText(error)
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setContentText(response.errorBody().string())
                            .show();
                }
            } catch (Exception e) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setContentText(e.getLocalizedMessage())
                        .show();
            }
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setContentText(response.raw().code() + " " + response.raw().message())
                    .show();
        }
    }
}
