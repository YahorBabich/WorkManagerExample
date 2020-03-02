package com.example.workmanagerexample

import android.app.Application
import androidx.work.Configuration

class WMApplication : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setMinimumLoggingLevel(android.util.Log.DEBUG).build()
}