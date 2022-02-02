package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.BuildConfig;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MainActivity;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.NotificationRelease;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MainActivity.FIRSTRUN_PREF;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MainActivity.ISFIRSTRUN;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_TYPE = "extra_type";
    public static final String DAILYREMINDER = "daily_reminder";
    public static final String RELEASEREMINDER = "release_reminder";
    private static final int ID_DAILY = 301;
    private static final int ID_RELEASE = 302;
    private static final int MAX = 3;
    private static final int NOTIFID = 0;
    public static final String CHANNEL_ID_DAILY = "daily_notification";
    public static CharSequence CHANNEL_NAME_DAILY = "Daily Notification";
    public static CharSequence CHANNEL_NAME_RELEASE = "Released Notification";
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    private static final String GROUP_KEY_RELEASED = "group_key_released";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type =intent.getStringExtra(EXTRA_TYPE);
        int requestCode = type.equalsIgnoreCase(DAILYREMINDER) ? ID_DAILY : ID_RELEASE;

        if (requestCode == ID_DAILY) {
            showNotifDailyReminder(ID_DAILY,context);
        }else{
            showNotifReleaseReminder(NOTIFID,context);
        }

    }

    private void showNotifReleaseReminder(final int notifId, final Context context) {
        final ArrayList<NotificationRelease> notif = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+ BuildConfig.TMDB_API_KEY+"&language="+context.getResources().getString(R.string.lang);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    int niD = 0;
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray responseArray = responseObject.getJSONArray("results");
                    Boolean notYet = false;
                    NotificationRelease notifModel;
                    for (int i = 0;i<responseArray.length();i++){
                        notifModel = new NotificationRelease();
                        String date = responseArray.getJSONObject(i).getString("release_date");
                        String dateArray[] = date.split("-");
                        Calendar calendar = Calendar.getInstance();
                        int caldays = calendar.get(Calendar.DATE);
                        int calmonth = calendar.get(Calendar.MONTH)+1;
                        int calyear = calendar.get(Calendar.YEAR);

                        int years = Integer.parseInt(dateArray[0]);
                        int month = Integer.parseInt(dateArray[1]);
                        int days = Integer.parseInt(dateArray[2]);

                        if ((caldays == days) && (calmonth == month) && (calyear == years)) {
                            notifModel.setTitle(responseArray.getJSONObject(i).getString("title"));
                            notifModel.setId(responseArray.getJSONObject(i).getInt("id"));
                            notifModel.setRelease(context.getResources().getString(R.string.releasedToday));

                            notif.add(notifModel);
                            Log.d("LENGTH1", String.valueOf(notif.size()));
                            sendNotif(niD);
                            niD++;
                        }else{
                            notYet = true;
                        }
                    }
                    if (notYet == true){
                        notifModel = new NotificationRelease();
                        notifModel.setTitle(context.getResources().getString(R.string.ops));
                        notifModel.setId(0);
                        notifModel.setRelease(context.getResources().getString(R.string.nothing));
                        notif.add(notifModel);
                        Log.d("LENGTH", String.valueOf(notif.size()));
                        sendNotif(niD);
                        niD++;
                        for (int i = 0;i<responseArray.length();i++){
                            notifModel = new NotificationRelease();
                            String date = responseArray.getJSONObject(i).getString("release_date");
                            String dateArray[] = date.split("-");
                            Calendar calendar = Calendar.getInstance();
                            int caldays = calendar.get(Calendar.DATE);
                            int calmonth = calendar.get(Calendar.MONTH)+1;
                            int calyear = calendar.get(Calendar.YEAR);

                            int years = Integer.parseInt(dateArray[0]);
                            int month = Integer.parseInt(dateArray[1]);
                            int days = Integer.parseInt(dateArray[2]);

                            if ((caldays <= days) && (calmonth == month) && (calyear == years)) {
                                int wait = days-caldays;
                                notifModel.setTitle(responseArray.getJSONObject(i).getString("title"));
                                notifModel.setId(responseArray.getJSONObject(i).getInt("id"));
                                notifModel.setRelease(context.getResources().getString(R.string.willReleaseOn)+wait+" "+context.getResources().getString(R.string.days));

                                notif.add(notifModel);

                                sendNotif(niD);
                                niD++;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            private void sendNotif(int idNotif) {

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_white);
                NotificationCompat.Builder mBuilder;
                String CHANNEL_ID = "channel_01";
                if (idNotif < MAX) {
                    mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setContentTitle(context.getResources().getString(R.string.catalogmovie))
                            .setContentText(notif.get(idNotif).getTitle()+"  "+notif.get(idNotif).getRelease())
                            .setSmallIcon(R.drawable.ic_notifications_white)
                            .setLargeIcon(largeIcon)
                            .setGroup(GROUP_KEY_RELEASED)
                            .setAutoCancel(true);
                } else {
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                            .addLine(notif.get(idNotif).getTitle()+" "+notif.get(idNotif).getRelease())
                            .addLine(notif.get(idNotif - 1).getTitle()+" "+notif.get(idNotif - 1).getRelease())
                            .setBigContentTitle(idNotif + context.getResources().getString(R.string.group_release))
                            .setSummaryText("mail@dicoding");
                    mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setContentTitle(idNotif + " new emails")
                            .setContentText("mail@dicoding.com")
                            .setSmallIcon(R.drawable.ic_notifications_white)
                            .setGroup(GROUP_KEY_RELEASED)
                            .setGroupSummary(true)
                            .setStyle(inboxStyle)
                            .setAutoCancel(true);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME_RELEASE, NotificationManager.IMPORTANCE_DEFAULT);
                    mBuilder.setChannelId(CHANNEL_ID);
                    if (mNotificationManager != null) {
                        mNotificationManager.createNotificationChannel(channel);
                    }
                }
                Notification notification = mBuilder.build();
                if (mNotificationManager != null) {
                    mNotificationManager.notify(idNotif, notification);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showNotifDailyReminder(int notifId, Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, CHANNEL_ID_DAILY)
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_notifications_white))
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text))
                .setSubText(context.getResources().getString(R.string.notification_subtext))
                .setAutoCancel(true);
        Notification notification = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DAILY,CHANNEL_NAME_DAILY,NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID_DAILY);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(notifId,notification);
        }

    }

    public void setDailyReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, DAILYREMINDER);

        //setting waktu
        int hour = 7;
        int minute = 0;
        int second = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        if (calendar.getTimeInMillis()<System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);

        if (alarmManager != null) {
            Log.d("TESTMILIS", "NOW"+System.currentTimeMillis()+" SCHEDULE"+String.valueOf(calendar.getTimeInMillis()));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        setFirstRun(context);
    }

    public void setReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, RELEASEREMINDER);

        //setting waktu
        int hour = 8;
        int minute = 0;
        int second = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

      if (calendar.getTimeInMillis()<System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        if (alarmManager != null) {
            Log.d("alarmmeneger","notnull");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        setFirstRun(context);
    }
    private void setFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIRSTRUN_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(ISFIRSTRUN,true) == true) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ISFIRSTRUN, false);
            editor.apply();
        }
    }
    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(DAILYREMINDER) ? ID_DAILY : ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }
}
