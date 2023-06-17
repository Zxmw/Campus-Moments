package com.android.campusmoments.Service;

import com.google.firebase.database.FirebaseDatabase;

import okhttp3.MediaType;

public class Config {
    public static final int GET_USER_FAIL = 0;
    public static final int GET_USER_SUCCESS = 1;
    public static final int FOLLOW_FAIL = 2;
    public static final int FOLLOW_SUCCESS = 3;
    public static final int UNFOLLOW_FAIL = 4;
    public static final int UNFOLLOW_SUCCESS = 5;
    public static final int BLOCK_FAIL = 6;
    public static final int BLOCK_SUCCESS = 7;
    public static final int UNBLOCK_FAIL = 8;
    public static final int UNBLOCK_SUCCESS = 9;
    public static final int REGISTER_SUCCESS = 10;
    public static final int REGISTER_FAIL = 11;
    public static final int SET_AVATAR_SUCCESS = 12;
    public static final int SET_AVATAR_FAIL = 13;
    public static final int SET_USERNAME_SUCCESS = 14;
    public static final int SET_USERNAME_FAIL = 15;
    public static final int SET_PASSWORD_SUCCESS = 16;
    public static final int SET_PASSWORD_FAIL = 17;
    public static final int SET_BIO_SUCCESS = 18;
    public static final int SET_BIO_FAIL = 19;
    public static final int LOGOUT_SUCCESS = 20;
    public static final int LOGOUT_FAIL = 21;
    public static final int PUB_MOMENT_FAIL = 22;
    public static final int PUB_MOMENT_SUCCESS = 23;
    public static final int GET_MOMENTS_FAIL = 24;
    public static final int GET_MOMENTS_SUCCESS = 25;
    public static final int GET_MOMENT_FAIL = 26;
    public static final int GET_MOMENT_SUCCESS = 27;
    public static final int SET_MOMENT_USER_FAIL = 28;
    public static final int SET_MOMENT_USER_SUCCESS = 29;
    public static final int GET_ALL_USERS_FAIL = 30;
    public static final int GET_ALL_USERS_SUCCESS = 31;
    public static final int REQUEST_PASSWORD_FAIL = 32;
    public static final int REQUEST_PASSWORD_SUCCESS = 33;
    public static final String USER_BASE_URL = "http://43.138.33.189:8888/users/api/";
    public static final String LOGIN_URL = USER_BASE_URL + "login";
    public static final String REGISTER_URL = USER_BASE_URL + "register";
    public static final String SELF_URL = USER_BASE_URL + "self";
    public static final String GET_ALL_USERS_URL = USER_BASE_URL + "users";
    public static final String GET_USER_URL = USER_BASE_URL + "users/";
    public static final String PATCH_USER_URL = USER_BASE_URL + "users/";
    public static final String LOGOUT_URL = USER_BASE_URL + "logout";
    public static final String FOLLOW_BLOCK_URL = USER_BASE_URL + "follow-block";

    public static final String REQUEST_RESET_PASSWORD_URL = "http://43.138.33.189:8888/users/" + "request-reset-email/";
    public static final String RESET_PASSWORD_URL = "http://43.138.33.189:8888/users/" + "password-reset-complete";
    public static final String MOMENTS_BASE_URL = "http://43.138.33.189:8888/moments/api/";
    public static final String PUB_MOMENT_URL = MOMENTS_BASE_URL + "moments";
    public static final String GET_MOMENTS_URL = MOMENTS_BASE_URL + "moments";
    public static final String GET_MOMENT_URL = MOMENTS_BASE_URL + "moments/";
    public static final String LIKE_STAR_URL = MOMENTS_BASE_URL + "like-star";
    public static final String POST_COMMENT_URL = MOMENTS_BASE_URL + "comments";
    public static final String GET_COMMENT_URL = MOMENTS_BASE_URL + "comments/";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8");
    public static final String FIREBASE_DATABASE_URL = "https://campus-moments-default-rtdb.asia-southeast1.firebasedatabase.app/";
    public static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
}
