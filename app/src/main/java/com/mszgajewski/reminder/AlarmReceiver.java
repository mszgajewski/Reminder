package com.mszgajewski.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static final String title = "Alarm jednorazowy";
    public static final String EXTRA_MESSAGE = "message";

    private static final int notifyId = 100;
    private static final String CHANNEL_ID = "channel_notify_alarms";
    private static final CharSequence CHANNEL_NAME = "Alarm Channel";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra(EXTRA_MESSAGE);
        showNotification(context, title,message,notifyId);
        //setOneTimeAlarm(context, m);
    }

    private void showNotification(Context context, String title, String message, int notifyId) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_trash_24)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[] {1000,1000, 1000, 1000, 1000});
        builder.setChannelId(CHANNEL_ID);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = builder.build();
        if (notificationManager != null){
            notificationManager.notify(notifyId,notification);
        }
    }
    public void setOneTimeAlarm(Context context, String date, String time, String message) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Date");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        //intent.putExtra(EXTRA_TYPE,type);

        String[] dateArray = date.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(context, "Alarm jednorazowy ustawiony", Toast.LENGTH_SHORT).show();
    }
}
