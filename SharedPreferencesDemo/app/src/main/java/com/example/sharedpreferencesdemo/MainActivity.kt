package com.example.sharedpreferencesdemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var nameView: EditText
    private lateinit var msgView: EditText

    private lateinit var sp: SharedPreferences

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SaveData")

    private val NAME_KEY = stringPreferencesKey("name")
    private val MSG_KEY = stringPreferencesKey("msg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nameView = findViewById(R.id.nameView)
        msgView = findViewById(R.id.msgView)

        val saveDraftBtn = findViewById<Button>(R.id.saveDraftBtn)
        val retrieveDaftBtn = findViewById<Button>(R.id.retrieveDraftBtn)

        saveDraftBtn.setOnClickListener {
            saveDraft()
        }

        retrieveDaftBtn.setOnClickListener {
            retriveDraft()
        }
    }

    override fun onStop() {
        super.onStop()
        //saveDraft()
        lifecycleScope.launch {
            saveData(nameView.getText().toString(),
                msgView.getText().toString())
        }
    }

    override fun onStart(){
        super.onStart()
        //retriveDraft()
        retrieveData()
    }

    private fun saveDraft(){
        sp = this.getSharedPreferences("SavedData", Context.MODE_PRIVATE)

        val editor  = sp.edit()

        val name = nameView.getText().toString()
        val msg = msgView.getText().toString()

        editor.putString("Name", name)
        editor.putString("Message", msg)

        editor.apply()
    }

    private fun retriveDraft(){
        sp = this.getSharedPreferences("SavedData", Context.MODE_PRIVATE)
        val name = sp.getString("Name", "")
        val msg = sp.getString("Message", "")
        nameView.setText(name)
        msgView.setText(msg)
    }

    private suspend fun saveData(name: String, msg: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[MSG_KEY] = msg
        }
    }

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