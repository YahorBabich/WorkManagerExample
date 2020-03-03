package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.workmanagerexample.NotificationWorker.Companion.NOTIFICATION_ID
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)

        start.setOnClickListener(this@MainActivity)
        stop.setOnClickListener(this@MainActivity)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.stop -> {
                Log.d("WorkManagerrrrrr", "stop")
                workManager.cancelAllWork()
            }
            R.id.start -> {
                Log.d("WorkManagerrrrrr", "start")
                val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
                periodicWorkRequest = PeriodicWorkRequest.Builder(
                    NotificationWorker::class.java, 15, TimeUnit.MINUTES
                ).setInitialDelay(10, TimeUnit.SECONDS).setInputData(data).build()
                workManager.enqueueUniquePeriodicWork(
                    "INIQUE_WORK_NAME",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWorkRequest
                )

                /* val instanceWorkManager = WorkManager.getInstance(this)
                 instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()*/

            }
        }
    }
}
