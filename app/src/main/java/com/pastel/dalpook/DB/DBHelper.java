package com.pastel.dalpook.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pastel.dalpook.data.CalModels;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dalpook.db";
    private static final int DATABASE_VERSION = 3;

    // 설정 테이블 T:true (on) / F:false (off)
    public static final String TABLE_NAME_SET = "SETTINGS";
    public static final String COLUMN_PUSH = "push";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_MONTH_LIST = "month_list";
    public static final String COLUMN_WEEK = "week";
    public static final String COLUMN_LESSON = "lesson";
    public static final String COLUMN_WORK = "work";
    public static final String COLUMN_DIARY = "diary";

    // 기록 내용
    public static final String TABLE_NAME_CONT = "CONTENTS";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TIME_END = "time_end";
    public static final String COLUMN_CONT = "cont";
    public static final String COLUMN_FLAG = "flag"; //달력 식별용.  M:월간 / W:주간 / L:수강표 / B:업무일지 / D:다이어리
    public static final String COLUMN_COLOR = "color";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 설정 테이블
        db.execSQL(" CREATE TABLE " + TABLE_NAME_SET + " (" +
                COLUMN_PUSH + " TEXT, " +
                COLUMN_MONTH + " TEXT," +
                COLUMN_MONTH_LIST + " TEXT," +
                COLUMN_WEEK + " TEXT," +
                COLUMN_LESSON + " TEXT," +
                COLUMN_WORK + " TEXT," +
                COLUMN_DIARY + " TEXT);"
        );

        // 기록 테이블
        db.execSQL(" CREATE TABLE " + TABLE_NAME_CONT + " (" +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT," +
                COLUMN_TIME_END + " TEXT," +
                COLUMN_CONT + " TEXT, " +
                COLUMN_COLOR + " TEXT, " +
                COLUMN_FLAG + " TEXT);"
        );

        // 초기 설정
        ContentValues infValues = new ContentValues();
        infValues.put(COLUMN_PUSH, "T");
        infValues.put(COLUMN_MONTH, "T");
        infValues.put(COLUMN_MONTH_LIST, "T");
        infValues.put(COLUMN_WEEK, "T");
        infValues.put(COLUMN_LESSON, "T");
        infValues.put(COLUMN_WORK, "T");
        infValues.put(COLUMN_DIARY, "T");
        db.insert(TABLE_NAME_SET, null, infValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONT);
        this.onCreate(db);
    }

    // 오늘의 일정 셀렉트
    public Cursor getToday(String date) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_TIME,
                COLUMN_CONT,
                COLUMN_COLOR,
                COLUMN_FLAG
        };

        //String selction = COLUMN_FLAG + " <> B OR D," + COLUMN_DATE + " = " + date;

        Cursor cursor = db.query(
                TABLE_NAME_CONT,
                projection,
                COLUMN_FLAG+"<>? AND "+ COLUMN_FLAG + "<>? AND "+ COLUMN_DATE + "=?",
                new String[]{"B", "D", date},
                null,
                null,
                COLUMN_TIME + " ASC");

        return cursor;
    }

    // 오늘의 일정 (달력 프래그먼트) 셀렉트
    public List<CalModels> getCalToday(String date) {

        List<CalModels> urlLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COLUMN_TIME,
                COLUMN_COLOR,
                COLUMN_FLAG
        };

        Cursor cursor = db.query(
                TABLE_NAME_CONT,
                projection,
                COLUMN_FLAG+"<>? AND "+ COLUMN_FLAG + "<>? AND "+ COLUMN_DATE + "=?",
                new String[]{"B", "D", date},
                null,
                null,
                COLUMN_TIME + " ASC");

        CalModels models;

        if (cursor.moveToFirst()) {
            do {
                models = new CalModels();

                models.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                models.setColor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_COLOR))));
                models.setFlag(cursor.getString(cursor.getColumnIndex(COLUMN_FLAG)));
                urlLinkedList.add(models);
            } while (cursor.moveToNext());
        }


        return urlLinkedList;
    }

    // 세팅값 데이터 셀렉트
    public Cursor getSets() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME_SET;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    // 기록내용 데이터 셀렉트
    public Cursor getConts(String flag) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_DATE,
                COLUMN_TIME,
                COLUMN_TIME_END,
                COLUMN_CONT,
                COLUMN_FLAG,
                COLUMN_COLOR
        };

        String selction = COLUMN_FLAG + " = '" + flag +"'";

        Cursor cursor = db.query(
                TABLE_NAME_CONT,
                projection,
                selction,
                null,
                null,
                null,
                COLUMN_TIME + " ASC");

        return cursor;
    }

    public Cursor getMonthList(){
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        calendar.setTime(new Date());
        String Year = String.valueOf(calendar.get(Calendar.YEAR));
        String Month = String.valueOf(calendar.get(Calendar.MONTH));
        String toMonth = Year + "-" + Month + "-" +"01";


        String[] projection = {
                COLUMN_DATE,
                COLUMN_TIME,
                COLUMN_CONT,
                COLUMN_FLAG,
                COLUMN_COLOR
        };

        Cursor cursor = db.query(
                TABLE_NAME_CONT,
                projection,
                COLUMN_DATE + ">=?",
                new String[]{toMonth},
                null,
                null,
                COLUMN_DATE + ","+ COLUMN_TIME +" ASC");

        return cursor;
    }


    // 세팅값 변경
    public void updateSets(String content, String flag) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(content.equals("push")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_PUSH + "' = '" + flag + "'");
        }else if(content.equals("month")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_MONTH + "' = '" + flag + "'");
        }else if(content.equals("month_list")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_MONTH_LIST + "' = '" + flag + "'");
        }else if(content.equals("week")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_WEEK + "' = '" + flag + "'");
        }else if(content.equals("lesson")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_LESSON + "' = '" + flag + "'");
        }else if(content.equals("work")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_WORK + "' = '" + flag + "'");
        }else if(content.equals("diary")){
            db.execSQL("UPDATE " + TABLE_NAME_SET + " SET '" + COLUMN_DIARY + "' = '" + flag + "'");
        }
        //Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 기록 내용 변경
    public void updateConts(String oldDate, String oldTime, String newDate, String newTime, String newCont, int newColor, String flag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, newDate);
        values.put(COLUMN_TIME, newTime);
        values.put(COLUMN_CONT, newCont);
        values.put(COLUMN_COLOR, newColor);

        db.update(TABLE_NAME_CONT, values, COLUMN_DATE+"=? AND "+ COLUMN_TIME + "=? AND "+ COLUMN_FLAG + "=?", new String[]{oldDate, oldTime, flag});

    }

    // 기록 내용 추가
    public void insertConts(String date, String time, String content, String flag, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues infValues = new ContentValues();
        infValues.put(COLUMN_DATE, date);
        infValues.put(COLUMN_TIME, time);
        infValues.put(COLUMN_CONT, content);
        infValues.put(COLUMN_FLAG, flag);
        infValues.put(COLUMN_COLOR, color);
        db.insert(TABLE_NAME_CONT, null, infValues);
    }

    // 기록 삭제
    public void deleteConts(String date, String time, String flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CONT, COLUMN_DATE+"=? AND "+ COLUMN_TIME + "=? AND "+ COLUMN_FLAG + "=?" , new String[]{date, time, flag});
        //db.execSQL("DELETE FROM " + TABLE_NAME_CONT + " WHERE '" + COLUMN_DATE + "' = '" + date + "' AND '" + COLUMN_TIME + "' = '" + time + "' AND '" + COLUMN_FLAG + "' = '" + flag + "'" );
        //Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

    }
}