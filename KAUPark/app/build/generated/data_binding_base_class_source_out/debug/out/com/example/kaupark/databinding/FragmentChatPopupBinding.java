// Generated by view binder compiler. Do not edit!
package com.example.kaupark.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.kaupark.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentChatPopupBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText blankText;

  @NonNull
  public final Button closeBtn;

  @NonNull
  public final TextView popupTitle;

  @NonNull
  public final Button sendBtn;

  @NonNull
  public final TextView userName;

  @NonNull
  public final View view;

  private FragmentChatPopupBinding(@NonNull ConstraintLayout rootView, @NonNull EditText blankText,
      @NonNull Button closeBtn, @NonNull TextView popupTitle, @NonNull Button sendBtn,
      @NonNull TextView userName, @NonNull View view) {
    this.rootView = rootView;
    this.blankText = blankText;
    this.closeBtn = closeBtn;
    this.popupTitle = popupTitle;
    this.sendBtn = sendBtn;
    this.userName = userName;
    this.view = view;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentChatPopupBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentChatPopupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_chat_popup, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentChatPopupBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.blank_text;
      EditText blankText = ViewBindings.findChildViewById(rootView, id);
      if (blankText == null) {
        break missingId;
      }

      id = R.id.close_btn;
      Button closeBtn = ViewBindings.findChildViewById(rootView, id);
      if (closeBtn == null) {
        break missingId;
      }

      id = R.id.popup_title;
      TextView popupTitle = ViewBindings.findChildViewById(rootView, id);
      if (popupTitle == null) {
        break missingId;
      }

      id = R.id.send_btn;
      Button sendBtn = ViewBindings.findChildViewById(rootView, id);
      if (sendBtn == null) {
        break missingId;
      }

      id = R.id.user_name;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      id = R.id.view;
      View view = ViewBindings.findChildViewById(rootView, id);
      if (view == null) {
        break missingId;
      }

      return new FragmentChatPopupBinding((ConstraintLayout) rootView, blankText, closeBtn,
          popupTitle, sendBtn, userName, view);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
