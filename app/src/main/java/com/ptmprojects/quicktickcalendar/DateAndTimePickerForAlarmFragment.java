package com.ptmprojects.quicktickcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class DateAndTimePickerForAlarmFragment extends DialogFragment {
    public static final String EXTRA_DATE_AND_TIME_FROM_DIALOG_FOR_ALARM = "ptmprojects.com.quicktickcalendar.dateForAlarm";
    public static final String EXTRA_UUID_OF_TASK_TO_CHANGE = "uuid of task to change";
    private static final String ARG_DATE_AND_TIME = "date and time";
    public static final String ARG_UUID_OF_TASK_TO_CHANGE = "uuid of task to change";
    private DatePicker mDatePickerForAlarm;
    private TimePicker mTimePickerForAlarm;

    public static DateAndTimePickerForAlarmFragment newInstance(LocalDateTime dateAndTime) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE_AND_TIME, dateAndTime);

        DateAndTimePickerForAlarmFragment fragment = new DateAndTimePickerForAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DateAndTimePickerForAlarmFragment newInstance(LocalDateTime dateAndTime, UUID idOfTaskToChange ) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE_AND_TIME, dateAndTime);
        args.putSerializable(ARG_UUID_OF_TASK_TO_CHANGE, idOfTaskToChange);
        Log.d(" ///// ID TO CHANGE ///", idOfTaskToChange.toString());


        DateAndTimePickerForAlarmFragment fragment = new DateAndTimePickerForAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDateTime dateAndTime = (LocalDateTime) getArguments().getSerializable(ARG_DATE_AND_TIME);
        UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID_OF_TASK_TO_CHANGE);
        int year = dateAndTime.getYear();
        int month = dateAndTime.getMonthOfYear() - 1;
        int day = dateAndTime.getDayOfMonth();
        int hour = dateAndTime.getHourOfDay();
        int minute = dateAndTime.getMinuteOfHour();

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.date_and_time_picker_alarm_dialog, null);

        mDatePickerForAlarm = (DatePicker) v.findViewById(R.id.dialog_date_picker_for_alarm);
        mDatePickerForAlarm.init(year, month, day, null);

        mTimePickerForAlarm = (TimePicker) v.findViewById(R.id.dialog_time_picker_for_alarm);
        mTimePickerForAlarm.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePickerForAlarm.setHour(hour);
            mTimePickerForAlarm.setMinute(minute);
        } else {
            mTimePickerForAlarm.setCurrentHour(hour);
            mTimePickerForAlarm.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.set_alarm_for_task)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            int year1 = mDatePickerForAlarm.getYear();
                            int month1 = mDatePickerForAlarm.getMonth();
                            int day1 = mDatePickerForAlarm.getDayOfMonth();
                            int hour1;
                            int minute1;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                hour1 = mTimePickerForAlarm.getHour();
                                minute1 = mTimePickerForAlarm.getMinute();
                            } else {
                                hour1 = mTimePickerForAlarm.getCurrentHour();
                                minute1 = mTimePickerForAlarm.getCurrentMinute();
                            }
                            Date date = new GregorianCalendar(year1, month1, day1).getTime();
                            LocalDate localDate = LocalDate.fromDateFields(date);
                            LocalDateTime localDateTime = new LocalDateTime(
                                    localDate.getYear(),
                                    localDate.getMonthOfYear(),
                                    localDate.getDayOfMonth(),
                                    hour1,
                                    minute1,
                                    0,
                                    0
                            );
                            if (uuid == null) {
                                sendResult(Activity.RESULT_OK, localDateTime);
                            } else {
                                sendResult(Activity.RESULT_OK, localDateTime, uuid);
                            }

                        })
                .create();
    }

    private void sendResult(int resultCode, LocalDateTime dateAndTime) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_AND_TIME_FROM_DIALOG_FOR_ALARM, dateAndTime);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void sendResult(int resultCode, LocalDateTime dateAndTime, UUID uuidOfTaskToChange) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_AND_TIME_FROM_DIALOG_FOR_ALARM, dateAndTime);
        intent.putExtra(EXTRA_UUID_OF_TASK_TO_CHANGE, uuidOfTaskToChange);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
