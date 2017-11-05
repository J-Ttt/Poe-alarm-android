package com.feilz.poe_alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {

    private static final int DB_version = 1;
    private static final String DB_name = "poe-alarm.db";

    public Database(Context c){
        super(c,DB_name,null,DB_version);
        Log.d("databse","created database");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseSQLStrings.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseSQLStrings.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
    public void insertToDatabase(String currency, double value){
        ContentValues data = new ContentValues();
        data.put(DatabaseSQLStrings.columnNames.currencies,currency);
        data.put(DatabaseSQLStrings.columnNames.value,value);
        getWritableDatabase().insert(DatabaseSQLStrings.table_name,null,data);
    }
    Cursor readDatabase(String currency){
        String sqlQuery = "SELECT " + DatabaseSQLStrings.columnNames.timestamp + ", " +
                DatabaseSQLStrings.columnNames.value + "FROM " + DatabaseSQLStrings.table_name +
                " WHERE " + DatabaseSQLStrings.columnNames.currencies + " = " + currency;
        return getReadableDatabase().rawQuery(sqlQuery,null);
    }
}
