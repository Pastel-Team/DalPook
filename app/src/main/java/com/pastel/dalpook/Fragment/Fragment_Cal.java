package com.pastel.dalpook.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pastel.dalpook.Calendar.CalMonthActivity;
import com.pastel.dalpook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Fragment_Cal extends Fragment {

    private RecyclerView rcv;
    private Button btn_test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_second, container, false); //pass the correct layout name for the fragment
        init(rootView);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CalMonthActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void init(View view){
        rcv = (RecyclerView)view.findViewById(R.id.rcv_cal);
        btn_test = (Button)view.findViewById(R.id.btn_test);
    }
}