package com.android.campusmoments.Service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.campusmoments.Activity.AvatarConfigActivity;
import com.android.campusmoments.Activity.BioConfigActivity;
import com.android.campusmoments.Activity.LoginActivity;
import com.android.campusmoments.Activity.MainActivity;
import com.android.campusmoments.Activity.PasswordConfigActivity;
import com.android.campusmoments.Activity.PersonCenterActivity;
import com.android.campusmoments.Activity.PubActivity;
import com.android.campusmoments.Activity.RegisterActivity;
import com.android.campusmoments.Activity.UserHomePageActivity;
import com.android.campusmoments.Activity.UsernameConfigActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.RequestBody;

public class Services {

    private static final String TAG = "Services";
    private static final String USER_BASE_URL = "http://10.0.2.2:8000/users/api/";
    private static final String LOGIN_URL = USER_BASE_URL + "login";
    private static final String REGISTER_URL = USER_BASE_URL + "register";
    private static final String SELF_URL = USER_BASE_URL + "self";
    private static final String GET_USER_URL = USER_BASE_URL + "users/";
    private static final String PATCH_USER_URL = USER_BASE_URL + "users/";
    private static final String LOGOUT_URL = USER_BASE_URL + "logout";

    private static final String MOMENTS_BASE_URL = "http://10.0.2.2:8000/moments/api/";
    private static final String PUB_MOMENT_URL = MOMENTS_BASE_URL + "moments";
    private static final String GET_MOMENTS_URL = MOMENTS_BASE_URL + "moments";
    private static final String GET_MOMENT_URL = MOMENTS_BASE_URL + "moments/";
    public static String token = null;
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    private static Handler tokenHandler = null;
    private static Handler loginHandler = null;
    private static Handler registerHandler = null;
    private static Handler setAvatarHandler = null;
    private static Handler setUsernameHandler = null;
    private static Handler setBioHandler = null;
    private static Handler setPasswordHandler = null;
    private static Handler logoutHandler = null;
    private static Handler userHomePageHandler = null;
    private static Handler pubMomentHandler = null;

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
    public static void setTokenHandler(Handler _tokenHandler) {
        tokenHandler = _tokenHandler;
    }
    public static void setSetBioHandler(Handler _setBioHandler) {
        setBioHandler = _setBioHandler;
    }
    public static void setSetPasswordHandler(Handler _setPasswordHandler) {
        setPasswordHandler = _setPasswordHandler;
    }
    public static void setUserHomePageHandler(Handler _userHomePageHandler) {
        userHomePageHandler = _userHomePageHandler;
    }
    public static void setLogoutHandler(Handler _logoutHandler) {
        logoutHandler = _logoutHandler;
    }

    public static void setPubMomentHandler(Handler _pubMomentHandler) {
        pubMomentHandler = _pubMomentHandler;
    }

