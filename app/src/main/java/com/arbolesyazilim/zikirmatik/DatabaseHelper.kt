package com.arbolesyazilim.zikirmatik

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "your_database_name"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "zikir_table"
        const val COLUMN_NAME = "name"
        const val COLUMN_NUMBER = "number"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_NUMBER INTEGER);"

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades here
    }

    fun insertData(name: String, number: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_NUMBER, number)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllData(): List<ZikirData> {
        val zikirList = mutableListOf<ZikirData>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val number = cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER))
                zikirList.add(ZikirData(name, number))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return zikirList
    }

    fun deleteZikirData(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME=?", arrayOf(name))
        db.close()
    }


}
