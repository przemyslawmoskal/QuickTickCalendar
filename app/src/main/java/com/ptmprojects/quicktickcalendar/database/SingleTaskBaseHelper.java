package com.ptmprojects.quicktickcalendar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ptmprojects.quicktickcalendar.database.SingleTaskDbSchema.SingleTaskTable;

public class SingleTaskBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TasksBase.db";

    public SingleTaskBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SingleTaskTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SingleTaskTable.Cols.UUID + ", " +
                SingleTaskTable.Cols.DATE + ", " +
                SingleTaskTable.Cols.TITLE + ", " +
                SingleTaskTable.Cols.DESCRIPTION + ", " +
                SingleTaskTable.Cols.IS_DONE + ", " +
                SingleTaskTable.Cols.ALARM + ", " +
                SingleTaskTable.Cols.LOCATION +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
