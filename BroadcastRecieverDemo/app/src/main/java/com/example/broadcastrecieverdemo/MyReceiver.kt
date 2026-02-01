package com.example.broadcastrecieverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
       val current_state = intent!!.getBooleanExtra("state", false)
        if (current_state){
            Toast.makeText(context, "Airplane mode activated", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "Airplane mode deactivated", Toast.LENGTH_LONG).show()
        }
    }
}