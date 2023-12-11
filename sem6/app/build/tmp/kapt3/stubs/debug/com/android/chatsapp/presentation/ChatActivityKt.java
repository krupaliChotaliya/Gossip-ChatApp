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

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0002"}, d2 = {"TOPIC", "", "app_debug"})
public final class ChatActivityKt {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String TOPIC = "/topics/myTopic";
}