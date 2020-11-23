package com.pastel.dalpook.Fragment;

import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.Utils.LoadingDialog;
import com.pastel.dalpook.data.DBModels;
import com.pastel.dalpook.R;

import java.util.Objects;

public class Fragment_Setting extends Fragment implements BillingProcessor.IBillingHandler, SensorEventListener {

    private BillingProcessor bp;

    private DBHelper dbHelper;
    private DBModels dbModels;

    //private CheckBox chk_push;
    private CheckBox chk_month;
    private CheckBox chk_month_list;
    private CheckBox chk_week;
    //private CheckBox chk_lesson;
    //private CheckBox chk_work;
    private CheckBox chk_diary;
    private Button btn_donate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = requireActivity().getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_third, container, false); //pass the correct layout name for the fragment

        bp = new BillingProcessor(getContext(), getString(R.string.bill_license_key), this);
        bp.initialize();

        init(rootView);
        getDB();

        /*
        if(dbModels.getPush().equals("T")){
            chk_push.setChecked(true);
            chk_push.setText("ON");
        }else{
            chk_push.setChecked(false);
            chk_push.setText("OFF");
        }

         */
        if(dbModels.getMonth().equals("T")){
            chk_month.setChecked(true);
        }else{
            chk_month.setChecked(false);
        }
        if(dbModels.getMonth_list().equals("T")){
            chk_month_list.setChecked(true);
        }else{
            chk_month_list.setChecked(false);
        }
        if(dbModels.getWeek().equals("T")){
            chk_week.setChecked(true);
        }else{
            chk_week.setChecked(false);
        }
        /*
        if(dbModels.getLesson().equals("T")){
            chk_lesson.setChecked(true);
        }else{
            chk_lesson.setChecked(false);
        }
        if(dbModels.getWork().equals("T")){
            chk_work.setChecked(true);
        }else{
            chk_work.setChecked(false);
        }

         */
        if(dbModels.getDiary().equals("T")){
            chk_diary.setChecked(true);
        }else{
            chk_diary.setChecked(false);
        }

        /*
        chk_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_push.isChecked()){
                    chk_push.setText("ON");
                    dbHelper.updateSets("push", "T");
                    dbModels.setPush("T");
                }else{
                    chk_push.setText("OFF");
                    dbHelper.updateSets("push", "F");
                    dbModels.setPush("F");
                }

                Fragment_Cal f = new Fragment_Cal();
                f.setTableLayout();
            }
        });

         */

        chk_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_month.isChecked()){
                    dbHelper.updateSets("month", "T");
                    dbModels.setMonth("T");
                }else{
                    dbHelper.updateSets("month", "F");
                    dbModels.setMonth("F");
                }
            }
        });

        chk_month_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_month_list.isChecked()){
                    dbHelper.updateSets("month_list", "T");
                    dbModels.setMonth_list("T");
                }else{
                    dbHelper.updateSets("month_list", "F");
                    dbModels.setMonth_list("F");
                }
            }
        });

        chk_week.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_week.isChecked()){
                    dbHelper.updateSets("week", "T");
                    dbModels.setWeek("T");
                }else{
                    dbHelper.updateSets("week", "F");
                    dbModels.setWeek("F");
                }
            }
        });

        /*
        chk_lesson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_lesson.isChecked()){
                    dbHelper.updateSets("lesson", "T");
                    dbModels.setLesson("T");
                }else{
                    dbHelper.updateSets("lesson", "F");
                    dbModels.setLesson("F");
                }
            }
        });

        chk_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_work.isChecked()){
                    dbHelper.updateSets("work", "T");
                    dbModels.setWork("T");
                }else{
                    dbHelper.updateSets("work", "F");
                    dbModels.setWork("F");
                }
            }
        });

         */

        chk_diary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chk_diary.isChecked()){
                    dbHelper.updateSets("diary", "T");
                    dbModels.setDiary("T");
                }else{
                    dbHelper.updateSets("diary", "F");
                    dbModels.setDiary("F");
                }
            }
        });


        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPurchase();
            }
        });

        return rootView;
    }

    private void init(View view){
        //chk_push = (CheckBox) view.findViewById(R.id.chk_push);
        chk_month = (CheckBox) view.findViewById(R.id.chk_month);
        chk_month_list = (CheckBox) view.findViewById(R.id.chk_month_list);
        chk_week = (CheckBox) view.findViewById(R.id.chk_week);
        //chk_lesson = (CheckBox) view.findViewById(R.id.chk_lesson);
        //chk_work = (CheckBox) view.findViewById(R.id.chk_work);
        chk_diary = (CheckBox) view.findViewById(R.id.chk_diary);
        btn_donate = (Button) view.findViewById(R.id.btn_donate);

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


    /**
     * 인앱 결제
     */

    private void onPurchase(){
        bp.purchase(getActivity(), "donate");
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        // 구매 성공시 호출
        if (productId.equals(getString(R.string.bill_sku))) {
            Toast.makeText(getContext(), "후원 해주셔서 감사합니다!", Toast.LENGTH_SHORT).show();

            // 결제후 다시 지속 결제가 가능하도록 컨슘처리.
            bp.consumePurchase(getString(R.string.bill_sku));
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        // 구매 여부 이력 저장용으로 사용하면됨
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        // 결제 오류시 메세지 처리정도 하면 됨

    }

    @Override
    public void onBillingInitialized() {
        // 구매여부를 여기서 저장하면됨
    }
}