package com.android.chatsapp.api;

import com.android.chatsapp.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J(\u0010\u0007\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00060\bj\b\u0012\u0004\u0012\u00020\u0006`\t0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH\'J\u0018\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH\'J\"\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u000e\u001a\u00020\u000b2\b\b\u0001\u0010\u000f\u001a\u00020\u000bH\'J\u0018\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\"\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0014\u001a\u00020\u0015H\'\u00a8\u0006\u0016"}, d2 = {"Lcom/android/chatsapp/api/UserApiService;", "", "addUser", "Lretrofit2/Call;", "Lokhttp3/ResponseBody;", "user", "Lcom/android/chatsapp/model/User;", "getAllUsers", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "currentUser", "", "getUser", "updateStatus", "uid", "status", "updateUser", "uploadImg", "file", "Lokhttp3/MultipartBody$Part;", "UserId", "Lokhttp3/RequestBody;", "app_debug"})
public abstract interface UserApiService {
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "/user")
    public abstract retrofit2.Call<okhttp3.ResponseBody> addUser(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.android.chatsapp.model.User user);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.PUT(value = "/user")
    public abstract retrofit2.Call<okhttp3.ResponseBody> updateUser(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.android.chatsapp.model.User user);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "/upload")
    @retrofit2.http.Multipart
    public abstract retrofit2.Call<okhttp3.ResponseBody> uploadImg(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Part
    okhttp3.MultipartBody.Part file, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Part(value = "userId")
    okhttp3.RequestBody UserId);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.GET(value = "/users")
    public abstract retrofit2.Call<java.util.ArrayList<com.android.chatsapp.model.User>> getAllUsers(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "currentUser")
    java.lang.String currentUser);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.GET(value = "/user")
    public abstract retrofit2.Call<com.android.chatsapp.model.User> getUser(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "currentUserId")
    java.lang.String currentUser);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.PUT(value = "/userStatus")
    public abstract retrofit2.Call<okhttp3.ResponseBody> updateStatus(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "uid")
    java.lang.String uid, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "status")
    java.lang.String status);
}