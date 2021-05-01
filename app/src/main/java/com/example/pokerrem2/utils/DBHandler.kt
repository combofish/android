package com.example.pokerrem2.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.pokerrem2.data.ReciteRecord

class DBHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "PokerDatabase"
        private val TABLE_CONTACTS = "reciteRecords"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            |create table reciteRecords(
            |id integer primary key autoincrement not null,
            |right integer not null,
            |wrong integer,
            |rate real not null,
            |remember_time text not null,
            |dictation_time text not null,
            |total_time text)
        """.trimMargin("|")
        Log.i("db",createTable)
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    //method to insert data
    fun addReciteRecord(rr: ReciteRecord):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("right",rr.right)
        contentValues.put("rate",rr.rate)
        contentValues.put("remember_time",rr.rememberTime)
        contentValues.put("dictation_time",rr.dictationTime)
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun viewReciteRecord():List<ReciteRecord>{
        val rrList:ArrayList<ReciteRecord> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var right:Int
        var rate:Float
        var rememberTime:String
        var dictationTime:String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                right = cursor.getInt(cursor.getColumnIndex("right"))
                rate = cursor.getFloat(cursor.getColumnIndex("rate"))
                rememberTime = cursor.getString(cursor.getColumnIndex("remember_time"))
                dictationTime = cursor.getString(cursor.getColumnIndex("dictation_time"))
               val rr = ReciteRecord(
                   id,
                   right,
                   rate,
                   rememberTime,
                   dictationTime
               )
                rrList.add(rr)
            } while (cursor.moveToNext())
        }
        return rrList
    }

    //method to update data
    fun updateReciteRecord(rr: ReciteRecord):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // contentValues.put("id",rr.id)
        contentValues.put("right",rr.right)
        contentValues.put("rate",rr.rate)
        contentValues.put("remember_time",rr.rememberTime)
        contentValues.put("dictation_time",rr.dictationTime)
        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+rr.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }


    //method to delete data
    fun deleteReciteRecord(rr:ReciteRecord):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id", rr.id) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+rr.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}