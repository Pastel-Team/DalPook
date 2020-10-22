package com.pastel.dalpook.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pastel.dalpook.R;
import com.pastel.dalpook.ViewPagerAdapter;

import java.util.Objects;

public class Fragment_Setting extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View rootView = lf.inflate(R.layout.fragment_second, container, false); //pass the correct layout name for the fragment
        init(rootView);


        return rootView;
    }

    private void init(View view){

    }
}