package com.pastel.dalpook.PushService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class PushManager {

    public void setPush(Context context, String hour, String min, String sec, String cont){
        Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent.putExtra("hour", hour);
        mAlarmIntent.putExtra("min", min);
        mAlarmIntent.putExtra("sec", sec);
        mAlarmIntent.putExtra("cont", cont);

        // id = hhmmss
        int id = Integer.parseInt(hour+min+sec);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, Integer.parseInt(sec));
        long time = calendar.getTimeInMillis();

        manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public void canclePush(Context context, String hour, String min, String sec){
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
        int id = Integer.parseInt(hour+min+sec);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(sender !=null){
            manager.cancel(sender);
            sender.cancel();
        }
    }
}
