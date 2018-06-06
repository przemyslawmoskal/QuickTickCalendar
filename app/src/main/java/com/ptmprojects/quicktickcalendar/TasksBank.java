package com.ptmprojects.quicktickcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ptmprojects.quicktickcalendar.database.SingleTaskBaseHelper;
import com.ptmprojects.quicktickcalendar.database.SingleTaskCursorWrapper;
import com.ptmprojects.quicktickcalendar.database.SingleTaskDbSchema;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class TasksBank {

    private Context mContext;
    private static TasksBank sTasksBank;

    private SQLiteDatabase mDatabase;

    private TasksBank(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new SingleTaskBaseHelper(mContext)
                .getWritableDatabase();
    }

    private static ContentValues getContentValues(SingleTask task) {
        ContentValues values = new ContentValues();
        if (task.getId() != null) {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.UUID, task.getId().toString());
        } else {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.UUID, "");
        }
        if (task.getDate() != null) {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.DATE, task.getDate().toString());
        } else {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.DATE, "");
        }
        if (task.getTitle() != null) {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.TITLE, task.getTitle());
        } else {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.TITLE, "");
        }
        if (task.getDescription() != null) {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.DESCRIPTION, task.getDescription());
        } else {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.DESCRIPTION, "");
        }
        values.put(SingleTaskDbSchema.SingleTaskTable.Cols.IS_DONE, task.isDone() ? 1 : 0);
        if (task.getAlarmDetails() != null) {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.ALARM, task.getAlarmDetails().toString());
        } else {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.ALARM, "");
        }
        if (task.getLocationDetails() != null) {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.LOCATION, task.getLocationDetails().toString());
        } else {
            values.put(SingleTaskDbSchema.SingleTaskTable.Cols.LOCATION, "");
        }

        return values;
    }


    public static TasksBank get(Context context) {
        if (sTasksBank == null) {
            sTasksBank = new TasksBank(context);
        }
        return sTasksBank;
    }

    public ArrayList<SingleTask> getTasksForDate(LocalDate date) {
        ArrayList<SingleTask> tasksForDate = new ArrayList<>();

        SingleTaskCursorWrapper cursor = queryTasks(SingleTaskDbSchema.SingleTaskTable.Cols.DATE + " = ?",
                new String[]{date.toString()});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasksForDate.add(cursor.getSingleTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return tasksForDate;
    }

    public List<SingleTask> getAllTasksList() {

        List<SingleTask> tasks = new ArrayList<>();

        SingleTaskCursorWrapper cursor = queryTasks(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getSingleTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return tasks;
    }

    public void addTask(SingleTask task) {
        ContentValues values = getContentValues(task);
        // nullColumnHack?
        mDatabase.insert(SingleTaskDbSchema.SingleTaskTable.NAME, "", values);
    }

    public void updateTask(SingleTask task) {
        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);

        mDatabase.update(SingleTaskDbSchema.SingleTaskTable.NAME, values,
                SingleTaskDbSchema.SingleTaskTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private SingleTaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SingleTaskDbSchema.SingleTaskTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SingleTaskCursorWrapper(cursor);
    }

    public boolean deleteTask(SingleTask task) {
        mDatabase.delete(SingleTaskDbSchema.SingleTaskTable.NAME, SingleTaskDbSchema.SingleTaskTable.Cols.UUID + " = ?",
                new String[]{task.getId().toString()});
        return true;
    }
}
