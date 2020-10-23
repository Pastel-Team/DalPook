package com.pastel.dalpook.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import com.applandeo.materialcalendarview.CalendarView;
import com.pastel.dalpook.R;

public class CalMonthActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_month);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setHeaderColor(Color.parseColor("#2e3145"));
        calendarView.setHeaderLabelColor(Color.WHITE);
        //calendarView.setForwardButtonImage([drawable]);
        //calendarView.setPreviousButtonImage([drawable]);

    }

}