package com.android.campusmoments.Service;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.campusmoments.Activity.AvatarConfigActivity;
import com.android.campusmoments.Activity.BioConfigActivity;
import com.android.campusmoments.Activity.PasswordConfigActivity;
import com.android.campusmoments.Activity.RegisterActivity;
import com.android.campusmoments.Activity.UsernameConfigActivity;
import com.android.campusmoments.Fragment.data.LoginDataSource;
import com.android.campusmoments.Fragment.data.model.LoggedInUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Response;

public class Services {

    private static final String TAG = "Services";
    private static final String BASE_URL = "http://127.0.0.1:8000/";
    private static final String LOGIN_URL = BASE_URL + "login/";
    private static final String REGISTER_URL = BASE_URL + "register/";
    private static final String SET_AVATAR_URL = BASE_URL + "set_avatar/";
    private static final String SET_USERNAME_URL = BASE_URL + "set_username/";
    private static final String SET_BIO_URL = BASE_URL + "set_bio/";
    private static final String SET_PASSWORD_URL = BASE_URL + "set_password/";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    private static Handler loginHandler = null;
    private static Handler registerHandler = null;
    private static Handler setAvatarHandler = null;
    private static Handler setUsernameHandler = null;
    private static Handler setBioHandler = null;
    private static Handler setPasswordHandler = null;
    public static User mySelf = null;
    public static void setLoginHandler(Handler _loginHandler) {
        loginHandler = _loginHandler;
    }
    public static void setRegisterHandler(Handler _registerHandler) {
        registerHandler = _registerHandler;
    }
    public static void setSetAvatarHandler(Handler _setAvatarHandler) {
        setAvatarHandler = _setAvatarHandler;
    }
    public static void setSetUsernameHandler(Handler _setUsernameHandler) {
        setUsernameHandler = _setUsernameHandler;
    }
    public static void setSetBioHandler(Handler _setBioHandler) {
        setBioHandler = _setBioHandler;
    }
    public static void setSetPasswordHandler(Handler _setPasswordHandler) {
        setPasswordHandler = _setPasswordHandler;
    }
    public static void setLoggedInUser(LoggedInUser user) {
        mySelf = new User(user);
    }

    public static void login(String username, String password) {
        String params = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        Log.d(TAG, "run: " + params);
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = LoginDataSource.LOGIN_FAIL;
                // temp
                message.obj = new LoggedInUser(1, "username", "bio", "token");
                loginHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message = new Message();
                message.what = LoginDataSource.LOGIN_SUCCESS;
                // TODO:convert response.body() to LoggedInUser
                message.obj = new LoggedInUser(1, "username", "bio", "token");
                loginHandler.sendMessage(message);
            }
        });
    }

    public static void register(String username, String password) {
        String params = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        Log.d(TAG, "run: " + params);
        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = RegisterActivity.REGISTER_FAIL;
                registerHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message = new Message();
                message.what = RegisterActivity.REGISTER_SUCCESS;
                message.obj = response.body().string();
                registerHandler.sendMessage(message);
            }
        });
    }

    public static void setAvatar(String base64Avatar) {
        String params = "{\"userId\":\"" + mySelf.id + "\",\"avatar\":\"" + base64Avatar + "\"}";
        Log.d(TAG, "run: " + params);
        Request request = new Request.Builder()
                .url(SET_AVATAR_URL)
                .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = AvatarConfigActivity.SET_AVATAR_FAIL;
                setAvatarHandler.sendMessage(message);

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message = new Message();
                message.what = AvatarConfigActivity.SET_AVATAR_SUCCESS;
                message.obj = response.body().string();
                setAvatarHandler.sendMessage(message);
            }
        });
    }

    public static void setUsername(String username) {
        String params = "{\"userId\":\"" + mySelf.id + "\",\"username\":\"" + username + "\"}";
        Log.d(TAG, "run: " + params);
        Request request = new Request.Builder()
                .url(SET_USERNAME_URL)
                .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = UsernameConfigActivity.SET_USERNAME_FAIL;
                setUsernameHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message = new Message();
                message.what = UsernameConfigActivity.SET_USERNAME_SUCCESS;
                message.obj = response.body().string();
            }
        });
    }


    public static void setPassword(String old_password, String new_password) {
        String params = "{\"userId\":\"" + mySelf.id + "\",\"oldPassword\":\"" + old_password + "\",\"newPassword\":\"" + new_password + "\"}";
        Log.d(TAG, "run: " + params);
        Request request = new Request.Builder()
                .url(SET_PASSWORD_URL)
                .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = PasswordConfigActivity.SET_PASSWORD_FAIL;
                setPasswordHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message = new Message();
                message.what = PasswordConfigActivity.SET_PASSWORD_SUCCESS;
                message.obj = response.body().string();
                setPasswordHandler.sendMessage(message);
            }
        });
    }

    public static void setBio(String bio) {
        String params = "{\"userId\":\"" + mySelf.id + "\",\"bio\":\"" + bio + "\"}";
        Log.d(TAG, "run: " + params);
        Request request = new Request.Builder()
                .url(SET_BIO_URL)
                .post(okhttp3.RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = BioConfigActivity.SET_BIO_FAIL;
                setBioHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message = new Message();
                message.what = BioConfigActivity.SET_BIO_SUCCESS;
                message.obj = response.body().string();
                setBioHandler.sendMessage(message);
            }
        });
    }
}
