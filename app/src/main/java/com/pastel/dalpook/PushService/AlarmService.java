package com.pastel.dalpook.PushService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.pastel.dalpook.MainActivity;
import com.pastel.dalpook.R;

import org.mozilla.javascript.tools.jsc.Main;

public class AlarmService extends Service {
    private int REQUEST_CODE = 1;
    public AlarmService(){

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public int onStartCommand(Intent intent, int flags, int startId){
        String hour = intent.getStringExtra("hour");
        String min = intent.getStringExtra("min");
        String sec = intent.getStringExtra("sec");
        String cont = intent.getStringExtra("cont");

        Intent mMainintent = new Intent(this, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, mMainintent, PendingIntent.FLAG_UPDATE_CURRENT);

        //푸시알람 설정
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_blackmoon)
                .setContentTitle("금일 " +hour+"시 " +min+"분에 일정이 있습니다.")
                .setContentIntent(mPendingIntent)
                .setContentText(cont)
                .setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification noti = mBuilder.build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        //id와 푸시 알림 관련 객체를 인수로 넘김 --> 여기서 id는 알리미매니저의 id와 동일?
        //id -> hhmmss
        mNotifyMgr.notify(Integer.valueOf(hour+min+sec), noti);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
