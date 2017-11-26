package com.feilz.poe_alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Database extends SQLiteOpenHelper {

    private static final int DB_version = 1;
    private static final String DB_name = "poe-alarm.db";

    public Database(Context c){
        super(c,DB_name,null,DB_version);
        Log.d("databse","created database");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseSQLStrings.CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseSQLStrings.DROP_ALARM_TABLE);
        onCreate(sqLiteDatabase);
    }

    void insertNewAlarm(String currency, boolean lessThan, double value, String league){
        ContentValues data = new ContentValues();
        data.put(DatabaseSQLStrings.columnNames.currencies,currency);
        data.put(DatabaseSQLStrings.columnNames.lessThan,lessThan);
        data.put(DatabaseSQLStrings.columnNames.value,value);
        data.put(DatabaseSQLStrings.columnNames.timestamp,getCurrentTime());
        data.put(DatabaseSQLStrings.columnNames.league,league);
        getWritableDatabase().insert(DatabaseSQLStrings.alarm_table_name,null,data);
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( "yy:MM:dd;HH:mm:ss", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    void removeAlarm(String currency, String league){
        getWritableDatabase().delete(DatabaseSQLStrings.alarm_table_name,
                DatabaseSQLStrings.columnNames.currencies + " =? AND " +
                DatabaseSQLStrings.columnNames.league + " =? ",new String[] {currency,league});
    }

    Cursor getAllAlarms(){
        String sqlQuery = "SELECT  * FROM " + DatabaseSQLStrings.alarm_table_name;
        return getReadableDatabase().rawQuery(sqlQuery,null);
    }
}
