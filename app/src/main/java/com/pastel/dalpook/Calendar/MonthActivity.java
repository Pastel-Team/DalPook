package com.pastel.dalpook.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.CalendarDialog;
import com.pastel.dalpook.Utils.LoadingDialog;
import com.pastel.dalpook.data.Event;

import org.hugoandrade.calendarviewlib.CalendarView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class MonthActivity extends AppCompatActivity {

    private final static int CREATE_EVENT_REQUEST_CODE = 100;

    private View vCreateEventInnerContainer;
    private View vCreateEventOuterContainer;
    private CalendarDialog mCalendarDialog;

    private List<Event> mEventList = new ArrayList<>();

    private String[] mShortMonths;
    private CalendarView mCalendarView;

    private TextView txt_month_year;
    private TextView txt_month_month;

    private RelativeLayout btn_month_back;
    private Button btn_month_monback;
    private Button btn_month_monnext;
    private Button btn_month_add;

    private DBHelper dbHelper;

    private Event DelTargetEvent = null;
    private LoadingDialog loadingDialog;

    AppCompatDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.setLoadImage(R.drawable.ic_loading);

        dbHelper = new DBHelper(this);

        txt_month_month = (TextView) findViewById(R.id.txt_month_month);
        txt_month_year = (TextView) findViewById(R.id.txt_month_year);
        btn_month_monback = (Button) findViewById(R.id.btn_month_monback);
        btn_month_monnext= (Button) findViewById(R.id.btn_month_monnext);
        btn_month_back = (RelativeLayout) findViewById(R.id.btn_month_back);
        btn_month_add = (Button) findViewById(R.id.btn_month_add);

        mShortMonths = new DateFormatSymbols().getShortMonths();

        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnMonthChangedListener(new CalendarView.OnMonthChangedListener() {
            @Override
            public void onMonthChanged(int month, int year) {
                txt_month_year.setText(Integer.toString(year));

                if(mShortMonths[month].equals("Jan") || mShortMonths[month].equals("1월")){
                    txt_month_month.setText("1월");
                }else if(mShortMonths[month].equals("Feb") || mShortMonths[month].equals("2월")) {
                    txt_month_month.setText("2월");
                }else if(mShortMonths[month].equals("Mar") || mShortMonths[month].equals("3월")){
                    txt_month_month.setText("3월");
                }else if(mShortMonths[month].equals("Apr") || mShortMonths[month].equals("4월")){
                    txt_month_month.setText("4월");
                }else if(mShortMonths[month].equals("May") || mShortMonths[month].equals("5월")){
                    txt_month_month.setText("5월");
                }else if(mShortMonths[month].equals("Jun") || mShortMonths[month].equals("6월")){
                    txt_month_month.setText("6월");
                }else if(mShortMonths[month].equals("Jul") || mShortMonths[month].equals("7월")){
                    txt_month_month.setText("7월");
                }else if(mShortMonths[month].equals("Aug") || mShortMonths[month].equals("8월")){
                    txt_month_month.setText("8월");
                }else if(mShortMonths[month].equals("Sep") || mShortMonths[month].equals("9월")){
                    txt_month_month.setText("9월");
                }else if(mShortMonths[month].equals("Oct") || mShortMonths[month].equals("10월")){
                    txt_month_month.setText("10월");
                }else if(mShortMonths[month].equals("Nov") || mShortMonths[month].equals("11월")){
                    txt_month_month.setText("11월");
                }else if(mShortMonths[month].equals("Dec") || mShortMonths[month].equals("12월")){
                    txt_month_month.setText("12월");
                }

            }
        });

        int month = mCalendarView.getCurrentDate().get(Calendar.MONTH);
        int year = mCalendarView.getCurrentDate().get(Calendar.YEAR);
        //getSupportActionBar().setTitle(mShortMonths[month]);
        //getSupportActionBar().setSubtitle(Integer.toString(year));
        txt_month_year.setText(Integer.toString(year));
        if(mShortMonths[month].equals("Jan") || mShortMonths[month].equals("1월")){
            txt_month_month.setText("1월");
        }else if(mShortMonths[month].equals("Feb") || mShortMonths[month].equals("2월")) {
            txt_month_month.setText("2월");
        }else if(mShortMonths[month].equals("Mar") || mShortMonths[month].equals("3월")){
            txt_month_month.setText("3월");
        }else if(mShortMonths[month].equals("Apr") || mShortMonths[month].equals("4월")){
            txt_month_month.setText("4월");
        }else if(mShortMonths[month].equals("May") || mShortMonths[month].equals("5월")){
            txt_month_month.setText("5월");
        }else if(mShortMonths[month].equals("Jun") || mShortMonths[month].equals("6월")){
            txt_month_month.setText("6월");
        }else if(mShortMonths[month].equals("Jul") || mShortMonths[month].equals("7월")){
            txt_month_month.setText("7월");
        }else if(mShortMonths[month].equals("Aug") || mShortMonths[month].equals("8월")){
            txt_month_month.setText("8월");
        }else if(mShortMonths[month].equals("Sep") || mShortMonths[month].equals("9월")){
            txt_month_month.setText("9월");
        }else if(mShortMonths[month].equals("Oct") || mShortMonths[month].equals("10월")){
            txt_month_month.setText("10월");
        }else if(mShortMonths[month].equals("Nov") || mShortMonths[month].equals("11월")){
            txt_month_month.setText("11월");
        }else if(mShortMonths[month].equals("Dec") || mShortMonths[month].equals("12월")){
            txt_month_month.setText("12월");
        }
        mCalendarView.setOnItemClickedListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClicked(List<CalendarView.CalendarObject> calendarObjects,
                                      Calendar previousDate,
                                      Calendar selectedDate) {
                if (calendarObjects.size() != 0) {
                    mCalendarDialog.setSelectedDate(selectedDate);
                    mCalendarDialog.show();
                }
                else {
                    if (diffYMD(previousDate, selectedDate) == 0)
                        createEvent(selectedDate);
                }
            }
        });

        mCalendarDialog = CalendarDialog.Builder.instance(this)
                .setEventList(mEventList)
                .setOnItemClickListener(new CalendarDialog.OnCalendarDialogListener() {
                    @Override
                    public void onEventClick(Event event) {
                        onEventSelected(event);
                    }

                    @Override
                    public void onCreateEvent(Calendar calendar) {
                        createEvent(calendar);
                    }
                })
                .setOnDeleteClick(new CalendarDialog.OnCalendarDialogDeleteListener() {
                    @Override
                    public void onDelete(View view, Event targetEvent) {

                        loadingDialog.show();

                        Event oldEvent = null;
                        for (Event e : mEventList) {
                            if (Objects.equals(targetEvent.getID(), e.getID())) {
                                oldEvent = e;
                                break;
                            }
                        }
                        if (oldEvent != null) {
                            mEventList.remove(oldEvent);
                            mCalendarView.removeCalendarObjectByID(parseCalendarObject(oldEvent));

                            mCalendarDialog.setEventList(mEventList);
                        }

                        // DB Delete
                        Calendar deleteCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

                        DBHelper dbHelper = new DBHelper(getApplicationContext());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                        String oldSec = String.valueOf(oldEvent.getDate().getTime().getSeconds());

                        deleteCal = oldEvent.getDate();
                        String date = dateFormat.format(deleteCal.getTime());
                        String time = timeFormat.format(deleteCal.getTime()) + ":"+oldSec;
                        dbHelper.deleteConts(date, time,"M");

                        loadingDialog.hide();
                    }
                })
                .create();

        btn_month_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(mCalendarView.getSelectedDate());
            }
        });

        btn_month_monback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendarView.getmViewPager().setCurrentItem(mCalendarView.getmViewPager().getCurrentItem() - 1, true);
            }
        });
        btn_month_monnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendarView.getmViewPager().setCurrentItem(mCalendarView.getmViewPager().getCurrentItem() + 1, true);
            }
        });

        btn_month_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getDB();

    }


    private void onEventSelected(Event event) {
        Activity context = MonthActivity.this;
        Intent intent = CreateEventActivity.makeIntent(context, event);

        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }

    private void createEvent(Calendar selectedDate) {
        Activity context = MonthActivity.this;
        Intent intent = CreateEventActivity.makeIntent(context, selectedDate);

        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_calendar_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today: {
                mCalendarView.setSelectedDate(Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int action = CreateEventActivity.extractActionFromIntent(data);
                Event event = CreateEventActivity.extractEventFromIntent(data);

                switch (action) {
                    case CreateEventActivity.ACTION_CREATE: { // insert

                        loadingDialog.show();

                        mEventList.add(event);
                        mCalendarView.addCalendarObject(parseCalendarObject(event));
                        mCalendarDialog.setEventList(mEventList);

                        // DB Insert
                        Calendar insertCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

                        DBHelper dbHelper = new DBHelper(this);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                        Calendar secCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                        String secStr = String.valueOf(secCal.get(Calendar.SECOND));

                        insertCal = event.getDate();
                        String date = dateFormat.format(insertCal.getTime());
                        String time = timeFormat.format(insertCal.getTime()) + ":"+secStr;
                        String title = event.getTitle();
                        int color = event.getColor();
                        dbHelper.insertConts(date, time, title, "M", String.valueOf(color));

                        loadingDialog.hide();

                        break;
                    }
                    case CreateEventActivity.ACTION_EDIT: {

                        loadingDialog.show();

                        Event oldEvent = null;
                        for (Event e : mEventList) {
                            if (Objects.equals(event.getID(), e.getID())) {
                                oldEvent = e;
                                break;
                            }
                        }
                        if (oldEvent != null) {

                            mEventList.remove(oldEvent);
                            mEventList.add(event);

                            // DB Update
                            Calendar oldCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                            Calendar newCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                            DBHelper dbHelper = new DBHelper(this);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                            String oldSec = String.valueOf(event.getDate().getTime().getSeconds());

                            oldCal = oldEvent.getDate();
                            newCal = event.getDate();

                            String oldDate = dateFormat.format(oldCal.getTime());
                            String oldTime = timeFormat.format(oldCal.getTime()) + ":"+oldSec;

                            String newDate = dateFormat.format(newCal.getTime());
                            String newTime = timeFormat.format(newCal.getTime()) + ":"+oldSec;
                            String newTitle = event.getTitle();
                            int newColor = event.getColor();

                            dbHelper.updateConts(oldDate, oldTime, newDate, newTime, newTitle, newColor, "M");

                            mCalendarDialog.setEventList(mEventList);
                            mCalendarView.removeCalendarObjectByID(parseCalendarObject(oldEvent));
                            mCalendarView.addCalendarObject(parseCalendarObject(event));
                        }

                        loadingDialog.hide();
                        break;
                    }
                    case CreateEventActivity.ACTION_DELETE: {

                        Event oldEvent = null;
                        for (Event e : mEventList) {
                            if (Objects.equals(event.getID(), e.getID())) {
                                oldEvent = e;
                                break;
                            }
                        }
                        if (oldEvent != null) {
                            mEventList.remove(oldEvent);
                            mCalendarView.removeCalendarObjectByID(parseCalendarObject(oldEvent));

                            mCalendarDialog.setEventList(mEventList);
                        }
                        break;
                    }
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static int diffYMD(Calendar date1, Calendar date2) {
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH))
            return 0;

        return date1.before(date2) ? -1 : 1;
    }

    private static CalendarView.CalendarObject parseCalendarObject(Event event) {
        return new CalendarView.CalendarObject(
                event.getID(),
                event.getDate(),
                event.getColor(),
                event.isCompleted() ? Color.TRANSPARENT : Color.parseColor("#2e3145"),
                event.getTitle());
    }

    private void getDB(){

        loadingDialog.show();

        dbHelper = new DBHelper(this);
        Cursor contCursor = dbHelper.getConts("M");

        //INF 정보
        if (contCursor.getCount() > 0) {
            if(contCursor.moveToFirst()){
                do{
                    String date = contCursor.getString(contCursor.getColumnIndex("date"));
                    String time = contCursor.getString(contCursor.getColumnIndex("time"));
                    String[] splDate = date.split("-");
                    String[] splTime = time.split(":");

                    String year = splDate[0];
                    String month = splDate[1];
                    String day = splDate[2];

                    String hour = splTime[0];
                    String minute = splTime[1];
                    String second = splTime[2];

                    Calendar setCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                    setCal.set(Calendar.YEAR, Integer.parseInt(year));
                    setCal.set(Calendar.MONTH, Integer.parseInt(month)-1);
                    setCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                    setCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    setCal.set(Calendar.MINUTE, Integer.parseInt(minute));
                    setCal.set(Calendar.SECOND, Integer.parseInt(second));

                    Event event = new Event(
                            Long.toString(System.currentTimeMillis()),
                            contCursor.getString(contCursor.getColumnIndex("cont")),
                            setCal,
                            Integer.parseInt(contCursor.getString(contCursor.getColumnIndex("color"))),
                            false//mIsCompleteCheckBox.isChecked()
                    );

                    mEventList.add(event);
                    mCalendarView.addCalendarObject(parseCalendarObject(event));
                }while (contCursor.moveToNext());

                mCalendarDialog.setEventList(mEventList);
            }
        }

        loadingDialog.hide();
    }


}