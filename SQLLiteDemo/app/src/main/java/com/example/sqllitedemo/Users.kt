package com.example.sqllitedemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-incremented primary key
    val name: String,
    val email: String
)