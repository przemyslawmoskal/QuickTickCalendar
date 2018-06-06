package com.ptmprojects.quicktickcalendar.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ptmprojects.quicktickcalendar.SingleTask;
import com.ptmprojects.quicktickcalendar.database.SingleTaskDbSchema.SingleTaskTable;

import org.joda.time.LocalDate;

import java.util.UUID;

public class SingleTaskCursorWrapper extends CursorWrapper {

    public SingleTaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public SingleTask getSingleTask() {
        String uuidString = getString(getColumnIndex(SingleTaskTable.Cols.UUID));
        String dateString = getString(getColumnIndex(SingleTaskTable.Cols.DATE));
        String title = getString(getColumnIndex(SingleTaskTable.Cols.TITLE));
        String description = getString(getColumnIndex(SingleTaskTable.Cols.DESCRIPTION));
        int isDone = getInt(getColumnIndex(SingleTaskTable.Cols.IS_DONE));
        String alarm = getString(getColumnIndex(SingleTaskTable.Cols.ALARM));
        String location = getString(getColumnIndex(SingleTaskTable.Cols.LOCATION));

        SingleTask task = new SingleTask(UUID.fromString(uuidString));
        task.setDate(new LocalDate(dateString));
        task.setTitle(title);
        task.setDescription(description);
        task.setDone(isDone != 0);
        task.setAlarmDetails(alarm);
        task.setLocationDetails(location);

        return task;
    }
}
