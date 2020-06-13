package com.h.android_project.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    public String token;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;
}