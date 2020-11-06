package com.pastel.dalpook.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.pastel.dalpook.R;

public class LoadingDialog extends AppCompatDialog{

    AppCompatDialog progressDialog;
    Context mContext;

    public LoadingDialog(Context context) {
        super(context);

        this.mContext = context;
    }

    public void progressOn() {
        if (mContext == null ) {
            return;
        }

        progressDialog = new AppCompatDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.show();
        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_dialog);

        Glide.with(mContext).asGif()
                .load(R.drawable.ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(img_loading_frame);
    }

    public void progressOff() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
