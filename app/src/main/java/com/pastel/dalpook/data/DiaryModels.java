package com.pastel.dalpook.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class DiaryModels implements Parcelable {
    // 다이어리 모델
    private String mTitle;
    private String mDesc;
    private Calendar mDate;
    private String mImg;

    public DiaryModels(Calendar date, String title, String desc, String uri) {
        mDate = date;
        mTitle = title;
        mDesc = desc;
        mImg = uri;
    }

    protected DiaryModels(Parcel in) {
        mDate = (Calendar) in.readSerializable();
        mTitle = in.readString();
        mDesc = in.readString();
        mImg = in.readString();
    }

    public static final Creator<DiaryModels> CREATOR = new Creator<DiaryModels>() {
        @Override
        public DiaryModels createFromParcel(Parcel in) {
            return new DiaryModels(in);
        }

        @Override
        public DiaryModels[] newArray(int size) {
            return new DiaryModels[size];
        }
    };

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public Calendar getmDate() {
        return mDate;
    }

    public void setmDate(Calendar mDate) {
        this.mDate = mDate;
    }

    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(mDate);
        parcel.writeString(mTitle);
        parcel.writeString(mDesc);
        parcel.writeString(mImg);
    }
}
