package com.android.campusmoments.Service;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static com.android.campusmoments.Service.Config.*;
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
    public static String token = null;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    public static User mySelf = null;
    public static void tokenCheck(String token, Handler handler) {
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
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = MainActivity.TOKEN_INVALID;
                    handler.sendMessage(message);
                }
            }
        });
    }
    public static void login(String username, String password, Handler handler) {
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
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {

                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = LoginActivity.LOGIN_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = LoginActivity.LOGIN_SUCCESS;
                assert response.body() != null;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    public static void register(String email, String username, String password, Handler handler) {
        String params = "{\"email\":\"" + email + "\",\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
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
                message.what = REGISTER_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() != 201) {
                    Message message = new Message();
                    message.what = REGISTER_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = REGISTER_SUCCESS;
                message.obj = response.body().string();
                handler.sendMessage(message);
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
    // 获取所有用户
    public static void getAllUsers(Handler handler) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(GET_ALL_USERS_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = GET_ALL_USERS_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = GET_ALL_USERS_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = GET_ALL_USERS_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    public static void setAvatar(String avatarPath, Handler handler) {
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
                message.what = SET_AVATAR_FAIL;
                handler.sendMessage(message);

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = SET_AVATAR_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = SET_AVATAR_SUCCESS;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }
    public static void setUsername(String username, Handler handler) {
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
                message.what = SET_USERNAME_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = SET_USERNAME_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = SET_USERNAME_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    public static void setPassword(String password, String new_password, Handler handler) {
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
                message.what = SET_PASSWORD_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = SET_PASSWORD_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = SET_PASSWORD_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    public static void setBio(String bio, Handler handler) {
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
                message.what = SET_BIO_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = SET_BIO_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = SET_BIO_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    public static void logout(Handler handler) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(LOGOUT_URL)
                .post(okhttp3.RequestBody.create(null, new byte[0]))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = LOGOUT_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 204) {
                    Message message = new Message();
                    message.what = LOGOUT_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = LOGOUT_SUCCESS;
                handler.sendMessage(message);
            }
        });
    }
    public static void follow(int id, boolean isFollowed, Handler handler) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", String.valueOf(id));
        builder.add("list", "follow");
        if(isFollowed) {
            builder.add("action", "remove");
        } else {
            builder.add("action", "add");
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(FOLLOW_BLOCK_URL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                if (isFollowed)
                    message.what = UNFOLLOW_FAIL;
                else
                    message.what = FOLLOW_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    if (isFollowed)
                        message.what = UNFOLLOW_FAIL;
                    else
                        message.what = FOLLOW_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                if (isFollowed)
                    message.what = UNFOLLOW_SUCCESS;
                else
                    message.what = FOLLOW_SUCCESS;
                message.obj = response.body().string();
                message.arg1 = id;
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    public static void block(int id, boolean isBlocked, Handler handler) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", String.valueOf(id));
        builder.add("list", "block");
        if(isBlocked) {
            builder.add("action", "remove");
        } else {
            builder.add("action", "add");
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(FOLLOW_BLOCK_URL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Message message = new Message();
                if (isBlocked)
                    message.what = UNBLOCK_FAIL;
                else
                    message.what = BLOCK_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    if (isBlocked)
                        message.what = UNBLOCK_FAIL;
                    else
                        message.what = BLOCK_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                if (isBlocked)
                    message.what = UNBLOCK_SUCCESS;
                else
                    message.what = BLOCK_SUCCESS;
                message.obj = response.body().string();
                message.arg1 = id;
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
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
    public static void pubMoment(Handler handler, String tag, String title, String content, String imagePath, String videoPath, String address) {
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
                message.what = PUB_MOMENT_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 201) {
                    Message message = new Message();
                    message.what = PUB_MOMENT_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = PUB_MOMENT_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    // 获取所有动态
    public static void getMomentsAll(Handler handler) {
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
                message.what = GET_MOMENTS_FAIL;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = GET_MOMENTS_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = GET_MOMENTS_SUCCESS;
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
    // 获取指定用户的动态
    public static void getMomentsByUser(int id, Handler handler) {
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
                message.what = GET_MOMENTS_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = GET_MOMENTS_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = GET_MOMENTS_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    // 按照id获取动态
    public static void getMomentById(int id, Handler handler) {
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
                message.what = GET_MOMENT_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = GET_MOMENT_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = GET_MOMENT_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    // 按照userId获取User
    public static void getUserById(int id, Handler handler) {
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
                message.what = GET_USER_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 200) {
                    Message message = new Message();
                    message.what = GET_USER_FAIL;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = GET_USER_SUCCESS;
                message.obj = response.body().string();
                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    // 评论
    public static void postComment(Handler handler, @NonNull String content, @NonNull int momentId) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("content", content);
        builder.addFormDataPart("by", String.valueOf(mySelf.id));
        builder.addFormDataPart("moment", String.valueOf(momentId));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(POST_COMMENT_URL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "postComment-onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 500) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = 1;
                message.obj = response.body().string();
//                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }

    public static void getCommentById(Handler handler, int id) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Token " + token)
                .url(GET_COMMENT_URL + id)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull java.io.IOException e) {
                Log.d(TAG, "getCommentById-onFailure: " + e.getMessage());
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws java.io.IOException {
                if (response.code() != 500) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = 1;
                message.obj = response.body().string();
//                Log.d(TAG, "onResponse: " + message.obj);
                handler.sendMessage(message);
            }
        });
    }
    /* 网络工具 */
    public static String checkObjStr(JSONObject obj, String name) {
        try {
            if (obj.isNull(name)) return null;
            return obj.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
