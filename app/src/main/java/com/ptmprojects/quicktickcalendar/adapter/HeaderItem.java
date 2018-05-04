package com.ptmprojects.quicktickcalendar.adapter;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public class HeaderItem extends ListItem {

    @NonNull
    private LocalDate mDate;

    public HeaderItem(@NonNull LocalDate date) {
        this.mDate = date;
    }

    @NonNull
    public LocalDate getDate() {
        return mDate;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

}
