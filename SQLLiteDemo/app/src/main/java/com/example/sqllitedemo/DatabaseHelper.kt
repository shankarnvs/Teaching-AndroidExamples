package com.example.sqllitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(
    context: Context?
) : SQLiteOpenHelper(context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDB.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_AGE INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert data
    fun insertUser(name: String, age: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L // Return true if inserted successfully
    }

    // Retrieve all users
    fun getAllUsers(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    // Update user
    fun updateUser(id: Int, name: String, age: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        return result > 0
    }

    // Delete user
    fun deleteUser(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        return result > 0
    }

    fun checkDuplicate(username: String): Boolean {
        val db = this.writableDatabase  // Step 1

        // Step 2: Check if username already exists in the database
        val cursor = db.query(
            TABLE_NAME,            // Table name
            arrayOf(COLUMN_ID),    // Columns to select (just checking ID, can select more if needed)
            "$COLUMN_NAME = ?", // WHERE clause to check for the username
            arrayOf(username),     // Bind the username to the WHERE clause
            null,                  // No GROUP BY clause
            null,                  // No HAVING clause
            null                   // No ORDER BY clause
        )
        var returnVal = true
        // Step 3: If cursor has data, it means the username already exists
        if (cursor.moveToFirst()) {
            returnVal = false // Username already exists, return false
        }
        cursor.close()

        return returnVal
    }

    fun isTableEmpty(): Boolean {
        val db = this.readableDatabase  // Get readable database

        // Use the table name directly in the query string
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)

        cursor.moveToFirst()

        val count = cursor.getInt(0)  // Get the count from the result

        cursor.close()  // Always close the cursor
        return count == 0  // If count is 0, the table is empty
    }

    fun clearTable() {
        val db = this.writableDatabase  // Get writable database

        // Execute a DELETE statement to remove all rows from the table
        // db.execSQL("DELETE FROM $TABLE_NAME")

        // Alternatively, you can use db.delete() with a null selection to delete all rows
        db.delete(TABLE_NAME, null, null)
    }
}