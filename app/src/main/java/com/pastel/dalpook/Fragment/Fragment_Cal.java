package com.pastel.dalpook.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pastel.dalpook.Calendar.DiaryActivity;
import com.pastel.dalpook.Calendar.LessonActivity;
import com.pastel.dalpook.Calendar.MonthActivity;
import com.pastel.dalpook.Calendar.MonthListActivity;
import com.pastel.dalpook.Calendar.WeekActivity;
import com.pastel.dalpook.Calendar.WorkActivity;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Utils.LoadingDialog;
import com.pastel.dalpook.data.DBModels;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.CalListAdapter;
import com.pastel.dalpook.Utils.RecyclerDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Fragment_Cal extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CalListAdapter mAdapter = null;

    private TableLayout tl;

    private DBHelper dbHelper;
    private DBModels dbModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = requireActivity().getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_second, container, false); //pass the correct layout name for the fragment

        init(rootView);
        setTableLayout();
        setRecyclerView();

        return rootView;
    }

    private void init(View view){

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rcv_cal);
        tl = (TableLayout) view.findViewById(R.id.tl_cal);
    }

    public void setRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(2);
        mRecyclerView.addItemDecoration(recyclerDecoration);

        Calendar TodayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String date = dateFormat.format(TodayCal.getTime());
        int week = TodayCal.get(Calendar.DAY_OF_WEEK);

        mAdapter = new CalListAdapter(dbHelper.getCalToday(date, String.valueOf(week)), getContext(), mRecyclerView, new CalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, String getFlag) {

                LoadingDialog loadingDialog = new LoadingDialog(getContext());
                loadingDialog.progressOn();
                new Thread() {
                    @Override
                    public void run() {
                        switch (getFlag){
                            case "M":
                                Intent intent = new Intent(getContext(), MonthActivity.class);
                                startActivity(intent);
                                break;
                            case "W":
                                Intent intent2 = new Intent(getContext(), WeekActivity.class);
                                startActivity(intent2);
                                break;
                            case "L":
                                Intent intent3 = new Intent(getContext(), LessonActivity.class);
                                startActivity(intent3);
                                break;
                            case "B":
                                Intent intent4 = new Intent(getContext(), WorkActivity.class);
                                startActivity(intent4);
                                break;
                            case "D":
                                Intent intent5 = new Intent(getContext(), DiaryActivity.class);
                                startActivity(intent5);
                                break;
                        }
                        loadingDialog.progressOff();
                    }
                }.start();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mAdapter.getItemCount());
    }

    public void setTableLayout(){

        /**
         * 세팅 정보
         */
        Cursor cursorSets = getSetDB();
        //세팅 정보
        if (cursorSets.getCount() > 0) {
            cursorSets.moveToFirst();

            dbModels.setPush(cursorSets.getString(0));
            dbModels.setMonth(cursorSets.getString(1));
            dbModels.setMonth_list(cursorSets.getString(2));
            dbModels.setWeek(cursorSets.getString(3));
            dbModels.setLesson(cursorSets.getString(4));
            dbModels.setWork(cursorSets.getString(5));
            dbModels.setDiary(cursorSets.getString(6));
        }

        List<String> list = new ArrayList<>();
        if(dbModels.getMonth().equals("T")) list.add("month");
        if(dbModels.getMonth_list().equals("T")) list.add("month_list");
        if(dbModels.getWeek().equals("T")) list.add("week");
        if(dbModels.getLesson().equals("T")) list.add("lesson");
        if(dbModels.getWork().equals("T")) list.add("work");
        if(dbModels.getDiary().equals("T")) list.add("diary");

        if(list.size() > 0){
            int rowCnt = (list.size()%2) + (list.size()/2);
            int colCnt = 2;
            int listIdx = 0;

            tl.removeAllViews();
            TableRow tableRow[] = new TableRow[rowCnt];
            View view[][] = new View[rowCnt][colCnt];
            LayoutInflater inflater=this.getLayoutInflater(); //this refers to Activity Foo.

            for(int i = 0 ; i < rowCnt ; i++){
                tableRow[i] = new TableRow(getContext());
                tableRow[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow[i].setGravity(Gravity.CENTER);
                tableRow[i].setWeightSum(2); //total row weight

                TableRow.LayoutParams lp = (TableRow.LayoutParams) tableRow[i].getLayoutParams();
                lp.weight = 1; //column weight
                lp.height = lp.width;
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);


                for(int j = 0 ; j < colCnt ; j ++){
                    view[i][j] = inflater.inflate(R.layout.tablerow_borderline, null);
                    view[i][j].setLayoutParams(lp);
                    TextView textView = view[i][j].findViewById(R.id.txt_tb);
                    ImageView imageView = view[i][j].findViewById(R.id.iv_tb);

                    tableRow[i].addView(view[i][j]);

                    switch (list.get(listIdx)){
                        case "month" :
                            setItemChange(textView, imageView, "월간달력");
                            tableRow[i].getChildAt(j).setTag("month");
                            break;
                        case "month_list":
                            setItemChange(textView, imageView, "월간목록달력");
                            tableRow[i].getChildAt(j).setTag("month_list");
                            break;
                        case "week" :
                            setItemChange(textView, imageView, "주간달력");
                            tableRow[i].getChildAt(j).setTag("week");
                            break;
                        case "lesson" :
                            setItemChange(textView, imageView, "수강시간표");
                            tableRow[i].getChildAt(j).setTag("lesson");
                            break;
                        case "work" :
                            setItemChange(textView, imageView, "업무일지");
                            tableRow[i].getChildAt(j).setTag("work");
                            break;
                        case "diary" :
                            setItemChange(textView, imageView, "다이어리");
                            tableRow[i].getChildAt(j).setTag("diary");
                            break;
                    }

                    DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
                    int width = (int) (dm.widthPixels * 0.45); // Display 사이즈의 45%
                    ViewGroup.LayoutParams params = tableRow[i].getChildAt(j).getLayoutParams();
                    params.height = width;
                    tableRow[i].getChildAt(j).setLayoutParams(params);

                    view[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            LoadingDialog loadingDialog = new LoadingDialog(getContext());
                            loadingDialog.progressOn();
                            new Thread() {
                                @Override
                                public void run() {
                                    if (view.getTag().equals("month")){
                                        Intent intent = new Intent(getContext(), MonthActivity.class);
                                        startActivity(intent);
                                    }else if (view.getTag().equals("month_list")){
                                        Intent intent = new Intent(getContext(), MonthListActivity.class);
                                        startActivity(intent);
                                    }else if (view.getTag().equals("week")){
                                        Intent intent = new Intent(getContext(), WeekActivity.class);
                                        startActivity(intent);
                                    }else if (view.getTag().equals("lesson")){
                                        Intent intent = new Intent(getContext(), LessonActivity.class);
                                        startActivity(intent);
                                    }else if (view.getTag().equals("work")){
                                        Intent intent = new Intent(getContext(), WorkActivity.class);
                                        startActivity(intent);
                                    }else if (view.getTag().equals("diary")){
                                        Intent intent = new Intent(getContext(), DiaryActivity.class);
                                        startActivity(intent);
                                    }

                                    loadingDialog.progressOff();
                                }
                            }.start();

                        }
                    });
                    if(i == rowCnt-1){
                        if(list.size()%2 == 1){
                            break;
                        }
                    }
                    listIdx = listIdx + 1;
                }
                tl.addView(tableRow[i]);
                tl.invalidate();
            }
        }

    }

    private Cursor getSetDB(){
        dbModels = new DBModels();
        dbHelper = new DBHelper(getContext());
        return dbHelper.getSets();
    }

    /*
    private Cursor getTodayDB(){
        Calendar TodayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String date = dateFormat.format(TodayCal.getTime());

        return  dbHelper.getCalToday(date);
    }

     */

    private void setItemChange(TextView textView, ImageView imageView, String text){
        textView.setText(text);

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        /**
         * 월간 : 60x48 / 월간목록 : 58x42 / 주간 : 70x60 / 수강 : 35x54 / 업무일지 : 42x50 / 다이어리 : 42x50
         */
        switch (text){
            case "월간달력":
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                params.height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
                imageView.setBackgroundResource(R.drawable.ic_month);
                break;
            case "월간목록달력":
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 58, getResources().getDisplayMetrics());
                params.height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, getResources().getDisplayMetrics());
                imageView.setBackgroundResource(R.drawable.ic_month_list);
                break;
            case "주간달력":
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                params.height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                imageView.setBackgroundResource(R.drawable.ic_week);
                break;
            case "수강시간표":
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                params.height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics());
                imageView.setBackgroundResource(R.drawable.ic_lesson);
                break;
            case "업무일지":
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, getResources().getDisplayMetrics());
                params.height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                imageView.setBackgroundResource(R.drawable.ic_work);
                break;
            case "다이어리":
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, getResources().getDisplayMetrics());
                params.height =(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                imageView.setBackgroundResource(R.drawable.ic_diary);
                break;
        }
        imageView.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         * 오늘의 일정
         */
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(2);
        mRecyclerView.addItemDecoration(recyclerDecoration);

        Calendar TodayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String date = dateFormat.format(TodayCal.getTime());
        int week = TodayCal.get(Calendar.DAY_OF_WEEK);

        mAdapter = new CalListAdapter(dbHelper.getCalToday(date, String.valueOf(week)), getContext(), mRecyclerView, new CalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, String getFlag) {
                LoadingDialog loadingDialog = new LoadingDialog(getContext());
                loadingDialog.progressOn();
                new Thread() {
                    @Override
                    public void run() {
                        switch (getFlag){
                            case "M":
                                Intent intent = new Intent(getContext(), MonthActivity.class);
                                startActivity(intent);
                                break;
                            case "W":
                                Intent intent2 = new Intent(getContext(), WeekActivity.class);
                                startActivity(intent2);
                                break;
                            case "L":
                                Intent intent3 = new Intent(getContext(), LessonActivity.class);
                                startActivity(intent3);
                                break;
                            case "B":
                                Intent intent4 = new Intent(getContext(), WorkActivity.class);
                                startActivity(intent4);
                                break;
                            case "D":
                                Intent intent5 = new Intent(getContext(), DiaryActivity.class);
                                startActivity(intent5);
                                break;
                        }
                        loadingDialog.progressOff();
                    }
                }.start();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mAdapter.getItemCount());
        //setTableLayout();
    }

    public void removeRecyclerItem(int pos){
        CalListAdapter adapter = (CalListAdapter) mRecyclerView.getAdapter();
        adapter.removeItem(pos);
    }
}