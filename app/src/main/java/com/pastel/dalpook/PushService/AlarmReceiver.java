package com.pastel.dalpook.PushService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mServiceintent = new Intent(context, AlarmService.class);
        mServiceintent.putExtra("hour", intent.getStringExtra("hour"));
        mServiceintent.putExtra("min", intent.getStringExtra("min"));
        mServiceintent.putExtra("sec", intent.getStringExtra("sec"));
        mServiceintent.putExtra("cont", intent.getStringExtra("cont"));

        context.startService(mServiceintent);
    }
}
