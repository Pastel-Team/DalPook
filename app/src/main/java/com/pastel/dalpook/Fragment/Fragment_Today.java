package com.pastel.dalpook.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pastel.dalpook.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Fragment_Today extends Fragment {

    private TextView txt_year;
    private TextView txt_month;
    private TextView txt_day;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LayoutInflater lf = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_main, container, false); //pass the correct layout name for the fragment
        init(rootView);

        // 현재날짜 구하기
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        txt_year.setText(yearFormat.format(currentTime));
        txt_month.setText(monthFormat.format(currentTime));
        txt_day.setText(dayFormat.format(currentTime));

        return rootView;
    }

    private void init(View view){
        txt_year = (TextView)view.findViewById(R.id.txt_year);
        txt_month = (TextView)view.findViewById(R.id.txt_month);
        txt_day = (TextView)view.findViewById(R.id.txt_day);
    }
}
