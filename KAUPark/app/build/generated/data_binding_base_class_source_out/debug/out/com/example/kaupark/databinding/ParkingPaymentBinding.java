// Generated by view binder compiler. Do not edit!
package com.example.kaupark.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.kaupark.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ParkingPaymentBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Spinner ParkingCarNumber;

  @NonNull
  public final ImageButton button;

  @NonNull
  public final Button button2;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final LinearLayout main;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final TextView textView5;

  @NonNull
  public final TextView textView6;

  @NonNull
  public final TextView title;

  private ParkingPaymentBinding(@NonNull LinearLayout rootView, @NonNull Spinner ParkingCarNumber,
      @NonNull ImageButton button, @NonNull Button button2, @NonNull ImageView imageView,
      @NonNull LinearLayout main, @NonNull TextView textView2, @NonNull TextView textView3,
      @NonNull TextView textView4, @NonNull TextView textView5, @NonNull TextView textView6,
      @NonNull TextView title) {
    this.rootView = rootView;
    this.ParkingCarNumber = ParkingCarNumber;
    this.button = button;
    this.button2 = button2;
    this.imageView = imageView;
    this.main = main;
    this.textView2 = textView2;
    this.textView3 = textView3;
    this.textView4 = textView4;
    this.textView5 = textView5;
    this.textView6 = textView6;
    this.title = title;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ParkingPaymentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ParkingPaymentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.parking_payment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ParkingPaymentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ParkingCar_Number;
      Spinner ParkingCarNumber = ViewBindings.findChildViewById(rootView, id);
      if (ParkingCarNumber == null) {
        break missingId;
      }

      id = R.id.button;
      ImageButton button = ViewBindings.findChildViewById(rootView, id);
      if (button == null) {
        break missingId;
      }

      id = R.id.button2;
      Button button2 = ViewBindings.findChildViewById(rootView, id);
      if (button2 == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      LinearLayout main = (LinearLayout) rootView;

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      id = R.id.textView5;
      TextView textView5 = ViewBindings.findChildViewById(rootView, id);
      if (textView5 == null) {
        break missingId;
      }

      id = R.id.textView6;
      TextView textView6 = ViewBindings.findChildViewById(rootView, id);
      if (textView6 == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      return new ParkingPaymentBinding((LinearLayout) rootView, ParkingCarNumber, button, button2,
          imageView, main, textView2, textView3, textView4, textView5, textView6, title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}