    public static void tokenCheck(String token) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(SELF_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    return;
                }
                assert response.body() != null;
                String json = response.body().string();
                Log.d(TAG, "onResponse: " + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    mySelf = new User(jsonObject);
                    Message message = new Message();
                    message.what = MainActivity.TOKEN_VALID;
                    tokenHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = MainActivity.TOKEN_INVALID;
                    tokenHandler.sendMessage(message);
                }
            }
        });
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
                message.what = LoginActivity.LOGIN_FAIL;
                loginHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {

                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = LoginActivity.LOGIN_FAIL;
                    loginHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = LoginActivity.LOGIN_SUCCESS;
                assert response.body() != null;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
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
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() != 201) {
                    Message message = new Message();
                    message.what = RegisterActivity.REGISTER_FAIL;
                    registerHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = RegisterActivity.REGISTER_SUCCESS;
                message.obj = response.body().string();
                registerHandler.sendMessage(message);
            }
        });
    }
    public static void getSelf(String token) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(SELF_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    return;
                }
                assert response.body() != null;
                String json = response.body().string();
                Log.d(TAG, "onResponse: " + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    mySelf = new User(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void getUser(int id) {
        Request request = new Request.Builder()
                .url(GET_USER_URL + id)
                .addHeader("Authorization", "Token " + token)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    return;
                }
                Message message = new Message();
                message.what = UserHomePageActivity.GET_USER_SUCCESS;
                message.obj = response.body().string();
                userHomePageHandler.sendMessage(message);
            }
        });
    }
    public static void setAvatar(String avatarPath) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        File file = new File(avatarPath);
        builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MEDIA_TYPE_FORM_DATA, file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(PATCH_USER_URL + mySelf.id)
                .addHeader("Authorization", "Token " + token)
                .patch(requestBody)
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
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = AvatarConfigActivity.SET_AVATAR_FAIL;
                    setAvatarHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = AvatarConfigActivity.SET_AVATAR_SUCCESS;
                message.obj = response.body().string();
                setAvatarHandler.sendMessage(message);
            }
        });
    }
    public static void setUsername(String username) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("username", username);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(PATCH_USER_URL + mySelf.id)
                .patch(requestBody)
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
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = UsernameConfigActivity.SET_USERNAME_FAIL;
                    setUsernameHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = UsernameConfigActivity.SET_USERNAME_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                setUsernameHandler.sendMessage(message);
            }
        });
    }
    public static void setPassword(String password, String new_password) {
        // 这个不对
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("password", password);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(PATCH_USER_URL + mySelf.id)
                .patch(requestBody)
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
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = PasswordConfigActivity.SET_PASSWORD_FAIL;
                    setPasswordHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = PasswordConfigActivity.SET_PASSWORD_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                setPasswordHandler.sendMessage(message);
            }
        });
    }
    public static void setBio(String bio) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("bio", bio);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(PATCH_USER_URL + mySelf.id)
                .patch(requestBody)
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
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = BioConfigActivity.SET_BIO_FAIL;
                    setBioHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = BioConfigActivity.SET_BIO_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                setBioHandler.sendMessage(message);
            }
        });
    }
    public static void logout() {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(LOGOUT_URL)
                .post(okhttp3.RequestBody.create(null, new byte[0]))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
//                Message message = new Message();
//                message.what = LogoutActivity.LOGOUT_FAIL;
//                logoutHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 204) {
                    Message message = new Message();
                    message.what = PersonCenterActivity.LOGOUT_FAIL;
                    logoutHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = PersonCenterActivity.LOGOUT_SUCCESS;
                logoutHandler.sendMessage(message);
            }
        });
    }
    public static void follow(int id, boolean isFollowed) {
        List<Integer> newFollowList = new ArrayList<>(mySelf.followList);
        if (isFollowed) {
            newFollowList.remove(Integer.valueOf(id));
        } else {
            newFollowList.add(id);
        }

        FormBody.Builder builder = new FormBody.Builder();
        // to array[integer] (formData)
        Log.d(TAG, "follow: " + new Gson().toJson(newFollowList));
        for (int i = 0; i < newFollowList.size(); i++) {
            builder.add("follow_list", String.valueOf(newFollowList.get(i)));
        }
        // if newFollowList is empty, add a empty array
//        if(newFollowList.size() == 0) {
//            builder.add("follow_list", "");
//        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(PATCH_USER_URL + mySelf.id)
                .patch(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                if (isFollowed)
                    message.what = UserHomePageActivity.UNFOLLOW_FAIL;
                else
                    message.what = UserHomePageActivity.FOLLOW_FAIL;
                userHomePageHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    if (isFollowed)
                        message.what = UserHomePageActivity.UNFOLLOW_FAIL;
                    else
                        message.what = UserHomePageActivity.FOLLOW_FAIL;
                    userHomePageHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                if (isFollowed)
                    message.what = UserHomePageActivity.UNFOLLOW_SUCCESS;
                else
                    message.what = UserHomePageActivity.FOLLOW_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                userHomePageHandler.sendMessage(message);
            }
        });
    }
    public static void block(int id, boolean isBlocked) {
        List<Integer> newBlockList = new ArrayList<>(mySelf.blockList);
        if (isBlocked) {
            newBlockList.remove(Integer.valueOf(id));
        } else {
            newBlockList.add(id);
        }

        FormBody.Builder builder = new FormBody.Builder();
        // to array[integer] (formData)
        Log.d(TAG, "block: " + new Gson().toJson(newBlockList));
        for (int i = 0; i < newBlockList.size(); i++) {
            builder.add("block_list", String.valueOf(newBlockList.get(i)));
        }
        // if newFollowList is empty, add a empty array
//        if(newBlockList.size() == 0) {
//            builder.add("block_list", "");
//        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(PATCH_USER_URL + mySelf.id)
                .patch(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                if (isBlocked)
                    message.what = UserHomePageActivity.UNBLOCK_FAIL;
                else
                    message.what = UserHomePageActivity.BLOCK_FAIL;
                userHomePageHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    if (isBlocked)
                        message.what = UserHomePageActivity.UNBLOCK_FAIL;
                    else
                        message.what = UserHomePageActivity.BLOCK_FAIL;
                    userHomePageHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                if (isBlocked)
                    message.what = UserHomePageActivity.UNBLOCK_SUCCESS;
                else
                    message.what = UserHomePageActivity.BLOCK_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                userHomePageHandler.sendMessage(message);
            }
        });
    }
    public static List<Integer> jsonArrayToList(JSONArray array) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(array.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    public static void pubMoment(String tag, String title, String content, String imagePath, String videoPath, String address) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("tag", tag);
        builder.addFormDataPart("title", title);
        builder.addFormDataPart("content", content);
        if (imagePath != null) {
            try {
                File file = new File(imagePath);
                builder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_FORM_DATA, file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (videoPath != null) {
            try {
                File file = new File(videoPath);
                builder.addFormDataPart("video", file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.addFormDataPart("address", address);
        builder.addFormDataPart("user", String.valueOf(mySelf.id));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(PUB_MOMENT_URL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = PubActivity.PUB_MOMENT_FAIL;
                pubMomentHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 201) {
                    Message message = new Message();
                    message.what = PubActivity.PUB_MOMENT_FAIL;
                    pubMomentHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = PubActivity.PUB_MOMENT_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                pubMomentHandler.sendMessage(message);
            }
        });
    }

    // 获取所有动态
    public static void getMoments(Handler handler) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(GET_MOMENTS_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = 1;
                message.obj = response.body().string();
                try {
                    JSONArray arr = new JSONArray(message.obj.toString());
                    Log.d(TAG, "onResponse: " + arr.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }

    public static void getMomentsByUser(int id) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(GET_MOMENTS_URL + "?user__id=" + id)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = UserHomePageActivity.GET_USER_MOMENTS_FAIL;
                userHomePageHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = UserHomePageActivity.GET_USER_MOMENTS_FAIL;
                    userHomePageHandler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = UserHomePageActivity.GET_USER_MOMENTS_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                userHomePageHandler.sendMessage(message);
            }
        });
    }
    // 按照id获取动态
    public static void getDetailedMoment(int id, Handler handler) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(GET_MOMENT_URL + id)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = 1;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    // 按照userId获取User
    public static void getUser(int id, Handler handler) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(GET_USER_URL + id)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = 1;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    // 从moment中获取user
    public static void setMomentUser(Moment moment, Handler handler) {
        getUser(moment.getUserId(), new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(msg.obj.toString());
                        moment.setUserInfo(obj);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = moment;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendMessage(msg);
                }
            }
        });
    }


    /* 网络工具 */
    public static String checkStr(JSONObject obj, String name) {
        try {
            if (obj.isNull(name)) return null;
            return obj.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public static String checkObjInt(JSONObject obj, String name) {
//        try {
//            if (obj.isNull(name)) return null;
//            return obj.getInt(name);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
