package com.android.campusmoments.data;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.android.campusmoments.Service.Services;
import com.android.campusmoments.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAIL = 0;
    Result<LoggedInUser> result = null;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN_SUCCESS) {
                successSignIn((String) msg.obj);
            } else if (msg.what == LOGIN_FAIL) {
                failSignIn();
            }

        }
    };
    public Handler getHandler() {
        return handler;
    }
    public Result<LoggedInUser> login(String username, String password) {
        Services.login(username, password);
        return result;
    }

    public void successSignIn(String username) {
        LoggedInUser User =
                new LoggedInUser(
                        java.util.UUID.randomUUID().toString(), username);
        result = new Result.Success<>(User);
    }

    public void failSignIn() {
        result = new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication

    }
}