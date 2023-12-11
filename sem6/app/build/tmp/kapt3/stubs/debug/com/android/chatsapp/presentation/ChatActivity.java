package com.android.chatsapp.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.android.chatsapp.R;
import com.android.chatsapp.adapters.MessageAdapter;
import com.android.chatsapp.api.MessageApiService;
import com.android.chatsapp.api.Retrofit;
import com.android.chatsapp.api.UserApiService;
import com.android.chatsapp.databinding.ActivityChatBinding;
import com.android.chatsapp.model.Message;
import com.android.chatsapp.model.User;
import com.android.chatsapp.notification.FirebaseService;
import com.android.chatsapp.notification.entity.NotificationData;
import com.android.chatsapp.notification.entity.PushNotification;
import com.android.chatsapp.notification.network.RetrofitInstance;
import com.android.chatsapp.viewmodel.UserViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import kotlinx.coroutines.Dispatchers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.ByteArrayOutputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010&\u001a\u00020\u00042\u0006\u0010\'\u001a\u00020(J\n\u0010)\u001a\u0004\u0018\u00010\u0004H\u0007J\u0010\u0010*\u001a\u00020+2\u0006\u0010$\u001a\u00020\u0004H\u0002J\u0006\u0010,\u001a\u00020+J\"\u0010-\u001a\u00020+2\u0006\u0010.\u001a\u00020(2\u0006\u0010/\u001a\u00020(2\b\u00100\u001a\u0004\u0018\u000101H\u0014J\b\u00102\u001a\u00020+H\u0017J\u0012\u00103\u001a\u00020+2\b\u00104\u001a\u0004\u0018\u000105H\u0015J\b\u00106\u001a\u00020+H\u0014J\b\u00107\u001a\u00020+H\u0014J\b\u00108\u001a\u000209H\u0017J\u0010\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020\u001cH\u0002J\u0010\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020?H\u0002J\u000e\u0010@\u001a\u00020+2\u0006\u0010A\u001a\u00020\u0004J\u0013\u0010B\u001a\u0004\u0018\u00010\u0004H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010CR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u001a\u001a\u0012\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006D"}, d2 = {"Lcom/android/chatsapp/presentation/ChatActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "FCMTOKEN", "", "adapter", "Lcom/android/chatsapp/adapters/MessageAdapter;", "baseUrl", "binding", "Lcom/android/chatsapp/databinding/ActivityChatBinding;", "bitmap", "Landroid/graphics/Bitmap;", "collection", "Lcom/google/firebase/firestore/CollectionReference;", "date", "Ljava/util/Date;", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "itemViewModel", "Lcom/android/chatsapp/viewmodel/UserViewModel;", "getItemViewModel", "()Lcom/android/chatsapp/viewmodel/UserViewModel;", "itemViewModel$delegate", "Lkotlin/Lazy;", "loadingbar", "Landroid/app/ProgressDialog;", "messages", "Ljava/util/ArrayList;", "Lcom/android/chatsapp/model/Message;", "Lkotlin/collections/ArrayList;", "receiver", "Lcom/android/chatsapp/model/User;", "receiverRoom", "retrofit", "Lretrofit2/Retrofit;", "senderRoom", "senderUid", "userId", "generateRandomPattern", "length", "", "getCurrentTime", "getMessages", "", "getRecevierStatus", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "onStart", "onSupportNavigateUp", "", "saveMessage", "message", "sendNotification", "Lkotlinx/coroutines/Job;", "notification", "Lcom/android/chatsapp/notification/entity/PushNotification;", "updateStatus", "status", "uploadImg", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ChatActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.android.chatsapp.databinding.ActivityChatBinding binding;
    private com.android.chatsapp.adapters.MessageAdapter adapter;
    private java.lang.String senderRoom;
    private java.lang.String receiverRoom;
    private java.lang.String senderUid;
    private java.lang.String baseUrl = "http://192.168.125.10:8080/";
    private android.graphics.Bitmap bitmap;
    private final retrofit2.Retrofit retrofit = null;
    private android.app.ProgressDialog loadingbar;
    private java.util.Date date;
    private java.lang.String userId;
    private com.android.chatsapp.model.User receiver;
    private final kotlin.Lazy itemViewModel$delegate = null;
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    private final com.google.firebase.firestore.CollectionReference collection = null;
    private java.util.ArrayList<com.android.chatsapp.model.Message> messages;
    private java.lang.String FCMTOKEN;
    
    public ChatActivity() {
        super();
    }
    
    private final com.android.chatsapp.viewmodel.UserViewModel getItemViewModel() {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void getMessages(java.lang.String senderUid) {
    }
    
    private final void saveMessage(com.android.chatsapp.model.Message message) {
    }
    
    @java.lang.Override
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable
    android.content.Intent data) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object uploadImg(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> continuation) {
        return null;
    }
    
    public final void updateStatus(@org.jetbrains.annotations.NotNull
    java.lang.String status) {
    }
    
    public final void getRecevierStatus() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String generateRandomPattern(int length) {
        return null;
    }
    
    @java.lang.Override
    protected void onStart() {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final java.lang.String getCurrentTime() {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @java.lang.Override
    public void onBackPressed() {
    }
    
    private final kotlinx.coroutines.Job sendNotification(com.android.chatsapp.notification.entity.PushNotification notification) {
        return null;
    }
}