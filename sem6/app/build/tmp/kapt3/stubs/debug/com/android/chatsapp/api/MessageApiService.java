package com.android.chatsapp.api;

import com.android.chatsapp.model.Message;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u00060\u00032\b\b\u0001\u0010\u0007\u001a\u00020\bH\'J,\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u000b\u001a\u00020\u00052\b\b\u0001\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\f\u001a\u00020\bH\'\u00a8\u0006\r"}, d2 = {"Lcom/android/chatsapp/api/MessageApiService;", "", "getMessages", "Lretrofit2/Call;", "Ljava/util/ArrayList;", "Lcom/android/chatsapp/model/Message;", "Lkotlin/collections/ArrayList;", "senderRoom", "", "saveMessage", "Lokhttp3/ResponseBody;", "message", "receiverRoom", "app_debug"})
public abstract interface MessageApiService {
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "/message")
    public abstract retrofit2.Call<okhttp3.ResponseBody> saveMessage(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.android.chatsapp.model.Message message, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "senderRoom")
    java.lang.String senderRoom, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "receiverRoom")
    java.lang.String receiverRoom);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.GET(value = "/message")
    public abstract retrofit2.Call<java.util.ArrayList<com.android.chatsapp.model.Message>> getMessages(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "senderRoom")
    java.lang.String senderRoom);
}