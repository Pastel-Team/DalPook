package com.pastel.dalpook.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.CalendarDialog;
import com.pastel.dalpook.data.Event;

import org.hugoandrade.calendarviewlib.CalendarView;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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

    private Button btn_month_back;
    private Button btn_month_monback;
    private Button btn_month_monnext;
    private Button btn_month_add;

    public static Intent makeIntent(Context context) {
        return new Intent(context, MonthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        txt_month_month = (TextView) findViewById(R.id.txt_month_month);
        txt_month_year = (TextView) findViewById(R.id.txt_month_year);
        btn_month_monback = (Button) findViewById(R.id.btn_month_monback);
        btn_month_monnext= (Button) findViewById(R.id.btn_month_monnext);
        btn_month_back = (Button) findViewById(R.id.btn_month_back);
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
                mCalendarView.setSelectedDate(Calendar.getInstance());
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
                    case CreateEventActivity.ACTION_CREATE: {
                        mEventList.add(event);
                        mCalendarView.addCalendarObject(parseCalendarObject(event));
                        mCalendarDialog.setEventList(mEventList);
                        break;
                    }
                    case CreateEventActivity.ACTION_EDIT: {
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

                            mCalendarView.removeCalendarObjectByID(parseCalendarObject(oldEvent));
                            mCalendarView.addCalendarObject(parseCalendarObject(event));
                            mCalendarDialog.setEventList(mEventList);
                        }
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
                event.isCompleted() ? Color.TRANSPARENT : Color.parseColor("#e8c792"));
    }
}