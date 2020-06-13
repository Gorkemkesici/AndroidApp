package com.h.android_project;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static String ApiBaseUrl = "https://reqres.in/api/";
    private static APIService instanceNonAuth;
    private static APIService instanceAuth;

    public static APIService getInstanceNonAuthorize() {
        if (instanceNonAuth == null)
            instanceNonAuth = getClientNonAuthorize().create(APIService.class);
        return instanceNonAuth;
    }

    public static APIService getInstanceAuthorize(final Activity context) {
        if (instanceAuth == null)
            instanceAuth = getClientAuthorize(context).create(APIService.class);
        return instanceAuth;
    }

    private static Retrofit getClientNonAuthorize() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20L, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(ApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static Retrofit getClientAuthorize(final Activity context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20L, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Utils.AuthInfo.token).build();
                return chain.proceed(request);
            }
        });

        return new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(ApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}