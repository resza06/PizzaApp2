package com.example.pizzaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.pizzaapp.model.MenuModel

class DatabaseHelper (var context: Context): SQLiteOpenHelper(
    context,DATABASE_NAME,null,DATABASE_VERSION
) {
    companion object {
        private val DATABASE_NAME = "pizza"
        private val DATABASE_VERSION = 1

        //table name
        private val TABLE_ACCOUNT = "account"

        //column account table
        private val COLUMN_EMAIL = "email"
        private val COLUMN_NAME = "name"
        private val COLUMN_LEVEL = "level"
        private val COLUMN_PASSWORD = "password"

        //table menu
        private val TABLE_MENU = "menu"
        //column menu table
        private val COLUMN_ID_MENU = "idMenu"
        private val COLUMN_NAMA_MENU = "menuName"
        private val COLUMN_PRICE_MENU = "price"
        private val COLUMN_IMAGE = "photo"

        //create table menu sql query
        private val CREATE_MENU_TABLE = ("CREATE TABLE " + TABLE_MENU + "("
                + COLUMN_ID_MENU + " INT PRIMARY KEY, "+ COLUMN_NAMA_MENU +" TEXT, "
                + COLUMN_PRICE_MENU + " INT, " + COLUMN_IMAGE +" BLOB)")

        //drop table menu sql query
        private val DROP_MENU_TABLE = "DROP TABLE IF EXIST $TABLE_MENU"
    }

    private val CREATE_ACCOUNT_TABLE = ("CREATE TABLE"+TABLE_ACCOUNT+"("
    + COLUMN_EMAIL + "TEXT PRIMARY KEY," + COLUMN_NAME + " TEXT,"
    +COLUMN_LEVEL + "TEXT,"+ COLUMN_PASSWORD+"TEXT)")
    //drop table account table query
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
        p0?.execSQL(CREATE_MENU_TABLE)
        p0?.execSQL(INSERT_ACCOUNT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_ACCOUNT_TABLE)
        p0?.execSQL(DROP_MENU_TABLE)
        onCreate(p0)
    }

    fun checkLogin(email: String, password: String): Boolean {
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        //selection critorio
        val selection = "$COLUMN_EMAIL =? AND $COLUMN_PASSWORD =?"
        //selection argument
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(
            TABLE_ACCOUNT,
            colums, //colums return
            selection,//colums from where close
            selectionArgs,//the values for the where clause
            null,//group the rows
            null,//filter by rows group
            null
        )//the sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true
        else
            return false
    }

    fun addAccount(email:String, name:String, level:String, password:String){
        val db = this.readableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_LEVEL, level)
        values.put(COLUMN_PASSWORD, password)

       val result = db.insert(TABLE_ACCOUNT, null, values)

        if (result==(0).toLong()){
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Register Succes, " +
            "please login using your new account", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun checkData(email:String):String{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        var name:String = " "
         val cursor = db.query(TABLE_ACCOUNT,
         colums,
         selection,
         selectionArgs,
         null,
         null,
         null)

        if (cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return name
    }

    fun addMenu(menu:MenuModel)

}
