// Generated by view binder compiler. Do not edit!
package com.example.kaupark.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.kaupark.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class HomeViewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView expectedfee;

  @NonNull
  public final View feeview;

  @NonNull
  public final ConstraintLayout frameLayout2;

  @NonNull
  public final Toolbar hometoolbar;

  @NonNull
  public final Button inbutton;

  @NonNull
  public final Button manageprofile;

  @NonNull
  public final FragmentContainerView mapimage;

  @NonNull
  public final TextView maptextview;

  @NonNull
  public final TextView myInfo;

  @NonNull
  public final Button outbutton;

  @NonNull
  public final ImageView parkinglogo;

  @NonNull
  public final ScrollView scrollview;

  @NonNull
  public final ConstraintLayout secondconst;

  @NonNull
  public final TextView usercarnum;

  @NonNull
  public final CircleImageView userimage;

  @NonNull
  public final View userview;

  private HomeViewBinding(@NonNull ConstraintLayout rootView, @NonNull TextView expectedfee,
      @NonNull View feeview, @NonNull ConstraintLayout frameLayout2, @NonNull Toolbar hometoolbar,
      @NonNull Button inbutton, @NonNull Button manageprofile,
      @NonNull FragmentContainerView mapimage, @NonNull TextView maptextview,
      @NonNull TextView myInfo, @NonNull Button outbutton, @NonNull ImageView parkinglogo,
      @NonNull ScrollView scrollview, @NonNull ConstraintLayout secondconst,
      @NonNull TextView usercarnum, @NonNull CircleImageView userimage, @NonNull View userview) {
    this.rootView = rootView;
    this.expectedfee = expectedfee;
    this.feeview = feeview;
    this.frameLayout2 = frameLayout2;
    this.hometoolbar = hometoolbar;
    this.inbutton = inbutton;
    this.manageprofile = manageprofile;
    this.mapimage = mapimage;
    this.maptextview = maptextview;
    this.myInfo = myInfo;
    this.outbutton = outbutton;
    this.parkinglogo = parkinglogo;
    this.scrollview = scrollview;
    this.secondconst = secondconst;
    this.usercarnum = usercarnum;
    this.userimage = userimage;
    this.userview = userview;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static HomeViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static HomeViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.home_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static HomeViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.expectedfee;
      TextView expectedfee = ViewBindings.findChildViewById(rootView, id);
      if (expectedfee == null) {
        break missingId;
      }

      id = R.id.feeview;
      View feeview = ViewBindings.findChildViewById(rootView, id);
      if (feeview == null) {
        break missingId;
      }

      ConstraintLayout frameLayout2 = (ConstraintLayout) rootView;

      id = R.id.hometoolbar;
      Toolbar hometoolbar = ViewBindings.findChildViewById(rootView, id);
      if (hometoolbar == null) {
        break missingId;
      }

      id = R.id.inbutton;
      Button inbutton = ViewBindings.findChildViewById(rootView, id);
      if (inbutton == null) {
        break missingId;
      }

      id = R.id.manageprofile;
      Button manageprofile = ViewBindings.findChildViewById(rootView, id);
      if (manageprofile == null) {
        break missingId;
      }

      id = R.id.mapimage;
      FragmentContainerView mapimage = ViewBindings.findChildViewById(rootView, id);
      if (mapimage == null) {
        break missingId;
      }

      id = R.id.maptextview;
      TextView maptextview = ViewBindings.findChildViewById(rootView, id);
      if (maptextview == null) {
        break missingId;
      }

      id = R.id.myInfo;
      TextView myInfo = ViewBindings.findChildViewById(rootView, id);
      if (myInfo == null) {
        break missingId;
      }

      id = R.id.outbutton;
      Button outbutton = ViewBindings.findChildViewById(rootView, id);
      if (outbutton == null) {
        break missingId;
      }

      id = R.id.parkinglogo;
      ImageView parkinglogo = ViewBindings.findChildViewById(rootView, id);
      if (parkinglogo == null) {
        break missingId;
      }

      id = R.id.scrollview;
      ScrollView scrollview = ViewBindings.findChildViewById(rootView, id);
      if (scrollview == null) {
        break missingId;
      }

      id = R.id.secondconst;
      ConstraintLayout secondconst = ViewBindings.findChildViewById(rootView, id);
      if (secondconst == null) {
        break missingId;
      }

      id = R.id.usercarnum;
      TextView usercarnum = ViewBindings.findChildViewById(rootView, id);
      if (usercarnum == null) {
        break missingId;
      }

      id = R.id.userimage;
      CircleImageView userimage = ViewBindings.findChildViewById(rootView, id);
      if (userimage == null) {
        break missingId;
      }

      id = R.id.userview;
      View userview = ViewBindings.findChildViewById(rootView, id);
      if (userview == null) {
        break missingId;
      }

      return new HomeViewBinding((ConstraintLayout) rootView, expectedfee, feeview, frameLayout2,
          hometoolbar, inbutton, manageprofile, mapimage, maptextview, myInfo, outbutton,
          parkinglogo, scrollview, secondconst, usercarnum, userimage, userview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
