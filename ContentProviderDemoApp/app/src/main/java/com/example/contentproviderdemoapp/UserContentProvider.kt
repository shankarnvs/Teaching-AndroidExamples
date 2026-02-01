package com.example.contentproviderdemoapp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class UserContentProvider : ContentProvider() {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDAO

    companion object {
        const val AUTHORITY = "com.example.contentproviderdemoapp.provider"
        const val TABLE_NAME = "users"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
            //content://com.example.contentproviderdemoapp.provider/users
        private const val USERS = 1
        private const val USER_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, USERS)
            addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        database = AppDatabase.getDatabase(context!!)
        userDao = database.userDAO()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor = when (uriMatcher.match(uri)) {
            USERS -> userDao.getAllUsersCursor()
            USER_ID -> {
                val id = uri.lastPathSegment!!.toInt()
                userDao.getUserByIdCursor(id)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            USERS -> {
                val user = User(
                    id = 0,
                    username = values?.getAsString("username") ?: "",
                    email = values?.getAsString("email") ?: ""
                )
                val rowId = userDao.insertUser(user)
                return Uri.withAppendedPath(CONTENT_URI, rowId.toString())
            }
            else -> throw IllegalArgumentException("Invalid URI: $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            USER_ID -> {
                val id = uri.lastPathSegment!!.toInt()
                val updatedUser = User(
                    id = id,
                    username = values?.getAsString("username") ?: "",
                    email = values?.getAsString("email") ?: ""
                )
                userDao.updateUser(updatedUser)
                1
            }
            else -> throw IllegalArgumentException("Invalid URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            USER_ID -> {
                val id = uri.lastPathSegment!!.toInt()
                userDao.deleteUserById(id)
                1
            }
            else -> throw IllegalArgumentException("Invalid URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            USERS -> "vnd.android.cursor.dir/$TABLE_NAME"
            USER_ID -> "vnd.android.cursor.item/$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}