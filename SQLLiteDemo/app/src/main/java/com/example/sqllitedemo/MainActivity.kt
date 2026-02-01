package com.example.sqllitedemo

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var saveButton: Button
    private lateinit var userList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        saveButton = findViewById(R.id.saveButton)
        userList = findViewById(R.id.userList)

        if(!dbHelper.isTableEmpty()){
            dbHelper.clearTable()
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull()

            if (name.isNotEmpty() && age != null) {
                if (dbHelper.checkDuplicate(name)) {
                    if (dbHelper.insertUser(name, age)) {
                        Toast.makeText(this, "User saved!", Toast.LENGTH_SHORT).show()
                        displayUsers()
                    } else {
                        Toast.makeText(this, "Error saving user", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Name exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter valid data!", Toast.LENGTH_SHORT).show()
            }
        }

        displayUsers()
    }

    private fun displayUsers() {
        val cursor: Cursor = dbHelper.getAllUsers()
        val users = StringBuilder()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val age = cursor.getInt(2)
            users.append("ID: $id, Name: $name, Age: $age\n")
        }
        userList.text = users.toString()
        cursor.close()
    }

}