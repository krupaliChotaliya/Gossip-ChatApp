package com.android.chatsapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B1\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0013H\u0016J\u0010\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u000e\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0013R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/android/chatsapp/adapters/ViewPagerAdapter;", "Landroidx/viewpager2/adapter/FragmentStateAdapter;", "items", "Ljava/util/ArrayList;", "Landroidx/fragment/app/Fragment;", "fragmentTitles", "", "", "fragmentManager", "Landroidx/fragment/app/FragmentManager;", "lifecycle", "Landroidx/lifecycle/Lifecycle;", "(Ljava/util/ArrayList;Ljava/util/List;Landroidx/fragment/app/FragmentManager;Landroidx/lifecycle/Lifecycle;)V", "containsItem", "", "itemId", "", "createFragment", "position", "", "getItemCount", "getItemId", "getTitle", "app_debug"})
public final class ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
    private final java.util.ArrayList<androidx.fragment.app.Fragment> items = null;
    private final java.util.List<java.lang.String> fragmentTitles = null;
    
    public ViewPagerAdapter(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<androidx.fragment.app.Fragment> items, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> fragmentTitles, @org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager fragmentManager, @org.jetbrains.annotations.NotNull
    androidx.lifecycle.Lifecycle lifecycle) {
        super(null);
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public androidx.fragment.app.Fragment createFragment(int position) {
        return null;
    }
    
    @java.lang.Override
    public long getItemId(int position) {
        return 0L;
    }
    
    @java.lang.Override
    public boolean containsItem(long itemId) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle(int position) {
        return null;
    }
}