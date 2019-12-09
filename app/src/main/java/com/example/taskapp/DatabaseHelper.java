package com.example.taskapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    static final String TASKS_TABLE_NAME = "TASKS";
    static final String SUB_TASKS_TABLE_NAME = "SUB_TASKS";

    // Table columns
    static final String _ID = "_id";
    static final String NAME = "name";
    static final String START_DATE = "start_date";
    static final String END_DATE = "end_date";
    static final String PRIORITY = "priority";
    static final String TASK_ID = "task_id";

    private static final String DB_NAME = "TASKS.DB";

    // database version
    private static final int DB_VERSION = 3;

    // Creating table query
    private static final String CREATE_TABLE_TASKS = "create table " + TASKS_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " +
            START_DATE + " TEXT NOT NULL, " + END_DATE + " TEXT NOT NULL, " + PRIORITY + ");";

    private static final String CREATE_TABLE_SUB_TASKS = "create table " + SUB_TASKS_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " +
            START_DATE + " TEXT NOT NULL, " + END_DATE + " TEXT NOT NULL, " + TASK_ID + ");";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_SUB_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_TASKS_TABLE_NAME);
        onCreate(db);
    }
}
