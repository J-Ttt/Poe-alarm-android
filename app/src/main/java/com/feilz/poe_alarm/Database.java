package com.feilz.poe_alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Database extends SQLiteOpenHelper {

    private static final int DB_version = 1;
    private static final String DB_name = "poe-alarm.db";

    Database(Context c){
        super(c,DB_name,null,DB_version);
        Log.d("databse","created database");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseSQLStrings.CREATE_ALARM_TABLE);
        sqLiteDatabase.execSQL(DatabaseSQLStrings.CREATE_LEAGUE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseSQLStrings.DROP_ALARM_TABLE);
        sqLiteDatabase.execSQL(DatabaseSQLStrings.DROP_LEAGUE_TABLE);
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

    boolean checkIfLeaguesExists(ArrayList<String> leagues){
        boolean result=true;
        String query = "SELECT * FROM " + DatabaseSQLStrings.league_names_table + " WHERE " +
                DatabaseSQLStrings.columnNames.league + " =?";
        Cursor c = getWritableDatabase().rawQuery(query, new String[]{});
        while (c.moveToNext()){
            if (leagues.contains(c.getString(c.getColumnIndexOrThrow(DatabaseSQLStrings.columnNames.league)))){
                leagues.remove(c.getString(c.getColumnIndexOrThrow(DatabaseSQLStrings.columnNames.league)));
            } else {
                getWritableDatabase().execSQL("DELETE FROM "+  DatabaseSQLStrings.league_names_table +
                                " WHERE " + DatabaseSQLStrings.columnNames.league + " = " +
                                c.getString(c.getColumnIndexOrThrow(DatabaseSQLStrings.columnNames.league)));
                result = false;
            }
        }
        c.close();
        getWritableDatabase().execSQL("DELETE from "+ DatabaseSQLStrings.league_names_table + " WHERE " +
                DatabaseSQLStrings.columnNames._ID + " NOT IN (SELECT MIN(" + DatabaseSQLStrings.columnNames._ID +
                ") FROM " + DatabaseSQLStrings.league_names_table +
                " GROUP BY " + DatabaseSQLStrings.columnNames.league + ")");
        /*if (!leagues.isEmpty()){
            for (String league : leagues) {
                insertLeague(league);
                result = false;
            }
        }*/
       return result;
    }


    private void insertLeague(String league){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSQLStrings.columnNames.league,league);
        getWritableDatabase().insert(DatabaseSQLStrings.league_names_table,null,cv);
    }

    ArrayList<String> getAllLeagues(){
        String query = "SELECT " + DatabaseSQLStrings.columnNames.league + " FROM " + DatabaseSQLStrings.league_names_table;
        ArrayList<String> leagues = new ArrayList<>();
        Cursor c = getWritableDatabase().rawQuery(query,new String[]{});
        while (c.moveToNext()){
            leagues.add(c.getString(c.getColumnIndexOrThrow(DatabaseSQLStrings.columnNames.league)));
        }
        c.close();
        return leagues;
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( "yy:MM:dd;HH:mm:ss", Locale.getDefault());
        return sdf.format(cal.getTime());
    }
    //DONE DURING DEVELOPMENT
    void resetTables(){
        getWritableDatabase().execSQL(DatabaseSQLStrings.DROP_ALARM_TABLE);
        getWritableDatabase().execSQL(DatabaseSQLStrings.CREATE_ALARM_TABLE);
        getWritableDatabase().execSQL(DatabaseSQLStrings.DROP_LEAGUE_TABLE);
        getWritableDatabase().execSQL(DatabaseSQLStrings.CREATE_LEAGUE_TABLE);
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
