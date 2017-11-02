package com.feilz.poe_alarm;

import android.provider.BaseColumns;


class DatabaseSQLStrings {
    static final String table_name = "poe_currencies";

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+
            table_name + "(" +
            columnNames._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            columnNames.currencies + " TEXT," +
            columnNames.timestamp + " REAL DEFAULT 0, " +
            columnNames.value + " FLOAT(7,3)" +
            ")";
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + table_name;

    static final class columnNames implements BaseColumns{
        static final String currencies = "currency";
        static final String _ID = "_id";
        static final String timestamp = "timestamp";
        static final String value = "value";

    }
}
