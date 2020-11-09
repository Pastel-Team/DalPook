package com.pastel.dalpook.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Popup.CreateEventActivity;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.CalListAdapter;
import com.pastel.dalpook.Utils.CalendarDialog;
import com.pastel.dalpook.Utils.ExpandAdapter;
import com.pastel.dalpook.data.Event;
import com.pastel.dalpook.data.TodayModels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class MonthListActivity extends AppCompatActivity {

    private final static int CREATE_EVENT_REQUEST_CODE = 100;

    private ArrayList<String> mParentList = new ArrayList<>();
    private ArrayList<Event> mEventList = new ArrayList<>(); // child
    private HashMap<String, ArrayList<Event>> childList = new HashMap<String, ArrayList<Event>>();

    private Button btn_month_list_add;
    private Button btn_month_list_back;
    private ExpandableListView expandableListView;
    private ExpandAdapter mAdapter;

    private DBHelper dbHelper;
    private TodayModels models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_list);

        dbHelper = new DBHelper(this);

        btn_month_list_back = (Button) findViewById(R.id.btn_month_list_back);
        btn_month_list_add = (Button) findViewById(R.id.btn_month_list_add);
        expandableListView = (ExpandableListView) findViewById(R.id.el_month_list);

        getDB();
        setExpandList();

        for(int i = 0 ; i < mAdapter.getGroupCount() ; i++){
            expandableListView.expandGroup(i);
        }

        btn_month_list_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(null);
            }
        });

        btn_month_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        /*
        btn_month_list_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(mCalendarView.getSelectedDate());
            }
        });

         */

        /*
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });

         */

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });
    }

    private void getDB(){
        dbHelper = new DBHelper(this);
        Cursor contCursor = dbHelper.getMonthList();

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

                    // 부모 리스트
                    String groupStr = year+". "+month;
                    Boolean isGroup = false;
                    if(mParentList.size() > 0){
                        for( int i = 0 ; i < mParentList.size() ; i++){
                            if(mParentList.get(i).toString().equals(groupStr)){
                                isGroup = true;
                                break;
                            }
                        }
                        if(!isGroup){
                            mParentList.add(groupStr);
                        }
                    }else{
                        mParentList.add(groupStr);
                    }
                    // 자식 리스트
                    mEventList.add(event);

                }while (contCursor.moveToNext());

                //mCalendarDialog.setEventList(mEventList);
            }
        }
    }

    private void setExpandList(){

        ArrayList<Event> events;
        if(mParentList.size() > 0){
            for(int i = 0 ; i < mParentList.size() ; i++){
                events = new ArrayList<>();
                String parentDate = mParentList.get(i).toString();

                for(int j = 0 ; j < mEventList.size() ; j++){
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                    calendar = mEventList.get(j).getDate();
                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                    int setMonth = Integer.parseInt(month);
                    if(setMonth < 10){
                        month = "0"+month;
                    }
                    String childDate = year+". "+month;
                    if(parentDate.equals(childDate)){
                        events.add(mEventList.get(j));
                    }
                }

                childList.put(mParentList.get(i), events);

            }

            mAdapter = new ExpandAdapter(this, mParentList, childList, new ExpandAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int pos, Event event) {
                    // 클릭이벤트
                }
            });

            expandableListView.setAdapter(mAdapter);
        }
    }

    private void createEvent(Calendar selectedDate) {
        Activity context = MonthListActivity.this;
        Intent intent = CreateEventActivity.makeIntent(context, selectedDate);

        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int action = CreateEventActivity.extractActionFromIntent(data);
                Event event = CreateEventActivity.extractEventFromIntent(data);

                switch (action) {
                    case CreateEventActivity.ACTION_CREATE: { // insert

                        //mCalendarView.addCalendarObject(parseCalendarObject(event));
                        //mCalendarDialog.setEventList(mEventList);

                        // DB Insert
                        Calendar insertCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

                        DBHelper dbHelper = new DBHelper(this);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                        Calendar secCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                        //String secStr = String.valueOf(secCal.get(Calendar.SECOND));

                        insertCal = event.getDate();
                        String insertSec = "";
                        if(insertCal.get(Calendar.SECOND) < 10){
                            insertSec = "0" + insertCal.get(Calendar.SECOND);
                        }else{
                            insertSec = String.valueOf(insertCal.get(Calendar.SECOND));
                        }

                        String date = dateFormat.format(insertCal.getTime());
                        String time = timeFormat.format(insertCal.getTime()) + ":"+insertSec;
                        String title = event.getTitle();
                        int color = event.getColor();
                        dbHelper.insertConts(date, time, title, "M", String.valueOf(color));

                        mParentList.clear();
                        mEventList.clear();
                        childList.clear();
                        getDB();
                        setExpandList();


                        for(int i = 0 ; i < mAdapter.getGroupCount() ; i ++){
                            expandableListView.expandGroup(i);
                        }

                        break;
                    }
                    case CreateEventActivity.ACTION_EDIT: {
                        Event oldEvent = null;
                        for (Event e : mEventList) {
                            if (Objects.equals(event.getDate(), e.getDate())) {
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

                            oldCal = oldEvent.getDate();
                            newCal = event.getDate();

                            String oldSec = "";
                            if(oldCal.get(Calendar.SECOND) < 10){
                                oldSec = "0" + oldCal.get(Calendar.SECOND);
                            }else{
                                oldSec = String.valueOf(oldCal.get(Calendar.SECOND));
                            }

                            String oldDate = dateFormat.format(oldCal.getTime());
                            String oldTime = timeFormat.format(oldCal.getTime()) + ":"+oldSec;

                            String newDate = dateFormat.format(newCal.getTime());
                            String newTime = timeFormat.format(newCal.getTime()) + ":"+oldSec;
                            String newTitle = event.getTitle();
                            int newColor = event.getColor();

                            dbHelper.updateConts(oldDate, oldTime, newDate, newTime, newTitle, newColor, "M");

                            //mCalendarDialog.setEventList(mEventList);
                            //mCalendarView.removeCalendarObjectByID(parseCalendarObject(oldEvent));
                            //mCalendarView.addCalendarObject(parseCalendarObject(event));
                        }

                        break;
                    }
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}