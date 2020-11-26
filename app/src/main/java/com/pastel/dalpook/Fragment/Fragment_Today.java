package com.pastel.dalpook.Fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.LoadingDialog;
import com.pastel.dalpook.Utils.TodayListAdapter;
import com.pastel.dalpook.data.TodayModels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Fragment_Today extends Fragment {

    private DBHelper dbHelper;
    private TodayModels models;
    private TodayListAdapter listAdapter;

    private TextView txt_year;
    private TextView txt_month;
    private TextView txt_day;
    private ListView listView;

    private LoadingDialog loadingDialog;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LayoutInflater lf = requireActivity().getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_main, container, false); //pass the correct layout name for the fragment
        init(rootView);

        // 현재날짜 구하기
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        txt_year.setText(yearFormat.format(currentTime));
        txt_month.setText(monthFormat.format(currentTime));
        txt_day.setText(dayFormat.format(currentTime));

        return rootView;
    }

    private void init(View view){
        txt_year = (TextView)view.findViewById(R.id.txt_year);
        txt_month = (TextView)view.findViewById(R.id.txt_month);
        txt_day = (TextView)view.findViewById(R.id.txt_day);
        listView = (ListView)view.findViewById(R.id.lv_main);
    }

    private Cursor getDB(){
        models = new TodayModels();
        dbHelper = new DBHelper(getContext());

        Calendar TodayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String date = dateFormat.format(TodayCal.getTime());
        int week = TodayCal.get(Calendar.DAY_OF_WEEK);

        return  dbHelper.getToday(date, String.valueOf(week));
    }

    @Override
    public void onResume(){
        super.onResume();

        listAdapter = new TodayListAdapter(getContext());
        listView.setAdapter(listAdapter);

        Cursor cursor = getDB();

        //오늘의 일정 데이터
        if (cursor.getCount() > 0) {
            if(cursor.moveToFirst()){
                do{
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

                    listAdapter.addItem(setCal, cont, color, flag);

                }while (cursor.moveToNext());
            }
        }

    }

    @Override
    public void onPause(){
        super.onPause();
    }

}
