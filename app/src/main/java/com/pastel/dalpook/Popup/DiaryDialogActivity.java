package com.pastel.dalpook.Popup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.pastel.dalpook.Calendar.DiaryActivity;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.DiaryPagerAdapter;
import com.pastel.dalpook.Utils.LoadingDialog;
import com.pastel.dalpook.data.DiaryModels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DiaryDialogActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_EVENT = "intent_extra_event";
    private static final String INTENT_EXTRA_ACTION = "intent_extra_action";
    private final int CREATE_EVENT_REQUEST_CODE = 100;
    private List<DiaryModels> eventLists = new ArrayList<DiaryModels>();
    private List<DiaryModels> newEventLists = new ArrayList<DiaryModels>();
    private HorizontalInfiniteCycleViewPager pager;
    private DiaryPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_dialog);

        initData();

        pager = (HorizontalInfiniteCycleViewPager)findViewById(R.id.infiniteViewPager);
        adapter = new DiaryPagerAdapter(eventLists, getApplicationContext(), new DiaryPagerAdapter.OnItemClickListener() {
            @Override
            public void onModify(int pos, DiaryModels event) {
                Intent intent = new Intent(DiaryDialogActivity.this, CreateDiaryActivity.class);
                intent.putExtra("event", event);
                startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
                overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
            }

            @Override
            public void onDelete(int pos, DiaryModels event) {

            }
        });
        pager.setAdapter(adapter);
        this.setFinishOnTouchOutside(true);
    }

    private void initData() {
        Intent intent = getIntent();
        eventLists = (List<DiaryModels>) intent.getSerializableExtra("eventList");
        DiaryModels event = intent.getParcelableExtra("event");

        // 순서 바꾸기
        List<DiaryModels> tmpList = new ArrayList<DiaryModels>();
        tmpList = eventLists;

        int eventPoint = 0;
        for(int i = 0 ; i < tmpList.size() ; i++){
            if(newEventLists.size() > 0){
                newEventLists.add(tmpList.get(i));
            }
            if(Objects.equals(tmpList.get(i).getmDate().getTime(), event.getmDate().getTime())){
                newEventLists.add(event);
                eventPoint = i;
            }
        }

        for( int i = 0 ; i < eventPoint ; i ++){
            newEventLists.add(tmpList.get(i));
        }

        eventLists = newEventLists;

        // TODO : eventList 순서바꾸기
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int action = CreateDiaryActivity.extractActionFromIntent(data);
                DiaryModels event = CreateDiaryActivity.extractEventFromIntent(data);

                switch (action) {
                    case CreateDiaryActivity.ACTION_EDIT: {

                        DiaryModels oldEvent = null;
                        for (DiaryModels e : eventLists) {
                            if (Objects.equals(event.getmDate(), e.getmDate())) {
                                oldEvent = e;
                                break;
                            }
                        }

                        eventLists.remove(oldEvent);
                        eventLists.add(event);

                        adapter.notifyDataSetChanged();
                        pager.notifyDataSetChanged();

                        finish();
                        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);

                        break;
                    }
                    case CreateDiaryActivity.ACTION_DELETE:

                        eventLists.remove(event);

                        adapter.notifyDataSetChanged();
                        pager.notifyDataSetChanged();

                        finish();
                        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
                        break;
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}