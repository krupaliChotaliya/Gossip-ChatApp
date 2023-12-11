package com.android.chatsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.android.chatsapp.adapters.UserAdapter;
import com.android.chatsapp.api.Retrofit;
import com.android.chatsapp.api.UserApiService;
import com.android.chatsapp.databinding.FragmentChatsBinding;
import com.android.chatsapp.model.User;
import com.android.chatsapp.presentation.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0006\u0010\u0010\u001a\u00020\u0011J&\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0011H\u0016J\u001a\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u00132\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\b\u0010 \u001a\u00020\u0011H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\f\u001a\u0012\u0012\u0004\u0012\u00020\u000e0\rj\b\u0012\u0004\u0012\u00020\u000e`\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/android/chatsapp/fragments/ChatsFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/android/chatsapp/adapters/UserAdapter$RecycleViewEvent;", "()V", "CurrentUser", "", "apiService", "Lcom/android/chatsapp/api/UserApiService;", "kotlin.jvm.PlatformType", "baseUrl", "binding", "Lcom/android/chatsapp/databinding/FragmentChatsBinding;", "userList", "Ljava/util/ArrayList;", "Lcom/android/chatsapp/model/User;", "Lkotlin/collections/ArrayList;", "getUsers", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onItemClick", "position", "", "onStart", "onViewCreated", "view", "setUpRecyclerView", "app_debug"})
public final class ChatsFragment extends androidx.fragment.app.Fragment implements com.android.chatsapp.adapters.UserAdapter.RecycleViewEvent {
    private com.android.chatsapp.databinding.FragmentChatsBinding binding;
    private java.util.ArrayList<com.android.chatsapp.model.User> userList;
    private java.lang.String baseUrl = "http://192.168.125.10:8080/";
    private final com.android.chatsapp.api.UserApiService apiService = null;
    private java.lang.String CurrentUser;
    
    public ChatsFragment() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpRecyclerView() {
    }
    
    @java.lang.Override
    public void onItemClick(int position) {
    }
    
    @java.lang.Override
    public void onStart() {
    }
    
    public final void getUsers() {
    }
}