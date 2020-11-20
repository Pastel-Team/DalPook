package com.pastel.dalpook.Popup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.pastel.dalpook.DB.DBHelper;
import com.pastel.dalpook.R;
import com.pastel.dalpook.Utils.ColorUtils;
import com.pastel.dalpook.Utils.DiaryPagerAdapter;
import com.pastel.dalpook.Utils.SelectDateActivity;
import com.pastel.dalpook.Utils.SelectDateAndTimeActivity;
import com.pastel.dalpook.data.DiaryModels;
import com.pastel.dalpook.data.Event;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CreateDiaryActivity extends AppCompatActivity {

    public static final int ACTION_DELETE = 1;
    public static final int ACTION_EDIT = 2;
    public static final int ACTION_CREATE = 3;
    public static final int ACTION_MODIFY = 4;

    private static final String INTENT_EXTRA_ACTION = "intent_extra_action";

    private TextView txt_date;
    private ImageView iv_img;
    private EditText edt_title;
    private EditText edt_desc;

    private final int GET_GALLERY_IMAGE = 200;
    private static final int SET_DATE_AND_TIME_REQUEST_CODE = 300;
    private static final String INTENT_EXTRA_EVENT = "intent_extra_event";

    private Snackbar snackbar;
    private Calendar mCalendar = Calendar.getInstance();

    private DiaryModels mOriginalEvent = null;
    private String path = "";
    //private Uri selectedImageUri = null;
    private String oldDate = "";
    private String status = "ADD"; // ADD : 신규등록 ,  MOD : 수정

    private List<DiaryModels> eventList = new ArrayList<DiaryModels>();

    private DiaryPagerAdapter mAdapter;

    public static int extractActionFromIntent(Intent intent) {
        return intent.getIntExtra(INTENT_EXTRA_ACTION, 0);
    }

    public static DiaryModels extractEventFromIntent(Intent intent) {
        return intent.getParcelableExtra(INTENT_EXTRA_EVENT);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diary);
        init();
    }
    private void init(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String today = dateFormat.format(calendar.getTime());

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        Button btn_delete = (Button)findViewById(R.id.btn_delete);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
        Button btn_save = (Button)findViewById(R.id.btn_save);
        txt_date = (TextView)findViewById(R.id.txt_date);
        iv_img = (ImageView)findViewById(R.id.iv_img);
        edt_title = (EditText)findViewById(R.id.txt_title);
        edt_desc = (EditText)findViewById(R.id.txt_desc);

        Intent intent = getIntent();
        mOriginalEvent = intent.getParcelableExtra("event");
        if(mOriginalEvent == null){ // New
            mCalendar.setTime(new Date());
            btn_delete.setVisibility(View.INVISIBLE);
            txt_date.setText(today);
            status = "ADD";
        }else{ // Modify
            mCalendar = mOriginalEvent.getmDate();
            //old date
            oldDate = dateFormat.format(mCalendar.getTime());
            btn_delete.setVisibility(View.VISIBLE);

            if(mOriginalEvent.getmImg().equals("")){
                iv_img.setImageResource(R.drawable.diary_default);
            }else{
                Uri uri = Uri.parse(mOriginalEvent.getmImg());
                iv_img.setImageURI(uri);
                if(iv_img.getDrawable() == null){
                    iv_img.setImageResource(R.drawable.diary_default);
                }
            }
            txt_date.setText(dateFormat.format(mCalendar.getTime()));
            edt_title.setText(mOriginalEvent.getmTitle());
            edt_desc.setText(mOriginalEvent.getmDesc());

            status = "MOD";
        }

        txt_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Activity context = CreateDiaryActivity.this;
                Intent intent = SelectDateActivity.makeIntent(context, mCalendar);

                startActivityForResult(intent,
                        SET_DATE_AND_TIME_REQUEST_CODE,
                        ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
            }
        });
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 저장
                if(edt_title.getText().toString().equals("")){
                    imm.hideSoftInputFromWindow(edt_title.getWindowToken(), 0);
                    snackbar = Snackbar.make(view, "제목을 입력해주세요.", Snackbar.LENGTH_SHORT);
                    View snackView = snackbar.getView();
                    snackView.setBackgroundColor(Color.parseColor("#e8c792"));
                    TextView textView = (TextView) snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.parseColor("#2e3145"));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    TextView actTextView = (TextView) snackView.findViewById(com.google.android.material.R.id.snackbar_action);
                    actTextView.setTypeface(actTextView.getTypeface(), Typeface.BOLD);
                    actTextView.setTextColor(Color.parseColor("#2e3145"));
                    actTextView.setTextSize( 16 );
                    actTextView.setMaxLines( 3 );
                    snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    }).show();
                }else if(edt_desc.getText().toString().equals("")){
                    imm.hideSoftInputFromWindow(edt_title.getWindowToken(), 0);
                    snackbar = Snackbar.make(view, "내용을 입력해주세요.", Snackbar.LENGTH_SHORT);
                    View snackView = snackbar.getView();
                    snackView.setBackgroundColor(Color.parseColor("#e8c792"));
                    TextView textView = (TextView) snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.parseColor("#2e3145"));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    TextView actTextView = (TextView) snackView.findViewById(com.google.android.material.R.id.snackbar_action);
                    actTextView.setTypeface(actTextView.getTypeface(), Typeface.BOLD);
                    actTextView.setTextColor(Color.parseColor("#2e3145"));
                    actTextView.setTextSize( 16 );
                    actTextView.setMaxLines( 3 );
                    snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    }).show();
                }else{
                    save();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(CreateDiaryActivity.this);

                Calendar calendar1 = Calendar.getInstance();
                calendar1 = mOriginalEvent.getmDate();
                String date = dateFormat.format(calendar1.getTime());
                dbHelper.deleteDiary(date);


                setResult(RESULT_OK, new Intent()
                        .putExtra(INTENT_EXTRA_ACTION, ACTION_DELETE)
                        .putExtra(INTENT_EXTRA_EVENT, mOriginalEvent));
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_out_down);

            }
        });
    }

    private void save(){

        int action = status.equals("ADD") ? ACTION_CREATE : ACTION_EDIT;

        DBHelper dbHelper = new DBHelper(this);
        String date = txt_date.getText().toString();
        String title = edt_title.getText().toString();
        String desc = edt_desc.getText().toString();

        Calendar calendar = Calendar.getInstance();
        String[] splitdate = date.split("-");
        int year = Integer.parseInt(splitdate[0]);
        int month = Integer.parseInt(splitdate[1]);
        int day = Integer.parseInt(splitdate[2]) ;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String msg = "";
        if(status.equals("ADD")){
            msg = dbHelper.insertDiary(date, title, desc, path);
        }else if(status.equals("MOD")){
            msg = dbHelper.updateDiary(oldDate, date, title, desc, path);
        }

        if(msg.equals("")){
            DiaryModels event = new DiaryModels(
                    calendar,
                    title,
                    desc,
                    path);

            setResult(RESULT_OK, new Intent()
                    .putExtra(INTENT_EXTRA_ACTION, action)
                    .putExtra(INTENT_EXTRA_EVENT, event));
            finish();

            if (action == ACTION_CREATE)
                overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
        }else{
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //selectedImageUri = data.getData();
            //iv_img.setImageURI(selectedImageUri);
            path = getPath(data.getData());
            //File file = new File(a);
            Uri uri = Uri.parse(path);
            iv_img.setImageURI(uri);
        }

        if (requestCode == SET_DATE_AND_TIME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                mCalendar = SelectDateActivity.extractCalendarFromIntent(data);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                txt_date.setText(dateFormat.format(mCalendar.getTime()));

            }
        }
    }
}