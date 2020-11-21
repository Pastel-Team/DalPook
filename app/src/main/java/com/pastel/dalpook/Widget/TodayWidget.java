package com.pastel.dalpook.Widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.pastel.dalpook.MainActivity;
import com.pastel.dalpook.R;

import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class TodayWidget extends AppWidgetProvider {

    //public static String EXTRA_WORD = "_THE_WORD";
    private static final int WIDGET_UPDATE_INTERVAL = 5000;
    //private static PendingIntent mSender;
    //private static AlarmManager mManager;


    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
/*
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.today_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

 */
        RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.today_widget);

        Intent svcIntent = new Intent(context, MyRemoteViewsService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        widgetViews.setRemoteAdapter(R.id.lv_widget, svcIntent);

        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent mSender = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        widgetViews.setPendingIntentTemplate(R.id.lv_widget, mSender);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget);
        appWidgetManager.updateAppWidget(appWidgetId, widgetViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName myWidget = new ComponentName(context.getPackageName(), TodayWidget.class.getName());
        int[] widgetIds = appWidgetManager.getAppWidgetIds(myWidget);

        String action = intent.getAction();

        //업데이트 액션이 들어오면
        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            if(widgetIds != null && widgetIds.length > 0){
                this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds); // onUpdate 호출
            }
        }

    }
}