package com.example.dell.app9;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by DELL on 11-03-2018.
 */


//for Request Accepted Notification

public class NotificationHelper extends ContextWrapper {
    private static final String CHANNEL_ID = "pilotzaichannel";
    private static final String CHANNEL_NAME = "Pilotzai";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel pilotChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            pilotChannel.enableLights(true);
            pilotChannel.enableVibration(true);
            pilotChannel.setLightColor(Color.GREEN);
            pilotChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(pilotChannel);
        }
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getPilotChannelNotification(String loggedUser) {
        Intent resultIntent = new Intent(this, displayDetails.class);
        resultIntent.putExtra("data", loggedUser);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText("Request accepted!")
                .setContentTitle("PilotZai")
                .setSmallIcon(R.drawable.ic_cutmypic__1_)
                .setAutoCancel(true)
                .setAutoCancel(true).setSound(alarmSound).setOnlyAlertOnce(true)
                .setContentIntent(resultPendingIntent);
    }

}