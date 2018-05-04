package com.ptmprojects.quicktickcalendar;

import org.joda.time.LocalDate;

import java.util.UUID;

public class SingleTask {
    private String mTitle;
    private String mDescription;
    private boolean mIsDone;
    private LocalDate mDate;
    private UUID mId;
    private String mAlarmDetails;
    private String mLocationDetails;

    public SingleTask(LocalDate date, String title, boolean isDone) {
        mDate = date;
        mTitle = title;
        mIsDone = isDone;
        mId = UUID.randomUUID();
    }

    public LocalDate getDate() {
        return mDate;
    }

    public void setDate(LocalDate date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isDone() {
        return mIsDone;
    }

    public void setDone(boolean done) {
        mIsDone = done;
    }

    public String getAlarmDetails() {
        return mAlarmDetails;
    }

    public void setAlarmDetails(String alarm) {
        mAlarmDetails = alarm;
    }

    public String getLocationDetails() {
        return mLocationDetails;
    }

    public void setLocationDetails(String location) {
        mLocationDetails = location;
    }

    public UUID getId() {
        return mId;
    }
}
