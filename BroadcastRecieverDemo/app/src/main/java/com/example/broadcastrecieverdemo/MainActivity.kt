package com.example.broadcastrecieverdemo

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    val mr = MyReceiver()
    val i_f = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        i_f.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(mr, i_f)
        Log.d("BrodcastReceiverLog", "onStart() funciton called")

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mr)
        Log.d("BrodcastReceiverLog", "onStop() funciton called")
    }

}