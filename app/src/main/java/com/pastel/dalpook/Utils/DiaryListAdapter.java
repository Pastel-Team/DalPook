package com.pastel.dalpook.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pastel.dalpook.Calendar.DiaryActivity;
import com.pastel.dalpook.Calendar.LessonActivity;
import com.pastel.dalpook.Calendar.MonthActivity;
import com.pastel.dalpook.Calendar.WeekActivity;
import com.pastel.dalpook.Calendar.WorkActivity;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Popup.DiaryDialogActivity;
import com.pastel.dalpook.R;
import com.pastel.dalpook.data.DiaryModels;
import com.pastel.dalpook.data.TodayModels;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DiaryListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DiaryModels> itemList = new ArrayList<DiaryModels>();
    private DiaryListAdapter.OnItemClickListener mListener;

    public DiaryListAdapter(Context context, OnItemClickListener listener){
        this.mContext = context;
        this.mListener = listener;
    }

    public interface OnItemClickListener{ // 인터페이스 정의
        void onItemClick(int pos, DiaryModels event);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_diary, viewGroup, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title) ;
        TextView txt_date = (TextView) view.findViewById(R.id.txt_date) ;
        ImageView iv_img = (ImageView) view.findViewById(R.id.iv_diary);
        Button btn_delete = (Button) view.findViewById(R.id.btn_diary_delete);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        DiaryModels listViewItem = itemList.get(i);

        // 아이템 내 각 위젯에 데이터 반영
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        calendar = listViewItem.getmDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String date = dateFormat.format(calendar.getTime());

        txt_date.setText(date);
        txt_title.setText(listViewItem.getmTitle());
        if(listViewItem.getmImg().equals("")){
            Glide.with(mContext)
                    .load(R.drawable.diary_default)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(iv_img);
        }else{
            Uri uri = Uri.parse(listViewItem.getmImg());
            iv_img.setImageURI(uri);
            if(iv_img.getDrawable() == null){
                Glide.with(mContext)
                        .load(R.drawable.diary_default)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(iv_img);
            }
        }

        final LoadingDialog loadingDialog = new LoadingDialog(mContext);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DB Delete
                Calendar deleteCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

                DBHelper dbHelper = new DBHelper(mContext);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                deleteCal = listViewItem.getmDate();

                dbHelper.deleteDiary(dateFormat.format(deleteCal.getTime()));

                itemList.remove(i);
                notifyDataSetInvalidated();
            }
        });

        // 뷰페이저 다이얼로그 호출
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(pos, listViewItem);
            }
        });

        return view;
    }

    public void addItem(Calendar date, String title, String desc, String uri) {
        DiaryModels item = new DiaryModels(date, title, desc, uri);
        itemList.add(item);

        notifyDataSetChanged();
    }
}
