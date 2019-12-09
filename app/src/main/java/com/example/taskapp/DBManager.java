package com.example.taskapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

class DBManager {
    private Context context;
    private SQLiteDatabase database;

    DBManager(Context c) {
        context = c;
    }

    void open() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    void insertTask(String name, String startDate, String endDate, int priority) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.START_DATE, startDate);
        contentValue.put(DatabaseHelper.END_DATE, endDate);
        contentValue.put(DatabaseHelper.PRIORITY, priority);
        database.insert(DatabaseHelper.TASKS_TABLE_NAME, null, contentValue);
    }

    void insertSubTask(long taskId, String name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TASK_ID, taskId);
        contentValue.put(DatabaseHelper.NAME, name);
        database.insert(DatabaseHelper.SUB_TASKS_TABLE_NAME, null, contentValue);
    }

    Cursor fetchTasks() {
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.NAME,
                DatabaseHelper.START_DATE,
                DatabaseHelper.END_DATE,
                DatabaseHelper.PRIORITY
        };
        Cursor cursor = database.query(DatabaseHelper.TASKS_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor fetchSubTasks(long taskId) {
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.NAME,
                DatabaseHelper.TASK_ID
        };
        Cursor cursor = database.query(DatabaseHelper.SUB_TASKS_TABLE_NAME, columns, DatabaseHelper.TASK_ID + " = " + taskId, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    void updateTask(long _id, String name, String startDate, String endDate, int priority) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.START_DATE, startDate);
        contentValues.put(DatabaseHelper.END_DATE, endDate);
        contentValues.put(DatabaseHelper.PRIORITY, priority);
        database.update(
                DatabaseHelper.TASKS_TABLE_NAME,
                contentValues,
                DatabaseHelper._ID + " = " + _id,
                null);
    }

    void updateSubTask(long subTaskId, String startDate, String endDate, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.START_DATE, startDate);
        contentValues.put(DatabaseHelper.END_DATE, endDate);
        database.update(
                DatabaseHelper.SUB_TASKS_TABLE_NAME,
                contentValues,
                DatabaseHelper._ID + " = " + subTaskId,
                null);
    }

}
