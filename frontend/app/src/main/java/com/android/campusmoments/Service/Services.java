package com.android.campusmoments.Service;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;

public class Services {

    private static final String TAG = "Services";
    private static final String BASE_URL = "http://127.0.0.1:8000/";
    private static final String LOGIN_URL = BASE_URL + "login/";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    private static Handler loginHandler = null;
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAIL = 0;
    public static String getLoginUrl() {
        return LOGIN_URL;
    }
    public static Handler getLoginHandler() {
        return loginHandler;
    }
    public static void setLoginHandler(Handler _loginHandler) {
        loginHandler = _loginHandler;
    }

    public static void login(String username, String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = getLoginUrl();
                String params = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
                Log.d(TAG, "run: " + params);
                Request request = new Request.Builder()
                        .url(url)
                        .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Message message = new Message();
                        message.what = LOGIN_FAIL;
                        loginHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                        Log.d(TAG, "onResponse: " + response.body().string());
                        Message message = new Message();
                        message.what = LOGIN_SUCCESS;
                        message.obj = response.body().string();
                        loginHandler.sendMessage(message);
                    }
                });
            }
        });
        thread.start();
    }
}
