package com.pastel.dalpook.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.R;
import com.pastel.dalpook.data.TodayModels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    //context 설정하기
    private Context context = null;
    private List<TodayModels> arrayList = new ArrayList<TodayModels>();

    public MyRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    private Cursor getDB() {
        DBHelper dbHelper = new DBHelper(context);

        Calendar TodayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String date = dateFormat.format(TodayCal.getTime());
        int week = TodayCal.get(Calendar.DAY_OF_WEEK);

        return dbHelper.getToday(date, String.valueOf(week));
    }

    // 데이터 추가
    public void setData() {

        arrayList.clear();
        Cursor cursor = getDB();

        //오늘의 일정 데이터
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String cont = cursor.getString(cursor.getColumnIndex("cont"));
                    int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex("color")));
                    String flag = cursor.getString(cursor.getColumnIndex("flag"));
                    String[] splTime = time.split(":");

                    String hour = splTime[0];
                    String minute = splTime[1];
                    String second = splTime[2];

                    Calendar setCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                    setCal.setTime(new Date());
                    setCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    setCal.set(Calendar.MINUTE, Integer.parseInt(minute));
                    setCal.set(Calendar.SECOND, Integer.parseInt(second));

                    TodayModels models = new TodayModels();
                    models.setTime(setCal);
                    models.setCont(cont);
                    models.setColor(color);
                    models.setFlag(flag);
                    arrayList.add(models);

                } while (cursor.moveToNext());
            }
        }
    }

    //이 모든게 필수 오버라이드 메소드

    //실행 최초로 호출되는 함수
    @Override
    public void onCreate() {
        //setData();
    }

    //항목 추가 및 제거 등 데이터 변경이 발생했을 때 호출되는 함수
    //브로드캐스트 리시버에서 notifyAppWidgetViewDataChanged()가 호출 될 때 자동 호출
    @Override
    public void onDataSetChanged() {
        setData();
    }

    //마지막에 호출되는 함수
    @Override
    public void onDestroy() {

    }

    // 항목 개수를 반환하는 함수
    @Override
    public int getCount() {
        return arrayList.size();
    }

    //각 항목을 구현하기 위해 호출, 매개변수 값을 참조하여 각 항목을 구성하기위한 로직이 담긴다.
    // 항목 선택 이벤트 발생 시 인텐트에 담겨야 할 항목 데이터를 추가해주어야 하는 함수
    @Override
    public RemoteViews getViewAt(int position) {
        /*
        RemoteViews listviewWidget = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        //listviewWidget.setTextViewText(R.id.text1, arrayList.get(position).content);

        //배경 표시유무
        RemoteViews mainWidget = new RemoteViews(context.getPackageName(), R.layout.today_widget);
        if(arrayList.size() > 0){
            mainWidget.setViewVisibility(R.id.main_empty, View.GONE);
            mainWidget.setViewVisibility(R.id.lv_widget, View.VISIBLE);
        }else{
            mainWidget.setViewVisibility(R.id.main_empty, View.VISIBLE);
            mainWidget.setViewVisibility(R.id.lv_widget, View.GONE);
        }

        // 시간
        String setTime = "";
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        calendar = arrayList.get(position).getTime();
        int isAMorPM = calendar.get(Calendar.AM_PM);
        int Hour = calendar.get(Calendar.HOUR);
        String AMHour = "";
        if (Hour >= 0 && Hour < 10) {
            AMHour = "0" + calendar.get(Calendar.HOUR);
            if (AMHour.equals("00")) {
                AMHour = "12";
            }
        } else {
            AMHour = String.valueOf(calendar.get(Calendar.HOUR));
        }

        int Minute = calendar.get(Calendar.MINUTE);
        String AMMinute = "";
        if (Minute >= 0 && Minute < 10) {
            AMMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            AMMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        switch (isAMorPM) {
            case Calendar.AM:
                setTime = "오전 " + AMHour + ":" + AMMinute;
                break;
            case Calendar.PM:
                setTime = "오후 " + AMHour + ":" + AMMinute;
                break;
        }

        listviewWidget.setTextViewText(R.id.txt_widget_time, setTime);
        listviewWidget.setTextViewText(R.id.txt_widget_cont, arrayList.get(position).getCont());
        //listviewWidget.setInt(R.id.color_widget, "setBackgroundColor", arrayList.get(position).getColor());

        // 항목 선택 이벤트 발생 시 인텐트에 담겨야 할 항목 데이터를 추가해주는 코드
        Intent dataIntent = new Intent();
        dataIntent.putExtra("time", arrayList.get(position).getTime());
        dataIntent.putExtra("cont", arrayList.get(position).getCont());
        dataIntent.putExtra("color", arrayList.get(position).getColor());
        dataIntent.putExtra("flag", arrayList.get(position).getFlag());
        listviewWidget.setOnClickFillInIntent(R.id.view_widget_item, dataIntent);
        //setOnClickFillInIntent 브로드캐스트 리시버에서 항목 선택 이벤트가 발생할 때 실행을 의뢰한 인텐트에 각 항목의 데이터를 추가해주는 함수
        //브로드캐스트 리시버의 인텐트와 Extra 데이터가 담긴 인텐트를 함치는 역할을 한다.

        return listviewWidget;
        */

        RemoteViews row = new RemoteViews( context.getPackageName(), R.layout.list_item_widget );

        //배경 표시유무
        RemoteViews mainWidget = new RemoteViews(context.getPackageName(), R.layout.today_widget);
        if(arrayList.size() > 0){
            mainWidget.setViewVisibility(R.id.main_empty, View.GONE);
            mainWidget.setViewVisibility(R.id.lv_widget, View.VISIBLE);
        }else{
            mainWidget.setViewVisibility(R.id.main_empty, View.VISIBLE);
            mainWidget.setViewVisibility(R.id.lv_widget, View.GONE);
        }

        // 시간
        String setTime = "";
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        calendar = arrayList.get(position).getTime();
        int isAMorPM = calendar.get(Calendar.AM_PM);
        int Hour = calendar.get(Calendar.HOUR);
        String AMHour = "";
        if (Hour >= 0 && Hour < 10) {
            AMHour = "0" + calendar.get(Calendar.HOUR);
            if (AMHour.equals("00")) {
                AMHour = "12";
            }
        } else {
            AMHour = String.valueOf(calendar.get(Calendar.HOUR));
        }

        int Minute = calendar.get(Calendar.MINUTE);
        String AMMinute = "";
        if (Minute >= 0 && Minute < 10) {
            AMMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            AMMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        switch (isAMorPM) {
            case Calendar.AM:
                setTime = "오전 " + AMHour + ":" + AMMinute;
                break;
            case Calendar.PM:
                setTime = "오후 " + AMHour + ":" + AMMinute;
                break;
        }

        row.setTextViewText(R.id.txt_widget_time, setTime);
        row.setTextViewText(R.id.txt_widget_cont, arrayList.get(position).getCont());
        row.setInt(R.id.color_widget, "setBackgroundColor", arrayList.get(position).getColor());


        Intent intent = new Intent();

        Bundle extras = new Bundle();
        //extras.putString(TodayWidget.EXTRA_WORD, arrayList.get(position));

        Intent dataIntent = new Intent();
        dataIntent.putExtra("time", arrayList.get(position).getTime());
        dataIntent.putExtra("cont", arrayList.get(position).getCont());
        dataIntent.putExtra("color", arrayList.get(position).getColor());
        dataIntent.putExtra("flag", arrayList.get(position).getFlag());
        row.setOnClickFillInIntent(R.id.view_widget_item, dataIntent);
        intent.putExtras(extras);

        row.setOnClickFillInIntent(android.R.id.text1, intent);

        return row;
    }

    //로딩 뷰를 표현하기 위해 호출, 없으면 null
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    //항목의 타입 갯수를 판단하기 위해 호출, 모든 항목이 같은 뷰 타입이라면 1을 반환하면 된다.
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //각 항목의 식별자 값을 얻기 위해 호출
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 같은 ID가 항상 같은 개체를 참조하면 true 반환하는 함수
    @Override
    public boolean hasStableIds() {
        return false;
    }
}