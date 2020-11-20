package com.pastel.dalpook.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Popup.CreateDiaryActivity;
import com.pastel.dalpook.Popup.CreateEventActivity;
import com.pastel.dalpook.Popup.DiaryDialogActivity;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.DiaryListAdapter;
import com.pastel.dalpook.Utils.TodayListAdapter;
import com.pastel.dalpook.data.DiaryModels;
import com.pastel.dalpook.data.Event;
import com.pastel.dalpook.data.TodayModels;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DiaryActivity extends AppCompatActivity {

    private ListView listView;
    private DiaryListAdapter listAdapter;
    private final int CREATE_EVENT_REQUEST_CODE = 100;
    private final int VIEW_EVENT_REQUEST_CODE = 200;

    private List<DiaryModels> eventLists = new ArrayList<DiaryModels>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        init();
        setList();
    }

    private void init(){

        listView = (ListView)findViewById(R.id.lv_diary);

        Button btn_back = (Button)findViewById(R.id.btn_diary_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btn_add = (Button)findViewById(R.id.btn_diary_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryActivity.this, CreateDiaryActivity.class);
                DiaryModels event = null;
                intent.putExtra("event", event);
                startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);

                overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
            }
        });
    }

    private Cursor getDB(){
        DBHelper dbHelper = new DBHelper(this);
        return  dbHelper.getDiary();
    }

    private void setList(){
        listAdapter = new DiaryListAdapter(this, new DiaryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, DiaryModels event) {
                Intent intent = new Intent(DiaryActivity.this, DiaryDialogActivity.class);
                intent.putParcelableArrayListExtra("eventList", (ArrayList<? extends Parcelable>) eventLists);
                intent.putExtra("event", event);
                startActivityForResult(intent, VIEW_EVENT_REQUEST_CODE);
            }
        });

        listView.setAdapter(listAdapter);

        Cursor cursor = getDB();

        //오늘의 일정 데이터
        if (cursor.getCount() > 0) {
            if(cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String desc = cursor.getString(cursor.getColumnIndex("cont"));
                    String uri = cursor.getString(cursor.getColumnIndex("pic"));
                    String[] splDate = date.split("-");

                    String year = splDate[0];
                    String month = splDate[1];
                    String day = splDate[2];

                    Calendar setCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                    setCal.set(Calendar.YEAR, Integer.parseInt(year));
                    setCal.set(Calendar.MONTH, Integer.parseInt(month)-1);
                    setCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

                    listAdapter.addItem(setCal, title, desc, uri);

                    DiaryModels event = new DiaryModels(setCal, title, desc, uri);
                    eventLists.add(event);

                }while (cursor.moveToNext());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int action = CreateDiaryActivity.extractActionFromIntent(data);
                DiaryModels event = CreateDiaryActivity.extractEventFromIntent(data);

                switch (action) {
                    case CreateDiaryActivity.ACTION_CREATE: { // insert

                        eventLists.add(event);

                        listView.setAdapter(null);
                        listAdapter= null;
                        eventLists.clear();
                        listView.setAdapter(listAdapter);
                        setList();

                        /*
                        // DB Insert
                        DBHelper dbHelper = new DBHelper(this);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                        Calendar calendar = Calendar.getInstance();
                        calendar = event.getmDate();

                        String date = dateFormat.format(calendar.getTime());
                        String title = event.getmTitle();
                        String desc = event.getmDesc();
                        String path = event.getmImg();

                        String msg = "";
                        msg = dbHelper.insertDiary(date, title, desc, path);

                        if(!msg.equals("")){
                            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                        }
                         */

                        break;
                    }
                    case CreateDiaryActivity.ACTION_EDIT: {

                        DiaryModels oldEvent = null;
                        for (DiaryModels e : eventLists) {
                            if (Objects.equals(event.getmDate(), e.getmDate())) {
                                oldEvent = e;
                                break;
                            }
                        }
                        if (oldEvent != null) {

                            listView.setAdapter(null);
                            listAdapter= null;
                            eventLists.clear();
                            listView.setAdapter(listAdapter);
                            setList();

                            /*
                            // DB Update
                            DBHelper dbHelper = new DBHelper(this);
                            Calendar oldCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                            Calendar newCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

                            oldCal = oldEvent.getmDate();
                            newCal = event.getmDate();

                            String oldDate = dateFormat.format(oldCal.getTime());
                            String newDate = dateFormat.format(newCal.getTime());
                            String newTitle = event.getmTitle();
                            String newDesc = event.getmDesc();
                            String newPath = event.getmImg();

                            String msg = "";
                            msg = dbHelper.updateDiary(oldDate, newDate, newTitle, newDesc, newPath);
                            if(!msg.equals("")){
                                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                            }

                             */
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
    public void onResume(){
        super.onResume();

        listView.setAdapter(null);
        listAdapter= null;
        eventLists.clear();
        listView.setAdapter(listAdapter);
        setList();
    }
}