package com.example.implicitintentdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var implIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.camBtn).setOnClickListener {
            startActivity(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        findViewById<Button>(R.id.phnBokBtn).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply{
                data = Uri.parse("content://contacts/people/")
            })
        }

        findViewById<Button>(R.id.opnDialer).setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL).apply{
                data = Uri.parse("tel:8008129196")
            })
        }
    }
}