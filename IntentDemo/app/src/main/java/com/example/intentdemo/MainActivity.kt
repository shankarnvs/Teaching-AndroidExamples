package com.example.intentdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var name_text: EditText
    private lateinit var age_text: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name_text = findViewById(R.id.nameText)
        age_text = findViewById(R.id.ageText)

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val di = Intent(this, SecondActivity::class.java).apply{
                putExtra("name", name_text.getText().toString())
                putExtra("age", age_text.getText().toString().toIntOrNull())
            }
            startActivity(di)
        }

        findViewById<Button>(R.id.strService).setOnClickListener {
            Intent(this, demoService::class.java).also{
                startService(it)
            }
        }

        findViewById<Button>(R.id.stpService).setOnClickListener {
            Intent(this, demoService::class.java).also{
                stopService(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        name_text.setText("")
        age_text.setText("")
    }
}