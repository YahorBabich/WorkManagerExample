package com.example.workmanagerexample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val TAG = "TAGGGG"
        const val PREFERENCE_NAME = "PREFERENCE_NAME"
        const val PERIODIC_WORK_REQUEST = "PERIODIC_WORK_REQUEST"
    }

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private lateinit var preference: SharedPreferences

    private val adapter = StatusAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        workManager = WorkManager.getInstance(this)
        preference = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        getWorkInfo()

        start.isEnabled = false
        stop.isEnabled = false
        start.setOnClickListener(this@MainActivity)
        stop.setOnClickListener(this@MainActivity)
    }

    private fun getWorkInfo() {
        val json: String? = preference.getString(PERIODIC_WORK_REQUEST, "")
        val id: UUID? = Gson().fromJson(json, UUID::class.java)

        if (id != null) {
            workManager.getWorkInfoByIdLiveData(id)
                .observe(this, Observer { workInfo ->
                    adapter.update("wm ${workInfo?.state}")
                    when (workInfo?.state) {
                        WorkInfo.State.ENQUEUED,
                        WorkInfo.State.RUNNING -> {
                            stop.isEnabled = true
                        }
                        else -> {
                            start.isEnabled = true
                        }
                    }
                })
        } else {
            adapter.update("wm empty")
        }
/*        workManager.getWorkInfosByTagLiveData(TAG).observe(this, Observer { observers ->
            if (observers.isNotEmpty()) {
                adapter.update("not empty observers ${observers[0].state}")
            } else {
                adapter.update("empty observers")
            }
        })*/
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.stop -> {
                adapter.update("stop")
                workManager.cancelAllWork()
                preference.edit().remove(PERIODIC_WORK_REQUEST).apply()
            }
            R.id.start -> {
                adapter.update("start")
                periodicWorkRequest = PeriodicWorkRequest.Builder(
                    NotificationWorkManager::class.java, 15, TimeUnit.MINUTES
                ).setInitialDelay(20, TimeUnit.SECONDS).build()
                workManager.enqueueUniquePeriodicWork(
                    TAG,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWorkRequest
                )

                val json: String = Gson().toJson(periodicWorkRequest.id)
                preference.edit().putString(PERIODIC_WORK_REQUEST, json).apply()
                getWorkInfo()
            }
        }
    }
}