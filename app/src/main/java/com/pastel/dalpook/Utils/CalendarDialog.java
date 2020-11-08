package com.pastel.dalpook.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pastel.dalpook.R;
import com.pastel.dalpook.data.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CalendarDialog {

    @SuppressWarnings("unused")
    private static final String TAG = CalendarDialog.class.getSimpleName();

    private final static Calendar sToday = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

    private static final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm", Locale.KOREA);

    private static final float MIN_OFFSET = 0f;
    private static final float MAX_OFFSET = 0.5f;

    private static final float MIN_ALPHA = 0.5f;
    private static final float MIN_SCALE = 0.8f;

    private final Context mContext;

    private Calendar mSelectedDate = sToday;

    private List<Event> mEventList = new ArrayList<>();
    private List<Event> setEventList = new ArrayList<>();
    private OnCalendarDialogListener mListener;
    private OnCalendarDialogDeleteListener mDelListener;

    private AlertDialog mAlertDialog;
    private View mView;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private Handler mHandler;

    private int focusedPos;
    private Pair<Integer, Calendar> setPageAndDay;
    private Calendar setCal = sToday;

    public CalendarDialog(Context context) {
        mContext = context;
        mHandler = new Handler();

        buildView();
    }

    public void setSelectedDate(Calendar selectedDate) {
        mSelectedDate = selectedDate;
        mViewPagerAdapter.setSelectedDate(mSelectedDate);
        mViewPager.setCurrentItem(mViewPagerAdapter.initialPageAndDay.first);

        setCal = selectedDate;
    }

    public void setEventList(List<Event> eventList) {
        mEventList = eventList;
        mViewPagerAdapter.notifyDataSetChanged();
    }

    void setOnCalendarDialogListener(OnCalendarDialogListener listener) {
        mListener = listener;
    }

    void setOnCalendarDialogDeleteListener(OnCalendarDialogDeleteListener mListener){
        mDelListener = mListener;
    }

    public void show() {
        long delayMillis = 100L;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayedShow();
            }
        }, delayMillis);
    }

    private void buildView() {
        mView = View.inflate(mContext, R.layout.dialog_calendar, null);
        mViewPager = mView.findViewById(R.id.viewPager_calendar);
        // Disable clip to padding
        mViewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        mViewPager.setPadding(160, 0, 160, 0);
        mViewPager.setPageMargin(60);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updatePager(mViewPager.findViewWithTag(position), 1f - positionOffset);
                updatePager(mViewPager.findViewWithTag(position + 1), positionOffset);
                updatePager(mViewPager.findViewWithTag(position + 2), 0);
                updatePager(mViewPager.findViewWithTag(position - 1), 0);
            }
        });

        mViewPagerAdapter = new ViewPagerAdapter(mSelectedDate, mEventList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mViewPagerAdapter.initialPageAndDay.first);

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissDialog();
                return false;
            }
        });

        mAlertDialog = new AlertDialog.Builder(mContext).create();
    }

    private void delayedShow() {
        if (mAlertDialog.getWindow() != null)
            mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mAlertDialog.setCanceledOnTouchOutside(true);

        //alert.setContentView(view);
        mAlertDialog.show();
        mAlertDialog.setContentView(mView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mAlertDialog.getWindow();

        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    private void dismissDialog() {
        mAlertDialog.dismiss();
    }

    private void updatePager(View view, float offset) {
        if (view == null)
            return;

        float adjustedOffset = (1.0f - 0.0f) * (offset - MIN_OFFSET) / (MAX_OFFSET - MIN_OFFSET) + 0.0f;
        adjustedOffset = adjustedOffset > 1f ? 1f : adjustedOffset;
        adjustedOffset = adjustedOffset < 0f ? 0f : adjustedOffset;

        float alpha = adjustedOffset * (1f - MIN_ALPHA) + MIN_ALPHA;
        float scale = adjustedOffset * (1f - MIN_SCALE) + MIN_SCALE;

        view.setAlpha(alpha);
        view.setScaleY(scale);
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private static final String DEFAULT_MIN_DATE = "01/01/1992";
        private static final String DEFAULT_MAX_DATE = "01/01/2100";

        private Calendar mMinDate = getCalendarObjectForLocale(DEFAULT_MIN_DATE, Locale.KOREA);
        private Calendar mMaxDate = getCalendarObjectForLocale(DEFAULT_MAX_DATE, Locale.KOREA);

        private Pair<Integer, Calendar> initialPageAndDay;

        private int TOTAL_COUNT;

        ViewPagerAdapter(Calendar selectedDate, List<Event> eventList) {
            mEventList = eventList;

            // Total number of pages (between min and max date)
            TOTAL_COUNT = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(mMaxDate.getTimeInMillis() - mMinDate.getTimeInMillis()));

            int initialPosition = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(selectedDate.getTimeInMillis() - mMinDate.getTimeInMillis()));

            initialPageAndDay = new Pair<>(initialPosition, selectedDate);

            setPageAndDay = initialPageAndDay;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {

            final Calendar day = (Calendar) initialPageAndDay.second.clone();
            day.add(Calendar.DAY_OF_MONTH, position - initialPageAndDay.first);

            LayoutInflater inflater = LayoutInflater.from(collection.getContext());
            View view = inflater.inflate(R.layout.pager_calendar_day, collection, false);
            view.setTag(position);

            TextView tvDay = view.findViewById(R.id.tv_calendar_day);
            TextView tvDayOfWeek = view.findViewById(R.id.tv_calendar_day_of_week);
            RecyclerView rvDay = view.findViewById(R.id.rv_calendar_events);
            View rlNoAlerts = view.findViewById(R.id.rl_no_events);
            View fabCreate = view.findViewById(R.id.fab_create_event);

            List<Event> eventList = getCalendarEventsOfDay(day);

            if (diffYMD(day, sToday) == -1) {
                fabCreate.setVisibility(View.INVISIBLE);
                fabCreate.setOnClickListener(null);
            }
            else {
                fabCreate.setVisibility(View.VISIBLE);
                fabCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onCreateEvent(day);
                    }
                });
            }

            tvDay.setText(new SimpleDateFormat("d", Locale.KOREA).format(day.getTime()));
            tvDayOfWeek.setText(new SimpleDateFormat("EEEE", Locale.KOREA).format(day.getTime()));

            rvDay.setLayoutManager(new LinearLayoutManager(collection.getContext(), LinearLayoutManager.VERTICAL, false));
            rvDay.setAdapter(new CalendarEventAdapter(eventList));
            rvDay.setVisibility(eventList.size() == 0? View.GONE : View.VISIBLE);

            rlNoAlerts.setVisibility(eventList.size() == 0? View.VISIBLE : View.GONE);

            collection.addView(view);

            return new ViewHolder(view);
        }

        @Override
        public int getCount() {
            return TOTAL_COUNT;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            final ViewHolder holder = (ViewHolder) object;
            return view == holder.container;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(((ViewHolder) object).container);
        }

        private void setSelectedDate(Calendar selectedDate) {
            int position = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(selectedDate.getTimeInMillis() - mMinDate.getTimeInMillis()));

            initialPageAndDay = new Pair<>(position, selectedDate);
        }

        private class ViewHolder {
            final View container;

            ViewHolder(View container) {
                this.container = container;
            }
        }

        private List<Event> getCalendarEventsOfDay(Calendar day) {
            List<Event> eventList = new ArrayList<>();
            for (Event e : mEventList) {
                if (diffYMD(e.getDate(), day) == 0)
                    eventList.add(e);
            }
            return eventList;
        }

        private Calendar getCalendarObjectForLocale(String date, Locale locale) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), locale);
            DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy", locale);
            if (date == null || date.isEmpty()) {
                return calendar;
            }

            try {
                final Date parsedDate = DATE_FORMATTER.parse(date);
                if (calendar == null)
                    calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), locale);
                calendar.setTime(parsedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return calendar;
        }

    }

    private class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>{

        private final List<Event> mCalendarEvents;

        CalendarEventAdapter(List<Event> events) {
            mCalendarEvents = events;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater vi = LayoutInflater.from(parent.getContext());
            View v = vi.inflate(R.layout.list_item_calendar_event, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Event event = mCalendarEvents.get(position);

            String defaultTitle = holder.itemView.getContext().getString(R.string.event_default_title);
            String title = event.getTitle() == null ? defaultTitle : event.getTitle();

            holder.tvEventName.setText(title);
            //holder.rclEventIcon.setBackgroundColor(event.getColor());
            holder.tvEventStatus.setText(timeFormat.format(event.getDate().getTime()));

            GradientDrawable mGradientDrawable = (GradientDrawable) holder.rclEventIcon.getBackground();
            mGradientDrawable.setStroke(10, event.getColor());
            mGradientDrawable.setColor(event.getColor());

            Typeface typeFace = ResourcesCompat.getFont(mContext, R.font.mapo_flower_island);
            holder.tvEventName.setTypeface(typeFace);
            holder.tvEventStatus.setTypeface(typeFace);
        }

        @Override
        public int getItemCount() {
            return mCalendarEvents.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            View rclEventIcon;
            TextView tvEventName;
            TextView tvEventStatus;
            Button btn_delete;

            ViewHolder(View view) {
                super(view);
                rclEventIcon = view.findViewById(R.id.rcl_calendar_event_icon);
                tvEventName = view.findViewById(R.id.tv_calendar_event_name);
                tvEventStatus = view.findViewById(R.id.tv_calendar_event_status);
                btn_delete = view.findViewById(R.id.btn_event_list_delete);
                view.setOnClickListener(this);

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(mDelListener!=null){
                            mDelListener.onDelete(view, mCalendarEvents.get(getAdapterPosition()));

                        }
                    }
                });
            }

            @Override
            public void onClick(View v) {

                if (mListener != null){
                    //setEventList = mViewPagerAdapter.getCalendarEventsOfDay(mSelectedDate);
                    //int year = mCalendarEvents.get(getAdapterPosition()).getDate().getTime().getYear();
                    //int month = mCalendarEvents.get(getAdapterPosition()).getDate().getTime().getMonth();
                    //int sec = mCalendarEvents.get(getAdapterPosition()).getDate().getTime().getSeconds();

                    mListener.onEventClick(mCalendarEvents.get(getAdapterPosition()));


                }
            }
        }
    }

    public interface OnCalendarDialogListener {
        void onEventClick(Event event);
        void onCreateEvent(Calendar calendar);
    }

    public interface OnCalendarDialogDeleteListener{
        void onDelete(View view, Event event);
    }

    private static int diffYMD(Calendar date1, Calendar date2) { // data1 : 비교대상 , data2 : 오늘
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH))
            return 0;

        return date1.before(date2) ? -1 : 1;
    }

    public static class Builder  {

        private final CalendarDialogParams P;

        public static Builder instance(Context context) {
            return new Builder(context);
        }

        private Builder(Context context) {
            P = new CalendarDialogParams(context);
        }

        public Builder setEventList(List<Event> calendarEventList) {
            P.mEventList = calendarEventList;
            return this;
        }

        public Builder setSelectedDate(Calendar selectedDate) {
            P.mSelectedDate = selectedDate;
            return this;
        }

        public Builder setOnItemClickListener(OnCalendarDialogListener listener) {
            P.mOnCalendarDialogListener = listener;
            return this;
        }
        public Builder setOnDeleteClick(OnCalendarDialogDeleteListener listener){
            P.mOnCalendarDialogDeleteListener = listener;
            return this;
        }

        public CalendarDialog create() {
            CalendarDialog calendarDialog = new CalendarDialog(P.mContext);

            P.apply(calendarDialog);

            return calendarDialog;
        }
    }

    private static class CalendarDialogParams {

        Context mContext;

        Calendar mSelectedDate = sToday;
        List<Event> mEventList = new ArrayList<>();

        OnCalendarDialogListener mOnCalendarDialogListener;

        OnCalendarDialogDeleteListener mOnCalendarDialogDeleteListener;

        CalendarDialogParams(Context context) {
            mContext = context;
        }

        void apply(CalendarDialog calendarDialog) {
            calendarDialog.setSelectedDate(mSelectedDate);
            calendarDialog.setEventList(mEventList);
            calendarDialog.setOnCalendarDialogListener(mOnCalendarDialogListener);
            calendarDialog.setOnCalendarDialogDeleteListener(mOnCalendarDialogDeleteListener);
        }
    }
}
