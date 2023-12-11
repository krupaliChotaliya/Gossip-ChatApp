package com.android.chatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006J\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0006R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/android/chatsapp/helper/SharedPrefManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "SharedPrefName", "", "editor", "Landroid/content/SharedPreferences$Editor;", "sharedPreferences", "Landroid/content/SharedPreferences;", "isLoggedIn", "logout", "", "saveUser", "", "userID", "app_debug"})
public final class SharedPrefManager {
    private java.lang.String SharedPrefName = "chatsapp";
    private android.content.SharedPreferences sharedPreferences;
    private android.content.Context context;
    private android.content.SharedPreferences.Editor editor;
    
    public SharedPrefManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final void saveUser(@org.jetbrains.annotations.NotNull
    java.lang.String userID) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String isLoggedIn() {
        return null;
    }
    
    public final boolean logout() {
        return false;
    }
}