package com.android.chatsapp.notification.network;

import com.android.chatsapp.notification.entity.PushNotification;
import com.google.gson.JsonElement;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\u0007"}, d2 = {"Lcom/android/chatsapp/notification/network/NotificationApi;", "", "postNotification", "Lretrofit2/Call;", "Lcom/google/gson/JsonElement;", "notification", "Lcom/android/chatsapp/notification/entity/PushNotification;", "app_debug"})
public abstract interface NotificationApi {
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.POST(value = "/fcm/send")
    @retrofit2.http.Headers(value = {"Authorization:key=AAAAy5GovvM:APA91bFNHPrm2IjDM0zv5Zvn9PStPfAnIjp58w1kGnaIHFlVmxQft82T85q6CoVNqqy9eUQLKfdXXTvC6W7E7_4moGwlC_r56zTCqCB6F-cZ_sSKoI3neLgZ4kWbiaTdtZB4QfpmLtPK", "Content-Type:application/json"})
    public abstract retrofit2.Call<com.google.gson.JsonElement> postNotification(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.android.chatsapp.notification.entity.PushNotification notification);
}