package com.pastel.dalpook.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pastel.dalpook.Calendar.MonthActivity;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.DB.DBModels;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.LoadingDialog;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_Cal extends Fragment {

    private RecyclerView rcv;
    private TableLayout tl;

    private DBHelper dbHelper;
    private DBModels dbModels;
    LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_second, container, false); //pass the correct layout name for the fragment

        init(rootView);
        getDB();
        setTable();


        return rootView;
    }

    private void init(View view){

        rcv = (RecyclerView)view.findViewById(R.id.rcv_cal);
        tl = (TableLayout) view.findViewById(R.id.tl_cal);

    }

    private void setTable(){
        List<String> list = new ArrayList<>();
        if(dbModels.getMonth().equals("T")) list.add("month");
        if(dbModels.getMonth_list().equals("T")) list.add("month_list");
        if(dbModels.getWeek().equals("T")) list.add("week");
        if(dbModels.getLesson().equals("T")) list.add("lesson");
        if(dbModels.getWork().equals("T")) list.add("work");
        if(dbModels.getDiary().equals("T")) list.add("diary");

        if(list.size() > 0){
            int rowidx = 0;

            // 행의 개수만큼 루프하며 행 생성
            for(int i = 0 ; i < list.size() ; i++){

                if(i%2 == 0){ // 테이블 로우 생성
                    TableRow tableRow = new TableRow(getContext());
                    tableRow.setBackground(new ColorDrawable(Color.TRANSPARENT));
                    tl.addView(tableRow);
                }

                if( i>1 && ( i%2 == 0) ){ //테이블 로우 인덱스 변경
                    rowidx = rowidx + 1;
                }
                TableRow tableRow = (TableRow)tl.getChildAt(rowidx);
                View view = (View) getLayoutInflater(). inflate(R.layout.tablerow_borderline, null);
                RelativeLayout bg = (RelativeLayout) view.findViewById(R.id.tb_border);
                TextView textView = (TextView) view.findViewById(R.id.txt_tb);
                ImageView imageView = (ImageView)view.findViewById(R.id.iv_tb);

                // 이미지 리소스, 크기변경
                /*
                월간 : 60x48
                월간리스트 : 35x54
                나머지 등등
                 */
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                params.width = 60;
                params.height = 48;
                //params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                //params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(params);
                textView.setText("달력");

                tableRow.addView(bg);
            }

        }


    }

    private void getDB(){
        dbModels = new DBModels();
        dbHelper = new DBHelper(getContext());
        Cursor cursorSets = dbHelper.getSets();

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

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause(){
        super.onPause();


    }
}