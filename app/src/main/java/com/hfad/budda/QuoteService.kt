package com.hfad.budda

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlin.math.floor


class QuoteService: Service() {

    private fun createNotification(quote: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_MAX
            )
            notificationChannel.description = "Quote Channel"
            notificationChannel.enableLights(true)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val resultIntent = Intent(this, FullQuoteActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("quote",quote)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.buddha)
            .setContentTitle(quote)
            .setContentText("Tap to open the quote")
            .setContentIntent(resultPendingIntent)
        notificationManager!!.notify(1, notificationBuilder.build())

    }

    companion object {
        var timer: CountDownTimer? = null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val quotes = intent?.getStringExtra("quotes")?.split("\n")
        val firstTime = intent?.getIntExtra("firstTime",1)
        val secondTime = intent?.getIntExtra("secondTime",1)
        val ids = intent?.getIntArrayExtra("ids")
        val interval = intent?.getIntExtra("interval",1)
        val countDownInterval: Double = if(firstTime!! < secondTime!!)
            ((secondTime - firstTime) / interval!!).toDouble()
        else
            ((24*60*60*1000 - (firstTime - secondTime)) / interval!!).toDouble()

            timer = object : CountDownTimer(Long.MAX_VALUE, floor(countDownInterval).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                val rightNow = Calendar.getInstance()
                val offset = (rightNow[Calendar.ZONE_OFFSET] +
                        rightNow[Calendar.DST_OFFSET]).toLong()
                val sinceMid: Long = (rightNow.timeInMillis + offset) %
                        (24 * 60 * 60 * 1000)
                if (millisUntilFinished <= Long.MAX_VALUE - countDownInterval)
                    if(firstTime < secondTime) {
                        if (sinceMid in firstTime..secondTime) {
                            Log.d("nott", quotes!![ids!!.random()])
                            createNotification(quotes[ids.random()])
                        }
                    } else {
                        if (sinceMid < firstTime && sinceMid > secondTime)
                            createNotification(quotes!![ids!!.random()])
                    }
            }
            override fun onFinish() {
                Toast.makeText(applicationContext,"open app",Toast.LENGTH_SHORT).show()
            }
        }.start()
        Log.d("nott", "timer on")
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
        if (timer == null)
            Log.d("nott","timer canceled")
        else
            Log.d("nott","timer not canceled")
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

}