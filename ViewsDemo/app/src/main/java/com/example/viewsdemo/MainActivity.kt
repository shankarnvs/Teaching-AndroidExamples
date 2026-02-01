package com.example.viewsdemo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(),  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val country_list_view = findViewById<ListView>(R.id.countryList)
        var country_list_array = resources.getStringArray(R.array.CountNames)
        val countries = ArrayAdapter(this, android.R.layout.simple_list_item_1, country_list_array)
        country_list_view.adapter = countries

        val pop = resources.getIntArray(R.array.Pop)
        val country_area  = resources.getIntArray(R.array.Ar)

        country_list_view.setOnItemClickListener { parent, view, position, l ->
            val country_selected = parent.getItemAtPosition(position).toString()
            val selected_country_population = pop.get(position)
            val selected_country_area = country_area.get(position)
            val msg_text = "$country_selected population is $selected_country_population\n & its area is $selected_country_area km\u00B2"
            Toast.makeText(this, msg_text, Toast.LENGTH_SHORT).show()
        }
    }
}