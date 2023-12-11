package com.android.chatsapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.chatsapp.R;
import com.android.chatsapp.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0016\u0017B-\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u0018\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/android/chatsapp/adapters/UserAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/android/chatsapp/adapters/UserAdapter$ViewHolder;", "list", "Ljava/util/ArrayList;", "Lcom/android/chatsapp/model/User;", "Lkotlin/collections/ArrayList;", "context", "Landroid/content/Context;", "listener", "Lcom/android/chatsapp/adapters/UserAdapter$RecycleViewEvent;", "(Ljava/util/ArrayList;Landroid/content/Context;Lcom/android/chatsapp/adapters/UserAdapter$RecycleViewEvent;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewEvent", "ViewHolder", "app_debug"})
public final class UserAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.android.chatsapp.adapters.UserAdapter.ViewHolder> {
    private final java.util.ArrayList<com.android.chatsapp.model.User> list = null;
    private final android.content.Context context = null;
    private final com.android.chatsapp.adapters.UserAdapter.RecycleViewEvent listener = null;
    
    public UserAdapter(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.android.chatsapp.model.User> list, @org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.android.chatsapp.adapters.UserAdapter.RecycleViewEvent listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.android.chatsapp.adapters.UserAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.android.chatsapp.adapters.UserAdapter.ViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0012\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0004H\u0016R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcom/android/chatsapp/adapters/UserAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Landroid/view/View$OnClickListener;", "itemView", "Landroid/view/View;", "listener", "Lcom/android/chatsapp/adapters/UserAdapter$RecycleViewEvent;", "(Landroid/view/View;Lcom/android/chatsapp/adapters/UserAdapter$RecycleViewEvent;)V", "lastmesage", "Landroid/widget/TextView;", "getLastmesage", "()Landroid/widget/TextView;", "profilePicture", "Lde/hdodenhof/circleimageview/CircleImageView;", "getProfilePicture", "()Lde/hdodenhof/circleimageview/CircleImageView;", "username", "getUsername", "onClick", "", "p0", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements android.view.View.OnClickListener {
        private final com.android.chatsapp.adapters.UserAdapter.RecycleViewEvent listener = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView username = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView lastmesage = null;
        @org.jetbrains.annotations.NotNull
        private final de.hdodenhof.circleimageview.CircleImageView profilePicture = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView, @org.jetbrains.annotations.NotNull
        com.android.chatsapp.adapters.UserAdapter.RecycleViewEvent listener) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getUsername() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getLastmesage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final de.hdodenhof.circleimageview.CircleImageView getProfilePicture() {
            return null;
        }
        
        @java.lang.Override
        public void onClick(@org.jetbrains.annotations.Nullable
        android.view.View p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/android/chatsapp/adapters/UserAdapter$RecycleViewEvent;", "", "onItemClick", "", "position", "", "app_debug"})
    public static abstract interface RecycleViewEvent {
        
        public abstract void onItemClick(int position);
    }
}