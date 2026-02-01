package com.example.contentproviderdemoapp

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>  // Returns a List<User>

    @Query("SELECT * FROM users")
    fun getAllUsersCursor(): Cursor  // Returns a Cursor (Needed for ContentProvider)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdCursor(userId: Int): Cursor  // Returns Cursor for ContentProvider

    @Insert
    fun insertUser(user: User): Long

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    fun deleteUserById(userId: Int)

    @Update
    fun updateUser(user: User)
}