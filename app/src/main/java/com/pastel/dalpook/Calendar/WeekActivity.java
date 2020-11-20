package com.pastel.dalpook.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Popup.CreateEventActivity;
import com.pastel.dalpook.Popup.CreateWeekEventActivity;
import com.pastel.dalpook.R;
import com.pastel.dalpook.data.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class WeekActivity extends AppCompatActivity{

    private int[] SunID = {R.id.txtSu00, R.id.txtSu01, R.id.txtSu02, R.id.txtSu03, R.id.txtSu04, R.id.txtSu05, R.id.txtSu06, R.id.txtSu07, R.id.txtSu08, R.id.txtSu09, R.id.txtSu10, R.id.txtSu11,
            R.id.txtSu12, R.id.txtSu13, R.id.txtSu14, R.id.txtSu15, R.id.txtSu16, R.id.txtSu17, R.id.txtSu18, R.id.txtSu19, R.id.txtSu20, R.id.txtSu21, R.id.txtSu22, R.id.txtSu23};
    private int[] MonID = {R.id.txtMo00, R.id.txtMo01, R.id.txtMo02, R.id.txtMo03, R.id.txtMo04, R.id.txtMo05, R.id.txtMo06, R.id.txtMo07, R.id.txtMo08, R.id.txtMo09, R.id.txtMo10, R.id.txtMo11,
            R.id.txtMo12, R.id.txtMo13, R.id.txtMo14, R.id.txtMo15, R.id.txtMo16, R.id.txtMo17, R.id.txtMo18, R.id.txtMo19, R.id.txtMo20, R.id.txtMo21, R.id.txtMo22, R.id.txtMo23};
    private int[] TueID = {R.id.txtTu00, R.id.txtTu01, R.id.txtTu02, R.id.txtTu03, R.id.txtTu04, R.id.txtTu05, R.id.txtTu06, R.id.txtTu07, R.id.txtTu08, R.id.txtTu09, R.id.txtTu10, R.id.txtTu11,
            R.id.txtTu12, R.id.txtTu13, R.id.txtTu14, R.id.txtTu15, R.id.txtTu16, R.id.txtTu17, R.id.txtTu18, R.id.txtTu19, R.id.txtTu20, R.id.txtTu21, R.id.txtTu22, R.id.txtTu23};
    private int[] WenID = {R.id.txtWe00, R.id.txtWe01, R.id.txtWe02, R.id.txtWe03, R.id.txtWe04, R.id.txtWe05, R.id.txtWe06, R.id.txtWe07, R.id.txtWe08, R.id.txtWe09, R.id.txtWe10, R.id.txtWe11,
            R.id.txtWe12, R.id.txtWe13, R.id.txtWe14, R.id.txtWe15, R.id.txtWe16, R.id.txtWe17, R.id.txtWe18, R.id.txtWe19, R.id.txtWe20, R.id.txtWe21, R.id.txtWe22, R.id.txtWe23};
    private int[] ThuID = {R.id.txtTh00, R.id.txtTh01, R.id.txtTh02, R.id.txtTh03, R.id.txtTh04, R.id.txtTh05, R.id.txtTh06, R.id.txtTh07, R.id.txtTh08, R.id.txtTh09, R.id.txtTh10, R.id.txtTh11,
            R.id.txtTh12, R.id.txtTh13, R.id.txtTh14, R.id.txtTh15, R.id.txtTh16, R.id.txtTh17, R.id.txtTh18, R.id.txtTh19, R.id.txtTh20, R.id.txtTh21, R.id.txtTh22, R.id.txtTh23};
    private int[] FriID = {R.id.txtFr00, R.id.txtFr01, R.id.txtFr02, R.id.txtFr03, R.id.txtFr04, R.id.txtFr05, R.id.txtFr06, R.id.txtFr07, R.id.txtFr08, R.id.txtFr09, R.id.txtFr10, R.id.txtFr11,
            R.id.txtFr12, R.id.txtFr13, R.id.txtFr14, R.id.txtFr15, R.id.txtFr16, R.id.txtFr17, R.id.txtFr18, R.id.txtFr19, R.id.txtFr20, R.id.txtFr21, R.id.txtFr22, R.id.txtFr23};
    private int[] SatID = {R.id.txtSa00, R.id.txtSa01, R.id.txtSa02, R.id.txtSa03, R.id.txtSa04, R.id.txtSa05, R.id.txtSa06, R.id.txtSa07, R.id.txtSa08, R.id.txtSa09, R.id.txtSa10, R.id.txtSa11,
            R.id.txtSa12, R.id.txtSa13, R.id.txtSa14, R.id.txtSa15, R.id.txtSa16, R.id.txtSa17, R.id.txtSa18, R.id.txtSa19, R.id.txtSa20, R.id.txtSa21, R.id.txtSa22, R.id.txtSa23};

    private TextView cell[][] = new TextView[24][7];

    private final static int CREATE_EVENT_REQUEST_CODE = 100;

    private final List<Event> eventList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        init();
        getDB();
    }

    private void init(){

        int cnt = 0;
        // 일
        for(int i = 0 ; i < SunID.length ; i++){
            cell[i][0] = (TextView)findViewById(SunID[i]);
            cell[i][0].setTag(String.valueOf(i));
            cell[i][0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 1); //일요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
            cnt++;
        }
        // 월
        cnt = 0;
        for(int i = 0 ; i < MonID.length ; i++){
            int finalCnt = cnt;
            cell[i][1] = (TextView)findViewById(MonID[i]);
            cell[i][1].setTag(String.valueOf(i));
            cell[i][1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 2); //월요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
        }
        // 화
        cnt = 0;
        for(int i = 0 ; i < TueID.length ; i++){
            int finalCnt = cnt;
            cell[i][2] = (TextView)findViewById(TueID[i]);
            cell[i][2].setTag(String.valueOf(i));
            cell[i][2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 3); //화요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
        }
        // 수
        cnt = 0;
        for(int i = 0 ; i < WenID.length ; i++){
            int finalCnt = cnt;
            cell[i][3] = (TextView)findViewById(WenID[i]);
            cell[i][3].setTag(String.valueOf(i));
            cell[i][3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 4); //수요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
        }
        // 목
        cnt = 0;
        for(int i = 0 ; i < ThuID.length ; i++){
            int finalCnt = cnt;
            cell[i][4] = (TextView)findViewById(ThuID[i]);
            cell[i][4].setTag(String.valueOf(i));
            cell[i][4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 5); //목요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
        }
        // 금
        cnt = 0;
        for(int i = 0 ; i < FriID.length ; i++){
            int finalCnt = cnt;
            cell[i][5] = (TextView)findViewById(FriID[i]);
            cell[i][5].setTag(String.valueOf(i));
            cell[i][5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 6); //금요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
        }
        // 토
        cnt = 0;
        for(int i = 0 ; i < SatID.length ; i++){
            int finalCnt = cnt;
            cell[i][6] = (TextView)findViewById(SatID[i]);
            cell[i][6].setTag(String.valueOf(i));
            cell[i][6].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isItem = false;
                    if(eventList.size() > 0){
                        for(int i = 0 ; i < eventList.size() ; i ++){
                            if( (eventList.get(i).getDate().get(Calendar.HOUR_OF_DAY) == Integer.parseInt(String.valueOf(view.getTag()))) &&
                                    (eventList).get(i).getDate().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                                onEventSelected(eventList.get(i));
                                isItem = true;
                            }
                        }
                    }
                    if(!isItem){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, 7); //토요일
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(view.getTag())));
                        createEvent(calendar);
                    }
                }
            });
        }

        Button btn_back = (Button)findViewById(R.id.btn_week_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void createEvent(Calendar selectedDate) {
        Activity context = WeekActivity.this;
        Intent intent = CreateWeekEventActivity.makeIntent(context, selectedDate);

        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }

    private void onEventSelected(Event event) {
        Activity context = WeekActivity.this;
        Intent intent = CreateWeekEventActivity.makeIntent(context, event);

        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }

    private void getDB(){
        DBHelper dbHelper = new DBHelper(this);
        Cursor contCursor = dbHelper.getConts("W");

        //INF 정보
        if (contCursor.getCount() > 0) {
            if(contCursor.moveToFirst()){
                do{
                    String date = contCursor.getString(contCursor.getColumnIndex("date"));
                    String time = contCursor.getString(contCursor.getColumnIndex("time"));
                    String[] splTime = time.split(":");

                    String hour = splTime[0];

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(date));

                    Event event = new Event(
                            Long.toString(System.currentTimeMillis()),
                            contCursor.getString(contCursor.getColumnIndex("cont")),
                            calendar,
                            Integer.parseInt(contCursor.getString(contCursor.getColumnIndex("color"))),
                            false//mIsCompleteCheckBox.isChecked()
                    );
                    //calendar = event.getDate();
                    int week = calendar.get(Calendar.DAY_OF_WEEK);
                    final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.tablecorner_color);
                    drawable.setColor(event.getColor());
                    cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setBackground(drawable);
                    cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setText(event.getTitle());

                    eventList.add(event);

                }while (contCursor.moveToNext());

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int action = CreateWeekEventActivity.extractActionFromIntent(data);
                Event event = CreateWeekEventActivity.extractEventFromIntent(data);

                switch (action) {
                    case CreateWeekEventActivity.ACTION_CREATE: { // insert

                        eventList.add(event);

                        Calendar calendar = Calendar.getInstance();
                        calendar = event.getDate();
                        int week = calendar.get(Calendar.DAY_OF_WEEK);

                        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.tablecorner_color);
                        drawable.setColor(event.getColor());
                        cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setBackground(drawable);
                        cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setText(event.getTitle());

                        // DB Insert

                        DBHelper dbHelper = new DBHelper(this);
                        String date = String.valueOf(week);
                        String time = "";
                        if(calendar.get(Calendar.HOUR_OF_DAY) < 10 ){
                            time = "0"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":00:00";
                        }else{
                            time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":00:00";
                        }
                        String title = event.getTitle();
                        int color = event.getColor();
                        dbHelper.insertConts(date, time, title, "W", String.valueOf(color));

                        break;
                    }
                    case CreateWeekEventActivity.ACTION_EDIT: {

                        Event oldEvent = null;
                        for (Event e : eventList) {
                            if (Objects.equals(event.getDate(), e.getDate())) {
                                oldEvent = e;
                                break;
                            }
                        }
                        if (oldEvent != null) {

                            eventList.remove(oldEvent);
                            eventList.add(event);

                            Calendar calendar = Calendar.getInstance();
                            calendar = event.getDate();
                            int week = calendar.get(Calendar.DAY_OF_WEEK);

                            final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.tablecorner_color);
                            drawable.setColor(event.getColor());
                            cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setBackground(drawable);
                            cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setText(event.getTitle());

                            // DB Update
                            DBHelper dbHelper = new DBHelper(this);

                            String date = String.valueOf(week);
                            String time = "";
                            if(calendar.get(Calendar.HOUR_OF_DAY) < 10 ){
                                time = "0"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":00:00";
                            }else{
                                time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":00:00";
                            }
                            String title = event.getTitle();
                            int color = event.getColor();
                            dbHelper.updateWeek(date, time, title, color, "W");
                        }

                        break;
                    }
                    case CreateWeekEventActivity.ACTION_DELETE :{

                        Event oldEvent = null;
                        for (Event e : eventList) {
                            if (Objects.equals(event.getDate(), e.getDate())) {
                                oldEvent = e;
                                break;
                            }
                        }
                        if (oldEvent != null) {
                            eventList.remove(oldEvent);

                            Calendar calendar = Calendar.getInstance();
                            calendar = oldEvent.getDate();
                            int week = calendar.get(Calendar.DAY_OF_WEEK);
                            if((week-1)%2 == 0){
                                cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setBackgroundResource(R.drawable.tablecorner_fill);
                            }else{
                                cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setBackgroundResource(R.drawable.tablecorner);
                            }
                            cell[calendar.get(Calendar.HOUR_OF_DAY)][week-1].setText("");


                            // DB delete
                            DBHelper dbHelper = new DBHelper(this);

                            String date = String.valueOf(week);
                            String time = "";
                            if(calendar.get(Calendar.HOUR_OF_DAY) < 10 ){
                                time = "0"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":00:00";
                            }else{
                                time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":00:00";
                            }
                            dbHelper.deleteConts(date, time, "W");
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