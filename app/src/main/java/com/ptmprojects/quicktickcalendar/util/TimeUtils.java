package com.ptmprojects.quicktickcalendar.util;

import android.content.Context;

import com.ptmprojects.quicktickcalendar.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {


    public static final LocalDate FIRST_DAY_OF_TIME;
    public static final LocalDate LAST_DAY_OF_TIME;
    public static final int DAYS_OF_TIME;

    static {
        FIRST_DAY_OF_TIME = new LocalDate(1900, 1, 1);
        LAST_DAY_OF_TIME = new LocalDate(2100, 12, 31);

        // check if this number of days is OK:
        DAYS_OF_TIME = 73413;
    }

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param day
     * @return the position or 0 if day is null
     */

    public static int getPositionForDay(LocalDate day) {
        if (day != null) {
            return Days.daysBetween(FIRST_DAY_OF_TIME, day).getDays();
        }
        return 0;
    }

    /**
     * Get the day for a given position in the ViewPager
     *
     * @param position
     * @return the day
     * @throws IllegalArgumentException if position is negative
     */

    public static LocalDate getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        return FIRST_DAY_OF_TIME.plusDays(position);
    }

    public static String getFormattedDate(Context context, LocalDate date) {
        final String defaultPattern = "yyyy-MM-dd";

        String pattern = null;
        if (context != null) {
            pattern = context.getString(R.string.date_format);
        }
        if (pattern == null) {
            pattern = defaultPattern;
        }
        DateTimeFormatter mDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        return mDateTimeFormatter.print(date);
    }


}
