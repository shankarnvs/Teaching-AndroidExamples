package com.example.intentdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.Timer
import java.util.TimerTask

class demoService : Service() {

    private val tim = Timer()
    private var counter = 0
    private var isStarted = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(isStarted){
            Log.d("Service Log", "Service already running")
        }else{
            tim.schedule(object:TimerTask(){
                override fun run(){
                    Log.d("Service Log", "$counter sec passed since service started")
                    counter++
                }
            }, 0,1000)
            isStarted = true
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        tim.cancel()
        Log.d("Service Log", "Service stopped")
    }
}