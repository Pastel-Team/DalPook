package com.pastel.dalpook.Utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pastel.dalpook.R;
import com.pastel.dalpook.data.CalModels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalListAdapter extends RecyclerView.Adapter<CalListAdapter.ViewHolder>{

    private List<CalModels> mTimeList;
    private Context mContext;
    private RecyclerView mRecyclerV;
    private ArrayList<CalModels> mData = null ;

    private OnItemClickListener mListener;
    private ViewHolder mHolder;

    public interface OnItemClickListener{ // 인터페이스 정의
        void onItemClick(View v, int pos, String getFlag);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public View view_color;
        public TextView textView;
        public View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            //personNameTxtV = (TextView) v.findViewById(R.id.name);
            //personAgeTxtV = (TextView) v.findViewById(R.id.age);
            //personOccupationTxtV = (TextView) v.findViewById(R.id.occupation);
            textView = (TextView) itemView.findViewById(R.id.txt_time_cal);
            view_color = (View) itemView.findViewById(R.id.view_cal_today_color);
        }
    }

    public void addItem(int position, String time, int color, String flag) {

        CalModels models = new CalModels(time, color, flag);

        mTimeList.add(position, models);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mTimeList.size());
        notifyDataSetChanged();

    }

    public void removeItem(int position){
        mTimeList.remove(position);
        notifyDataSetChanged();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CalListAdapter(List<CalModels> myDataset, Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        mTimeList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
        this.mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CalListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.list_item_cal, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        this.mHolder = holder;

        final CalModels models = mTimeList.get(position);

        String time = models.getTime();
        String[] splTime = time.split(":");

        String hour = splTime[0];
        String minute = splTime[1];

        Calendar setCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        setCal.setTime(new Date());
        setCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        setCal.set(Calendar.MINUTE, Integer.parseInt(minute));

        String setTime = "";

        int isAMorPM = setCal.get(Calendar.AM_PM);

        int Hour = setCal.get(Calendar.HOUR);
        String AMHour ="";
        if(Hour >= 0 && Hour < 10){
            AMHour = "0"+setCal.get(Calendar.HOUR);
            if(AMHour.equals("00")){
                AMHour = "12";
            }
        }else{
            AMHour = String.valueOf(setCal.get(Calendar.HOUR));
        }

        int Minute = setCal.get(Calendar.MINUTE);
        String AMMinute ="";
        if(Minute >= 0 && Minute < 10){
            AMMinute = "0"+ setCal.get(Calendar.MINUTE);
        }else{
            AMMinute = String.valueOf(setCal.get(Calendar.MINUTE));
        }
        switch (isAMorPM){
            case Calendar.AM :
                setTime = "오전 " + AMHour + ":" + AMMinute;
                break;
            case Calendar.PM :
                setTime = "오후 " + AMHour + ":" + AMMinute;
                break;
        }

        holder.textView.setText(setTime);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.textView, position, models.getFlag());
            }
        });

        GradientDrawable mGradientDrawable = (GradientDrawable) holder.view_color.getBackground();
        mGradientDrawable.setStroke(10, models.getColor());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

}