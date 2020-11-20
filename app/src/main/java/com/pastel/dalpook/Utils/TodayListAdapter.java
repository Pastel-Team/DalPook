package com.pastel.dalpook.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pastel.dalpook.Calendar.DiaryActivity;
import com.pastel.dalpook.Calendar.LessonActivity;
import com.pastel.dalpook.Calendar.MonthActivity;
import com.pastel.dalpook.Calendar.WeekActivity;
import com.pastel.dalpook.Calendar.WorkActivity;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Fragment.Fragment_Cal;
import com.pastel.dalpook.R;
import com.pastel.dalpook.data.TodayModels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TodayListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TodayModels> itemList = new ArrayList<TodayModels>();

    public TodayListAdapter(Context context){
        this.mContext = context;
    }

    public interface OnItemClickListener{ // 인터페이스 정의
        void onItemClick(int pos);
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
        Button btn_delete = (Button) view.findViewById(R.id.btn_today_list_delete);
        View view_color = (View) view.findViewById(R.id.view_today_color);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        TodayModels listViewItem = itemList.get(i);

        // 아이템 내 각 위젯에 데이터 반영
        String setTime = "";
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        calendar = listViewItem.getTime();

        int isAMorPM = calendar.get(Calendar.AM_PM);
        int Hour = calendar.get(Calendar.HOUR);
        String AMHour ="";
        if(Hour >= 0 && Hour < 10){
            AMHour = "0"+calendar.get(Calendar.HOUR);
            if(AMHour.equals("00")){
                AMHour = "12";
            }
        }else{
            AMHour = String.valueOf(calendar.get(Calendar.HOUR));
        }

        int Minute = calendar.get(Calendar.MINUTE);
        String AMMinute ="";
        if(Minute >= 0 && Minute < 10){
            AMMinute = "0"+ calendar.get(Calendar.MINUTE);
        }else{
            AMMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        switch (isAMorPM){
            case Calendar.AM :
                setTime = "오전 " + AMHour + ":" + AMMinute;
                break;
            case Calendar.PM :
                setTime = "오후 " + AMHour + ":" + AMMinute;
                break;
        }


        txt_time.setText(setTime);
        txt_content.setText(listViewItem.getCont());

        GradientDrawable mGradientDrawable = (GradientDrawable) view_color.getBackground();
        mGradientDrawable.setStroke(10, listViewItem.getColor());
        mGradientDrawable.setColor(listViewItem.getColor());

        final LoadingDialog loadingDialog = new LoadingDialog(mContext);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DB Delete
                Calendar deleteCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

                DBHelper dbHelper = new DBHelper(mContext);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                deleteCal = listViewItem.getTime();
                String Sec = "";
                if(deleteCal.get(Calendar.SECOND) < 10){
                    Sec = "0" + String.valueOf(deleteCal.get(Calendar.SECOND));
                }else{
                    Sec = String.valueOf(deleteCal.get(Calendar.SECOND));
                }

                String date = "";
                String flag = itemList.get(i).getFlag();
                if(flag.equals("M")){
                    date = dateFormat.format(deleteCal.getTime());
                }else if(flag.equals("W")){
                    date = String.valueOf(deleteCal.get(Calendar.DAY_OF_WEEK));
                }
                String time = timeFormat.format(deleteCal.getTime()) + ":"+Sec;
                dbHelper.deleteConts(date, time,"W");

                itemList.remove(i);
                notifyDataSetInvalidated();
            }
        });

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

    public void addItem(Calendar time, String content, int color, String flag) {
        TodayModels item = new TodayModels();

        item.setTime(time);
        item.setCont(content);
        item.setColor(color);
        item.setFlag(flag);
        itemList.add(item);

        notifyDataSetChanged();
    }
}
