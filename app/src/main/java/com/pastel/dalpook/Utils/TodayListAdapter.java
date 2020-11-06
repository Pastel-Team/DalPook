package com.pastel.dalpook.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pastel.dalpook.Calendar.DiaryActivity;
import com.pastel.dalpook.Calendar.LessonActivity;
import com.pastel.dalpook.Calendar.MonthActivity;
import com.pastel.dalpook.Calendar.WeekActivity;
import com.pastel.dalpook.Calendar.WorkActivity;
import com.pastel.dalpook.R;

import java.time.Month;
import java.util.ArrayList;

public class TodayListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TodayModels> itemList = new ArrayList<TodayModels>();

    public TodayListAdapter(Context context){
        this.mContext = context;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_today, viewGroup, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView txt_time = (TextView) view.findViewById(R.id.txt_time) ;
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        TodayModels listViewItem = itemList.get(i);

        // 아이템 내 각 위젯에 데이터 반영
        txt_time.setText(listViewItem.getTime());
        txt_content.setText(listViewItem.getCont());

        final LoadingDialog loadingDialog = new LoadingDialog(mContext);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String flag = itemList.get(i).getFlag();

                loadingDialog.progressOn();
                Intent intent;
                new Thread() {
                    @Override
                    public void run() {
                        Intent intent;
                        switch (flag){
                            case "M":
                                intent = new Intent(mContext, MonthActivity.class);
                                mContext.startActivity(intent);
                                break;
                            case "W":
                                intent = new Intent(mContext, WeekActivity.class);
                                mContext.startActivity(intent);
                                break;
                            case "L":
                                intent = new Intent(mContext, LessonActivity.class);
                                mContext.startActivity(intent);
                                break;
                            case "B":
                                intent = new Intent(mContext, WorkActivity.class);
                                mContext.startActivity(intent);
                                break;
                            case "D":
                                intent = new Intent(mContext, DiaryActivity.class);
                                mContext.startActivity(intent);
                                break;
                        }
                        loadingDialog.progressOff();
                    }
                }.start();

            }
        });

        return view;
    }

    public void addItem(String time, String content, String flag) {
        TodayModels item = new TodayModels();

        item.setTime(time);
        item.setCont(content);
        item.setFlag(flag);
        itemList.add(item);

        notifyDataSetChanged();
    }
}
