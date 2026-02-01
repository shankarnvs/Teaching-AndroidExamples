package com.example.servicedemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.Timer
import java.util.TimerTask

class DemoService : Service() {

    private var timer = Timer()
    private var counter = 0
    private var isStarted = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isStarted){
            Log.d("Service Log", "Service already running")
        }else {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    counter++
                    Log.d("Service Log", "$counter sec passed")
                }
            }, 0, 1000)
            isStarted = true
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        Log.d("Service Log", "Service Stopped")
    }

}