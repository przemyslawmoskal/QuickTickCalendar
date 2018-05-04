package com.ptmprojects.quicktickcalendar;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TasksBank {

    private Context mContext;
    private static TasksBank sTasksBank;
    private List<SingleTask> mAllTasksList;

    public static TasksBank get(Context context) {
        if (sTasksBank == null) {
            sTasksBank = new TasksBank(context);
        }
        return sTasksBank;
    }

    private TasksBank(Context context) {
        Random rand = new Random();
        mAllTasksList = new ArrayList<>();
        // Just a sample, to check if code is working
        mContext = context.getApplicationContext();
        LocalDate today = new LocalDate();
        for (int i = 0; i < 10; i++) {
            mAllTasksList.add(new SingleTask(today, "Today " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.minusDays(4), "Task " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.minusDays(3), "Task " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.minusDays(2), "Day Before Yesterday " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.minusDays(1), "Yesterday " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.plusDays(1), "Tomorrow " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.plusDays(2), "Day After Tomorrow " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.plusDays(3), "Task " + i, i % 2 == 0 ? true : false));
            mAllTasksList.add(new SingleTask(today.plusDays(4), "Task " + i, i % 2 == 0 ? true : false));

        }
        // Random filling with details:
        for (SingleTask task : mAllTasksList) {
            int i = rand.nextInt(2);
            if (i % 2 == 0) {
                task.setDescription("ABCDE descr");
            }
            i = rand.nextInt(2);
            if (i % 2 == 0) {
                task.setAlarmDetails("FGHIJ alarm");
            }
            i = rand.nextInt(2);
            if (i % 2 == 0) {
                task.setLocationDetails("KLMNO location");
            }
        }
    }

    public ArrayList<SingleTask> getTasksForDate(LocalDate date) {
        ArrayList<SingleTask> tasksForDate = new ArrayList<>();
        for (SingleTask task : getAllTasksList()) {
            if (task.getDate().equals(date)) {
                tasksForDate.add(task);
            }
        }
        return tasksForDate;
    }

    public List<SingleTask> getAllTasksList() {
        return mAllTasksList;
    }

    public void addTask(SingleTask task) {
        mAllTasksList.add(task);
    }

    public boolean deleteTask(SingleTask task) {
        for (SingleTask tsk : getAllTasksList()) {
            if (task.getId().equals(tsk.getId())) {
                mAllTasksList.remove(task);
                return true;
            }
        }
        return false;
    }
}
