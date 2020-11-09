package com.pastel.dalpook.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pastel.dalpook.R;
import com.pastel.dalpook.data.Event;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mParentList;
    private ArrayList<Event> mChildList;
    private ViewHolder mChildListViewHolder;
    private HashMap<String, ArrayList<Event>> mChildHashMap;
    private ExpandAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, Event event);
    }

    // CustomExpandableListViewAdapter 생성자
    public ExpandAdapter(Context context, ArrayList<String> parentList, HashMap<String, ArrayList<Event>> childHashMap, OnItemClickListener listener) {
        this.mContext = context;
        this.mParentList = parentList;
        this.mChildHashMap = childHashMap;
        this.mListener = listener;
    }

    /* ParentListView에 대한 method */
    @Override
    public String getGroup(int groupPosition) { // ParentList의 position을 받아 해당 TextView에 반영될 String을 반환
        return mParentList.get(groupPosition);
    }

    @Override
    public int getGroupCount() { // ParentList의 원소 개수를 반환
        return mParentList.size();
    }

    @Override
    public long getGroupId(int groupPosition) { // ParentList의 position을 받아 long값으로 반환
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { // ParentList의 View
        if (convertView == null) {
            LayoutInflater groupInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ParentList의 layout 연결. root로 argument 중 parent를 받으며 root로 고정하지는 않음
            convertView = groupInfla.inflate(R.layout.expandable_group_row, parent, false);
        }

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView) convertView.findViewById(R.id.txt_group_row);
        parentText.setText(getGroup(groupPosition));

        View view = (View) convertView.findViewById(R.id.view_divider);
        if (groupPosition == 0) {
            view.setVisibility(View.GONE);
        }
        return convertView;
    }

    /* 여기서부터 ChildListView에 대한 method */
    @Override
    public Event getChild(int groupPosition, int childPosition) { // groupPostion과 childPosition을 통해 childList의 원소를 얻어옴
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).get(childPosition);

    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).size();

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long 형 값을 반환
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View. 위 ParentList의 View를 얻을 때와 비슷하게 Layout 연결 후, layout 내 TextView, ImageView를 연결
        Event childData = (Event) getChild(groupPosition, childPosition);
        if (convertView == null) {
            mChildListViewHolder= new ViewHolder();
            LayoutInflater childInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.expandable_child_row, null);

            mChildListViewHolder = new ViewHolder();
            mChildListViewHolder.ly_week = (LinearLayout) convertView.findViewById(R.id.ly_week);
            mChildListViewHolder.ly_event = (LinearLayout) convertView.findViewById(R.id.ly_event);
            mChildListViewHolder.txt_el_date = (TextView) convertView.findViewById(R.id.txt_el_date);
            mChildListViewHolder.txt_el_weekday = (TextView) convertView.findViewById(R.id.txt_el_weekday);
            mChildListViewHolder.view_month_list_color = (View) convertView.findViewById(R.id.view_month_list_color);
            mChildListViewHolder.txt_month_list_time = (TextView) convertView.findViewById(R.id.txt_month_list_time);
            mChildListViewHolder.txt_month_list_content = (TextView) convertView.findViewById(R.id.txt_month_list_content);
            mChildListViewHolder.btn_month_list_delete = (Button) convertView.findViewById(R.id.btn_month_list_delete);
            convertView.setTag(mChildListViewHolder);

        }else{
            mChildListViewHolder = (ViewHolder) convertView.getTag();
        }

        //색상
        GradientDrawable mGradientDrawable = (GradientDrawable) mChildListViewHolder.view_month_list_color.getBackground();
        mGradientDrawable.setStroke(10, childData.getColor());

        // 해당 이벤트 정보 매칭
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        calendar = childData.getDate();

        // 시간 정보
        String setTime = "";

        int isAMorPM = calendar.get(Calendar.AM_PM);

        int Hour = calendar.get(Calendar.HOUR);
        String AMHour = "";
        if (Hour >= 0 && Hour < 10) {
            AMHour = "0" + calendar.get(Calendar.HOUR);
            if (AMHour.equals("00")) {
                AMHour = "12";
            }
        } else {
            AMHour = String.valueOf(calendar.get(Calendar.HOUR));
        }

        int Minute = calendar.get(Calendar.MINUTE);
        String AMMinute = "";
        if (Minute >= 0 && Minute < 10) {
            AMMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            AMMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        switch (isAMorPM) {
            case Calendar.AM:
                setTime = "오전 " + AMHour + ":" + AMMinute;
                break;
            case Calendar.PM:
                setTime = "오후 " + AMHour + ":" + AMMinute;
                break;
        }

        // 요일 정보
        String DayOfWeek = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                DayOfWeek = "일요일";
                break;
            case 2:
                DayOfWeek = "월요일";
                break;
            case 3:
                DayOfWeek = "화요일";
                break;
            case 4:
                DayOfWeek = "수요일";
                break;
            case 5:
                DayOfWeek = "목요일";
                break;
            case 6:
                DayOfWeek = "금요일";
                break;
            case 7:
                DayOfWeek = "토요일";
                break;
        }
        mChildListViewHolder.txt_el_weekday.setText(DayOfWeek);
        mChildListViewHolder.txt_el_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

        // 주말 색상 변경
        if (mChildListViewHolder.txt_el_weekday.getText().equals("일요일")) {
            mChildListViewHolder.txt_el_weekday.setTextColor(Color.parseColor("#de4e54"));
            mChildListViewHolder.txt_el_date.setTextColor(Color.parseColor("#de4e54"));
        } else if (mChildListViewHolder.txt_el_weekday.getText().equals("토요일")) {
            mChildListViewHolder.txt_el_weekday.setTextColor(Color.parseColor("#6176ff"));
            mChildListViewHolder.txt_el_date.setTextColor(Color.parseColor("#6176ff"));
        }else{
            mChildListViewHolder.txt_el_weekday.setTextColor(Color.parseColor("#c4ccff"));
            mChildListViewHolder.txt_el_date.setTextColor(Color.parseColor("#c4ccff"));
        }

        // 같은 날짜내에 여러 일정이 있을 때 첫번째를 제외한 일정에 날짜 요일 투명화
       if (childPosition > 0) {
            Calendar prevCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
            Event prevData = (Event) getChild(groupPosition, childPosition - 1);
            prevCal = prevData.getDate();

            if (prevCal.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                mChildListViewHolder.txt_el_weekday.setTextColor(Color.TRANSPARENT);
                mChildListViewHolder.txt_el_date.setTextColor(Color.TRANSPARENT);
            }
        }


        mChildListViewHolder.txt_month_list_time.setText(setTime);
        mChildListViewHolder.txt_month_list_content.setText(childData.getTitle());

        mChildListViewHolder.btn_month_list_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(mChildListViewHolder.ly_event, childPosition, (Event) getChild(groupPosition, childPosition));
            }
        });

        //mChildListViewHolder.mChildListViewText.setText(getChild(groupPosition, childPosition).mChildText);
        //mChildListViewHolder.mChildListViewIcon.setImageDrawable(getChild(groupPosition, childPosition).mChildItem);
        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    } // stable ID인지 boolean 값으로 반환

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    } // 선택여부를 boolean 값으로 반환

    public class ViewHolder {
        // each data item is just a string in this case

        public TextView txt_el_date;
        public TextView txt_el_weekday;

        private LinearLayout ly_week;
        public LinearLayout ly_event;
        public View view_month_list_color;

        public TextView txt_month_list_time;
        public TextView txt_month_list_content;

        public Button btn_month_list_delete;
    }

}