package com.feilz.poe_alarm;

import android.provider.BaseColumns;


class DatabaseSQLStrings {
    static final String alarm_table_name = "poe_currency_alarm";

    static final String CREATE_ALARM_TABLE = "CREATE TABLE IF NOT EXISTS "+
            alarm_table_name + "(" +
            columnNames._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            columnNames.currencies + " TEXT," +
            columnNames.lessThan + " BOOLEAN " +
            columnNames.timestamp + " REAL DEFAULT 0, " +
            columnNames.league + " TEXT " +
            columnNames.value + " FLOAT(7,3)" +
            ")";
    static final String DROP_ALARM_TABLE = "DROP TABLE IF EXISTS " + alarm_table_name;

    static final String league_names_table = "poe_league_names";

    static final String CREATE_LEAGUE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            league_names_table + "(" +
            columnNames._ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            columnNames.league + " TEXT " +
            columnNames.helpNum + " INTEGER)";

    static final String DROP_LEAGUE_TABLE = "DROP TABLE IF EXISTS " + league_names_table;

    static final class columnNames implements BaseColumns{
        static final String currencies = "currency";
        static final String lessThan = "lessThan";
        static final String _ID = "_id";
        static final String timestamp = "timestamp";
        static final String value = "value";
        static final String league = "league";
        static final String helpNum = "helpNum";
    }
}
