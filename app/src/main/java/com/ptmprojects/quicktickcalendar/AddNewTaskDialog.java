package com.ptmprojects.quicktickcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AddNewTaskDialog extends DialogFragment {
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mDateButton;
    private Button mAlarmButton;
    private Button mLocationButton;
    public static final String EXTRA_DATE = "ptmprojects.com.quicktickcalendar.date";
    public static final String EXTRA_TITLE = "ptmprojects.com.quicktickcalendar.title";
    public static final String EXTRA_DESCRIPTION = "ptmprojects.com.quicktickcalendar.description";
    public static final String EXTRA_ALARM = "ptmprojects.com.quicktickcalendar.alarm";
    public static final String EXTRA_LOCATION = "ptmprojects.com.quicktickcalendar.location";
    private static final String DATE_PICKER_DIALOG = "DatePickerDialog";
    private static final String DATE_AND_TIME_PICKER_DIALOG = "DateAndTimePickerDialog";
    private static final int REQUEST_DATE_FROM_DATE_PICKER = 0;
    private static final int REQUEST_DATE_AND_TIME_FROM_DATE_AND_TIME_PICKER = 1;

    DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd");
    DateTimeFormatter formatterForDateAndTime = DateTimeFormat.forPattern("YYYY-MM-dd', 'HH':'mm");

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.add_new_task_dialog, null);
        mTitleEditText = (EditText) v.findViewById(R.id.task_title_edit_text);


        mDescriptionEditText = (EditText) v.findViewById(R.id.task_description_edit_text);
        mDateButton = (Button) v.findViewById(R.id.set_date_button);
        mDateButton.setOnClickListener(v1 -> {
            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment
                    .newInstance(new LocalDate());
            dialog.setTargetFragment(AddNewTaskDialog.this, REQUEST_DATE_FROM_DATE_PICKER);
            dialog.show(manager, DATE_PICKER_DIALOG);
        });
        mAlarmButton = (Button) v.findViewById(R.id.set_alarm_button);
        mAlarmButton.setOnClickListener(v12 -> {
            FragmentManager manager = getFragmentManager();
            DateAndTimePickerForAlarmFragment dialog = DateAndTimePickerForAlarmFragment
                    .newInstance(new LocalDateTime());
            dialog.setTargetFragment(AddNewTaskDialog.this, REQUEST_DATE_AND_TIME_FROM_DATE_AND_TIME_PICKER);
            dialog.show(manager, DATE_AND_TIME_PICKER_DIALOG);
        });


        mLocationButton = (Button) v.findViewById(R.id.set_location_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    String title = mTitleEditText.getText().toString();
                    String description = mDescriptionEditText.getText().toString();
                    LocalDate date = formatter.parseLocalDate(mDateButton.getText().toString());
                    String alarm = mAlarmButton.getText().toString();
                    String location = mLocationButton.getText().toString();
                    sendResult(Activity.RESULT_OK, date,
                            title,
                            description,
                            alarm,
                            location);

                })
                .setNegativeButton(R.string.cancel_button, (dialog, id) -> {
//                        LoginDialogFragment.this.getDialog().cancel();
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            mTitleEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!("".equals(mTitleEditText.getText().toString())) && !getString(R.string.set_date).equals(mDateButton.getText())) {
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mDateButton.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!("".equals(mTitleEditText.getText().toString())) && !getString(R.string.set_date).equals(mDateButton.getText())) {
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });


        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE_FROM_DATE_PICKER) {
            LocalDate date = (LocalDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE_FROM_DATE_PICKER);
            mDateButton.setText(formatter.print(date));
        }
        if (requestCode == REQUEST_DATE_AND_TIME_FROM_DATE_AND_TIME_PICKER) {
            LocalDateTime dateTime = (LocalDateTime) data.getSerializableExtra(DateAndTimePickerForAlarmFragment.EXTRA_DATE_AND_TIME_FROM_DIALOG_FOR_ALARM);
            mAlarmButton.setText(formatterForDateAndTime.print(dateTime));
        }
    }

    private void sendResult(int resultCode, LocalDate date, String title, String description, String alarm, String location) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_ALARM, alarm);
        intent.putExtra(EXTRA_LOCATION, location);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
