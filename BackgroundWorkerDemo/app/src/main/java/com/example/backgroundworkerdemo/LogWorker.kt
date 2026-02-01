package com.example.backgroundworkerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class LogWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString("log_message") ?: "Default log message"
        Log.d("LogWorker", "Periodic Task: $message")
        return Result.success()
    }
}