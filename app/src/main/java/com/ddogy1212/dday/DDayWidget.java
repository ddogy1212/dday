package com.ddogy1212.dday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class DDayWidget extends AppWidgetProvider {
    private static final String PREF = "dday_settings";
    private static final String ALARM_ACTION = "com.ddogy1212.dday.ACTION_REFRESH_DATE";

    public static String displayText(Context context) {
        SharedPreferences p = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String name = p.getString("name", "나예💕").trim();
        if (name.isEmpty()) name = "나예💕";
        String date = p.getString("date", "");
        if (date.isEmpty()) return name + " · 날짜 설정";
        try {
            long days = ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.now());
            String dday = days > 0 ? "D+" + days : (days < 0 ? "D" + days : "D-DAY");
            return name + " · " + dday;
        } catch (RuntimeException ex) {
            return name + " · 날짜 설정";
        }
    }

    public static void refresh(Context context) {
        AppWidgetManager m = AppWidgetManager.getInstance(context);
        int[] ids = m.getAppWidgetIds(new ComponentName(context, DDayWidget.class));
        updateWidgets(context, m, ids);
        scheduleNextDay(context);
    }

    private static void updateWidgets(Context context, AppWidgetManager m, int[] ids) {
        SharedPreferences p = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String display = displayText(context);
        int color = p.getInt("color", Color.rgb(255, 145, 194));
        for (int id : ids) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_dday);
            views.setTextViewText(R.id.dday_text, display);
            views.setTextColor(R.id.dday_text, color);
            Intent open = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, open,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.dday_text, pi);
            m.updateAppWidget(id, views);
        }
    }

    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        updateWidgets(context, manager, ids);
        scheduleNextDay(context);
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String a = intent.getAction();
        if (a != null && !AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(a)) {
            refresh(context);
        }
    }

    private static void scheduleNextDay(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null) return;
        Intent intent = new Intent(context, DDayWidget.class).setAction(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 101, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        long next = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli() + 60_000L;
        alarm.setAndAllowWhileIdle(AlarmManager.RTC, next, pi);
    }
}
