package com.ptmprojects.quicktickcalendar.adapter;

import android.support.annotation.NonNull;

import com.ptmprojects.quicktickcalendar.SingleTask;

public class TaskItem extends ListItem {

    @NonNull
    private SingleTask mTask;

    public TaskItem(@NonNull SingleTask task) {
        this.mTask = task;
    }

    @NonNull
    public SingleTask getEvent() {
        return mTask;
    }

    // here getters and setters
    // for title and so on, built
    // using event

    @Override
    public int getType() {
        return TYPE_TASK;
    }

}
