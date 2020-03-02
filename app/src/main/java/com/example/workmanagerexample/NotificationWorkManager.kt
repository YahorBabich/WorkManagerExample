package com.example.workmanagerexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class NotificationWorkManager(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date())
        Log.d("WorkManagerrrrrr", "show at : $timeStamp")
        val mBuilder =
            NotificationCompat.Builder(applicationContext, "default_notification_channel_id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("NotificationWorkManager")
                .setContentText("time: $timeStamp")
                .setTicker("message")
                .setAutoCancel(true)
                .setDefaults(0)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("default_notification_channel_id")
            val notificationChannel = NotificationChannel(
                "default_notification_channel_id",
                "AMChannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, mBuilder.build())
    }
}