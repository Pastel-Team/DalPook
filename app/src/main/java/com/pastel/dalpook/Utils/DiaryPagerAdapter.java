package com.pastel.dalpook.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pastel.dalpook.Calendar.DiaryActivity;
import com.pastel.dalpook.Popup.CreateDiaryActivity;
import com.pastel.dalpook.R;
import com.pastel.dalpook.data.DiaryModels;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryPagerAdapter extends PagerAdapter {

    List<DiaryModels> eventLists;
    Context context;
    LayoutInflater layoutInflater;

    private DiaryPagerAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{ // 인터페이스 정의
        void onModify(int pos, DiaryModels event);
        void onDelete(int pos, DiaryModels event);
    }

    public DiaryPagerAdapter(List<DiaryModels> eventLists, Context context, OnItemClickListener listener){
        this.eventLists = eventLists;
        this.context = context;
        this.mListener = listener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pager_diary_day, container, false);
        TextView txtDate = (TextView)view.findViewById(R.id.tv_diary_day);
        TextView txtTitle = (TextView)view.findViewById(R.id.txt_title);
        TextView txtDesc = (TextView)view.findViewById(R.id.txt_desc);
        ImageView imageView = (ImageView)view.findViewById(R.id.iv_dialog_diary);
        Button btn_mod = (Button)view.findViewById(R.id.btn_mod);

        if(eventLists.get(position).getmImg().equals("")){
            Glide.with(context)
                    .load(R.drawable.diary_default)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageView);
        }else{
            Uri uri = Uri.parse(eventLists.get(position).getmImg());
            imageView.setImageURI(uri);
            if(imageView.getDrawable() == null){
                Glide.with(context)
                        .load(R.drawable.diary_default)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(imageView);
            }
        }
        //txtDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(eventLists.get(position).getmDate()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        calendar = eventLists.get(position).getmDate();
        txtDate.setText(dateFormat.format(calendar.getTime()));
        txtTitle.setText(eventLists.get(position).getmTitle());
        txtDesc.setText(eventLists.get(position).getmDesc());

        btn_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onModify(position, eventLists.get(position));
            }
        });

        container.addView(view);

        return view;
    }
}
