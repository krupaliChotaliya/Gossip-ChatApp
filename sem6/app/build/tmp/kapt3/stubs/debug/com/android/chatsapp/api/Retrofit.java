package com.android.chatsapp.api;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/android/chatsapp/api/Retrofit;", "", "()V", "Companion", "app_debug"})
public final class Retrofit {
    @org.jetbrains.annotations.NotNull
    public static final com.android.chatsapp.api.Retrofit.Companion Companion = null;
    private static com.google.gson.Gson gson;
    
    public Retrofit() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fR\"\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\u0010"}, d2 = {"Lcom/android/chatsapp/api/Retrofit$Companion;", "", "()V", "gson", "Lcom/google/gson/Gson;", "kotlin.jvm.PlatformType", "getGson", "()Lcom/google/gson/Gson;", "setGson", "(Lcom/google/gson/Gson;)V", "createOkHttpClient", "Lokhttp3/OkHttpClient;", "createRetrofitInstance", "Lretrofit2/Retrofit;", "baseUrl", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final okhttp3.OkHttpClient createOkHttpClient() {
            return null;
        }
        
        public final com.google.gson.Gson getGson() {
            return null;
        }
        
        public final void setGson(com.google.gson.Gson p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final retrofit2.Retrofit createRetrofitInstance(@org.jetbrains.annotations.NotNull
        java.lang.String baseUrl) {
            return null;
        }
    }
}