package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "UsersDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "password TEXT," +
                    "role TEXT)"
        )

        // Default admin
        val adminValues = ContentValues().apply {
            put("username", "admin")
            put("password", "admin123")
            put("role", "admin")
        }
        db.insert("users", null, adminValues)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, newV: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertUser(username: String, password: String, role: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        cv.put("role", role)
        val res = db.insert("users", null, cv)
        return res != -1L
    }

    fun login(username: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )

        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getInt(0),
                username = cursor.getString(1),
                password = cursor.getString(2),
                role = cursor.getString(3)
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun getUsers(): ArrayList<User> {
        val list = ArrayList<User>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)

        while (cursor.moveToNext()) {
            list.add(
                User(
                    id = cursor.getInt(0),
                    username = cursor.getString(1),
                    password = cursor.getString(2),
                    role = cursor.getString(3)
                )
            )
        }
        cursor.close()
        return list
    }

    fun updateUser(id: Int, username: String, password: String, role: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        cv.put("role", role)
        val res = db.update("users", cv, "id=?", arrayOf(id.toString()))
        return res > 0
    }

    fun deleteUser(id: Int): Boolean {
        val db = writableDatabase
        val res = db.delete("users", "id=?", arrayOf(id.toString()))
        return res > 0
    }
}