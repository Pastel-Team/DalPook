package com.pastel.dalpook.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.pastel.dalpook.R;

public class LoadingDialog {

    Dialog dialog;
    Activity activity;
    int loadImageGif;
    Boolean cancelable = false;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    //set drawable of loading gif
    public void setLoadImage(int img){
        this.loadImageGif = img;
    }

    //(optional)if user can cancel(click out of layout)
    public void setCancelable(Boolean state){
        this.cancelable = state;
    }

    public void show() {
        if(loadImageGif!=0){
            dialog  = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //inflate the layout
            dialog.setContentView(R.layout.dialog_loading);
            //setup cancelable, default=false
            dialog.setCancelable(cancelable);
            //get imageview to use in Glide
            ImageView imageView = dialog.findViewById(R.id.iv_frame_dialog);
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(imageView);

            //load gif and callback to imageview
            Glide.with(activity)
                    .load(loadImageGif)
                    .placeholder(loadImageGif)
                    .centerCrop()
                    .into(imageViewTarget);

            dialog.show();
        }else{
            Log.e("LoadingDialog", "Erro, missing drawable of imageloading (gif), please, use setLoadImage(R.drawable.name).");
        }
    }

    //Dismiss the dialog
    public void hide(){
        dialog.dismiss();
    }
}
