package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)
        periodicWorkRequest = PeriodicWorkRequest.Builder(
            NotificationWorkManager::class.java, 15, TimeUnit.MINUTES
        ).setInitialDelay(20, TimeUnit.SECONDS).build()

        start.setOnClickListener(this@MainActivity)
        stop.setOnClickListener(this@MainActivity)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.stop -> {
                workManager.cancelAllWork()
            }
            R.id.start -> {
                val task = OneTimeWorkRequest.Builder(NotificationWorkManager::class.java).build()
                workManager.enqueue(task)
            }
        }
    }
}
