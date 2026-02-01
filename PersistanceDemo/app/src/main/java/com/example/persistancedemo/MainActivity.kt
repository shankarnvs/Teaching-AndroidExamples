package com.example.persistancedemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.persistancedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var nameView: EditText
    private lateinit var msgView: EditText
    private lateinit var saveDraftBtn: Button
    private lateinit var retrieveDraftBtn: Button

    private lateinit var sp: SharedPreferences

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SaveData")

    private val NAME_KEY = stringPreferencesKey("name")
    private val MSG_KEY = stringPreferencesKey("msg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        nameView = findViewById(R.id.nameText)
        msgView = findViewById(R.id.msgText)
        saveDraftBtn = findViewById(R.id.saveDraft)
        retrieveDraftBtn = findViewById(R.id.retrieveDraft)

        saveDraftBtn.setOnClickListener {
            //saveData()
            lifecycleScope.launch {
                saveData(nameView.text.toString(), msgView.text.toString())
            }
        }

        retrieveDraftBtn.setOnClickListener {
            retrieveData()
        }

    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            saveData(nameView.text.toString(), msgView.text.toString())
            Log.d("PersistanceLog", "Data Saved")
        }
    }

    override fun onStart() {
        super.onStart()
        retrieveData()
        Log.d("PersistanceLog", "Data retrieved")
    }

    /*private fun saveData(){
        sp = this.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        val name = nameView.getText().toString()
        val msg = msgView.getText().toString()

        val editor = sp.edit()

        editor.putString("name", name)
        editor.putString("msg", msg)

        editor.apply()
    }*/

    private suspend fun saveData(name: String, msg: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[MSG_KEY] = msg
        }
    }

    /*private fun retrieveData(){
        sp = this.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        val name = sp.getString("name", "")
        val msg = sp.getString("msg", "")

        nameView.setText(name)
        msgView.setText(msg)
    }*/

    private fun retrieveData() {
        lifecycleScope.launch {
            dataStore.data.map { preferences ->
                Pair(
                    preferences[NAME_KEY] ?: "",
                    preferences[MSG_KEY] ?: ""
                )
            }.collect { (name, msg) ->
                nameView.setText(name)
                msgView.setText(msg)
            }
        }
    }
}