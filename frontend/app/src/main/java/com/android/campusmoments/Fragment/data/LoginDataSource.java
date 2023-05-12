package com.android.campusmoments.Fragment.data;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.android.campusmoments.Service.Services;
import com.android.campusmoments.Fragment.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAIL = 0;
    Result<LoggedInUser> result = null;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN_SUCCESS) {
                successSignIn((LoggedInUser) msg.obj);
            } else if (msg.what == LOGIN_FAIL) {
//                failSignIn();
                // temp
                successSignIn((LoggedInUser) msg.obj);
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

    public void successSignIn(LoggedInUser data) {
        LoggedInUser User =
                new LoggedInUser(data);
        result = new Result.Success<>(User);
    }

    public void failSignIn() {
        result = new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication

    }
}