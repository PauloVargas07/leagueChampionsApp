package com.example.leaguechampions.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.leaguechampions.R

@SuppressLint("MissingPermission")
fun sendTeamNotification(context: Context) {
    val channelId = "team_notification_channel"
    val channelName = "Team Notifications"
    val channelDescription = "Notifications when teams are created"

    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, channelName, importance).apply {
        description = channelDescription
    }
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)

    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Teams Created")
        .setContentText("The teams have been drawn. Check them out!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(1234, notificationBuilder.build())
    }
}
