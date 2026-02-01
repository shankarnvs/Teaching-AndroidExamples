package com.example.contentproviderdemoapp

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnInsert: Button
    private lateinit var btnDeleteSelected: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val selectedUsers = mutableSetOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        btnInsert = findViewById(R.id.btnInsert)
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(emptyList()) { user, isChecked ->
            if (isChecked) selectedUsers.add(user) else selectedUsers.remove(user)
        }
        recyclerView.adapter = userAdapter

        btnInsert.setOnClickListener {
            insertUser()
        }
        btnDeleteSelected.setOnClickListener {
            deleteSelectedUsers()
        }

        loadUsers()
    }

    private fun insertUser() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        if (username.isNotEmpty() && email.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val values = ContentValues().apply {
                    put("username", username)
                    put("email", email)
                }
                contentResolver.insert(Uri.parse("content://com.example.contentproviderdemoapp.provider/users"), values)
                runOnUiThread { loadUsers() }  // Refresh list on UI thread
            }
        }
    }

    private fun loadUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val uri = Uri.parse("content://com.example.contentproviderdemoapp.provider/users")
            val cursor = contentResolver.query(uri, null, null, null, null)
            val users = mutableListOf<User>()

            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val username = it.getString(it.getColumnIndexOrThrow("username"))
                    val email = it.getString(it.getColumnIndexOrThrow("email"))
                    users.add(User(id, username, email))
                }
            }

            runOnUiThread {
                userAdapter.updateUsers(users)  // Update RecyclerView on UI thread
            }
        }
    }

    private fun deleteSelectedUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            for (user in selectedUsers) {
                val uri = Uri.parse("content://com.example.contentproviderdemoapp.provider/users/${user.id}")
                contentResolver.delete(uri, null, null)
            }
            selectedUsers.clear()
            runOnUiThread { loadUsers() }  // Refresh list on UI thread
        }
    }

}