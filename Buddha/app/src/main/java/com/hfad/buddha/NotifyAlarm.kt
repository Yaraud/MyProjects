package com.hfad.buddha

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.File

class NotifyAlarm: BroadcastReceiver() {
    private fun createNotification(quote: String,context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_ID,
                "MyNotifications",
                NotificationManager.IMPORTANCE_MAX
            )
            notificationChannel.description = "Quote Channel"
            notificationChannel.enableLights(true)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val resultIntent = Intent(context, FullQuoteActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("quote",quote)
        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val snoozeIntent = Intent(context, NotifyAlarm::class.java)
            snoozeIntent.putExtra("quote", "888")
        val nextIntent = PendingIntent.getBroadcast(context,0, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.buddha)
            .setContentTitle(quote)
            .setContentText("Tap to open the quote")
            .setContentIntent(resultPendingIntent)
            .addAction(R.drawable.baseline_navigate_next_black_20,"Next",nextIntent)
        notificationManager!!.notify(1, notificationBuilder.build())

    }
    override fun onReceive(context: Context, intent: Intent?) {
        val quote: String
        val path = context.filesDir
        val file = File(path, "quotes_file.txt")
        val quotes = file.readText().split("\n")
        val ids = file.readText().split("\n").size - 1
        quote = if (ids != 0)
            quotes[(0..ids).random()]
        else
            "No any quote"
        createNotification(quote,context)
    }
}