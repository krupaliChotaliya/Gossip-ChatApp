// Generated by view binder compiler. Do not edit!
package com.android.chatsapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.android.chatsapp.R;
import com.devlomi.circularstatusview.CircularStatusView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemStatusviewBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final CircularStatusView circularStatusView;

  @NonNull
  public final RelativeLayout imageLayout;

  @NonNull
  public final CircleImageView receiverStatus;

  private ItemStatusviewBinding(@NonNull RelativeLayout rootView,
      @NonNull CircularStatusView circularStatusView, @NonNull RelativeLayout imageLayout,
      @NonNull CircleImageView receiverStatus) {
    this.rootView = rootView;
    this.circularStatusView = circularStatusView;
    this.imageLayout = imageLayout;
    this.receiverStatus = receiverStatus;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemStatusviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemStatusviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_statusview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemStatusviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.circular_status_view;
      CircularStatusView circularStatusView = ViewBindings.findChildViewById(rootView, id);
      if (circularStatusView == null) {
        break missingId;
      }

      RelativeLayout imageLayout = (RelativeLayout) rootView;

      id = R.id.receiverStatus;
      CircleImageView receiverStatus = ViewBindings.findChildViewById(rootView, id);
      if (receiverStatus == null) {
        break missingId;
      }

      return new ItemStatusviewBinding((RelativeLayout) rootView, circularStatusView, imageLayout,
          receiverStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}