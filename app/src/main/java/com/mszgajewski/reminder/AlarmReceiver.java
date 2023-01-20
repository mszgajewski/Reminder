package com.mszgajewski.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context);

    }

        private void createNotification(Context context) {
        Intent tent = new Intent(context, HomeActivity.class);
        tent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, tent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"reminder")
                .setContentTitle("Tytuł")
                .setContentText("Much longer text that cannot fit one line... ")
                .setSmallIcon(R.drawable.ic_trash_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "reminder";
                String description = "Channel for Reminder App";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("reminder", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1,builder.build());
        }
    }
}
