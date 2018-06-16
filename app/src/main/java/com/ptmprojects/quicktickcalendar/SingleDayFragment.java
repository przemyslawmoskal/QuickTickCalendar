package com.ptmprojects.quicktickcalendar;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class SingleDayFragment extends Fragment {
    private static final String TAG = "SingleDayFragment";
    private static final String DIALOG_NEW_TASK = "DialogNewTask";
    private static final int REQUEST_DATA_FROM_DIALOG = 0;
    private static final String KEY_DATE = "date";
    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private LocalDate date;

    public static SingleDayFragment newInstance(LocalDate date) {
        SingleDayFragment fragmentFirst = new SingleDayFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATE, date);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "SingleDayFragment.onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final LocalDate localDate = (LocalDate) getArguments().getSerializable(KEY_DATE);
        date = localDate;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "SingleDayFragment.onCreateView()");
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_single_day, container, false);
        mTaskRecyclerView = (RecyclerView) view.findViewById(R.id.single_day_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(TAG, "SingleDayFragment.onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "SingleDayFragment.onResume()");
        TasksBank tasksBank;
        if (getActivity() != null) {
            tasksBank = TasksBank.get(getActivity());
            List<SingleTask> tasksForSingleDay = tasksBank.getTasksForDate(date);
            if (mAdapter == null) {
                mAdapter = new TaskAdapter(tasksForSingleDay);
                mTaskRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setTasks(tasksForSingleDay);
                mAdapter.notifyDataSetChanged();
                mTaskRecyclerView.invalidate();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "SingleDayFragment.onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.add_task_item: {
                FragmentManager fragmentManager = getFragmentManager();
                AddNewTaskDialog dialog = new AddNewTaskDialog();
                dialog.setTargetFragment(SingleDayFragment.this, REQUEST_DATA_FROM_DIALOG);
                dialog.show(fragmentManager, DIALOG_NEW_TASK);
                return true;
            }
            case R.id.year_item: {
                Intent i = new Intent(getContext(), AllTasksByDateActivity.class);
                startActivity(i);
                return true;
            }
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "SingleDayFragment.onActivityResult()");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATA_FROM_DIALOG) {
            LocalDate date = (LocalDate) data.getSerializableExtra(AddNewTaskDialog.EXTRA_DATE);
            String title = (String) data.getSerializableExtra(AddNewTaskDialog.EXTRA_TITLE);
            String description = (String) data.getSerializableExtra(AddNewTaskDialog.EXTRA_DESCRIPTION);
            String alarmString = (String) data.getSerializableExtra(AddNewTaskDialog.EXTRA_ALARM);
            SingleTask newTask = new SingleTask(date, title, false);
            newTask.setDescription(description);
            newTask.setAlarmDetails(alarmString);
            TasksBank.get(getContext()).addTask(newTask);

            int size;
            if (TasksBank.get(getActivity()).getTasksForDate(date) == null) {
                size = -1;
            } else {
                size = TasksBank.get(getActivity()).getTasksForDate(date).size();
            }


            Log.d(TAG, "---------------- IN ONACTIVITYRESULT");

            // there should be new ID for each notficiation:

            if (newTask.getAlarmDetails() != null && !getString(R.string.set_alarm).equals(newTask.getAlarmDetails())) {
                int JOB_ID = 1;
                JobScheduler scheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                boolean hasBeenScheduled = false;
                for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                    if (jobInfo.getId() == JOB_ID) {
                        hasBeenScheduled = true;
                    }
                }
                DateTimeFormatter formatterForDateAndTime = DateTimeFormat.forPattern("YYYY-MM-dd', 'HH':'mm");
                try {
                    LocalDateTime dateAndTimeForAlarm = formatterForDateAndTime.parseLocalDateTime(newTask.getAlarmDetails().toString());
                    DateTimeZone zone = DateTimeZone.getDefault();
                    long millisOfAlarm = dateAndTimeForAlarm.toDateTime().getMillis();
                    if (!hasBeenScheduled) {
                        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(getContext(), NotificationJobService.class))
                                .setMinimumLatency(millisOfAlarm - System.currentTimeMillis())
                                .setOverrideDeadline(millisOfAlarm + 1000 * 60)
                                .setPersisted(true)
                                .build();
                        scheduler.schedule(jobInfo);
                    }
                } catch (IllegalArgumentException iae) {
                    Toast.makeText(getContext(), "No time set for alarm", Toast.LENGTH_SHORT).show();
                }
                JOB_ID++;


            }


            //

            Toast.makeText(getContext(), date.toString() + " " + title + " " + description + ", list size: " + size, Toast.LENGTH_SHORT)
                    .show();

            mTaskRecyclerView.getAdapter().notifyDataSetChanged();
            updateUI();
            ;
            ((MainActivity) getActivity()).getVpPager().getAdapter().notifyDataSetChanged();
            mTaskRecyclerView.invalidate();
        }
    }

    private void updateUI() {
        Log.i(TAG, "SingleDayFragment.updateUI()");
        TasksBank tasksBank = TasksBank.get(getActivity());
        List<SingleTask> tasksForSingleDay = tasksBank.getTasksForDate(date);

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(tasksForSingleDay);
            mTaskRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(tasksForSingleDay);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SingleTask mSingleTask;
        private EditText mTitleTextView;
        private ImageButton mDescriptionTextView;
        private CheckBox mIsCompletedCheckBox;
        private ImageButton mDeleteButton;
        private LinearLayout details;
        private EditText mDescriptionDetails;
        private EditText mAlarmDetails;
        private EditText mLocationDetails;
        private Button mAddDetailsButton;
        private LinearLayout mSetDescriptionWholeLayout;
        private LinearLayout mSetAlarmWholeLayout;
        private LinearLayout mSetLocationWholeLayout;
        private LinearLayout mAddDetailsWholeLayout;

        public TaskHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (EditText) itemView.findViewById(R.id.task_title);
            mDescriptionTextView = (ImageButton) itemView.findViewById(R.id.task_description);
            mIsCompletedCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_completed);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            details = (LinearLayout) itemView.findViewById(R.id.details);
            mDescriptionDetails = (EditText) itemView.findViewById(R.id.description_details);
            mAlarmDetails = (EditText) itemView.findViewById(R.id.alarm_details);
            mLocationDetails = (EditText) itemView.findViewById(R.id.location_details);
            mAddDetailsButton = (Button) itemView.findViewById(R.id.add_details_button);
            mSetDescriptionWholeLayout = (LinearLayout) itemView.findViewById(R.id.set_description_whole_layout);
            mSetAlarmWholeLayout = (LinearLayout) itemView.findViewById(R.id.set_alarm_whole_layout);
            mSetLocationWholeLayout = (LinearLayout) itemView.findViewById(R.id.set_location_whole_layout);
            mAddDetailsWholeLayout = (LinearLayout) itemView.findViewById(R.id.add_details_whole_layout);
        }

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.single_task_list, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (EditText) itemView.findViewById(R.id.task_title);
            mDescriptionTextView = (ImageButton) itemView.findViewById(R.id.task_description);
            mIsCompletedCheckBox = (CheckBox) itemView.findViewById(R.id.task_completed);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            details = (LinearLayout) itemView.findViewById(R.id.details);
            mDescriptionDetails = (EditText) itemView.findViewById(R.id.description_details);
            mAlarmDetails = (EditText) itemView.findViewById(R.id.alarm_details);
            mLocationDetails = (EditText) itemView.findViewById(R.id.location_details);
            mAddDetailsButton = (Button) itemView.findViewById(R.id.add_details_button);
            mSetDescriptionWholeLayout = (LinearLayout) itemView.findViewById(R.id.set_description_whole_layout);
            mSetAlarmWholeLayout = (LinearLayout) itemView.findViewById(R.id.set_alarm_whole_layout);
            mSetLocationWholeLayout = (LinearLayout) itemView.findViewById(R.id.set_location_whole_layout);
            mAddDetailsWholeLayout = (LinearLayout) itemView.findViewById(R.id.add_details_whole_layout);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mSingleTask.getTitle() + " klik!", Toast.LENGTH_SHORT).show();
        }

        public void bind(SingleTask task) {
            mSingleTask = task;
            mTitleTextView.setText(task.getTitle());
            mTitleTextView.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    task.setTitle(mTitleTextView.getText().toString());
                }
            });

            mIsCompletedCheckBox.setChecked(task.isDone());
            mDeleteButton.setOnClickListener((a) -> {
                TasksBank.get(getContext()).deleteTask(task);
                mTaskRecyclerView.getAdapter().notifyDataSetChanged();
                updateUI();
            });

            if (((task.getDescription() != null) && (!"".equals(task.getDescription())))
                    && ((task.getLocationDetails() != null) && (!"".equals(task.getLocationDetails())))
                    && ((task.getAlarmDetails() != null) && (!"".equals(task.getAlarmDetails())))) {
                mAddDetailsWholeLayout.setVisibility(View.GONE);
            } else {
                mAddDetailsWholeLayout.setVisibility(View.VISIBLE);
            }
            if (task.getDescription() != null && !"".equals(task.getAlarmDetails())) {
                mSetDescriptionWholeLayout.setVisibility(View.VISIBLE);
                mDescriptionDetails.setText(task.getDescription());
            } else {
                mDescriptionDetails.setText("");
                task.setDescription(null);
                mSetDescriptionWholeLayout.setVisibility(View.GONE);
            }
            mDescriptionDetails.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    task.setDescription(mDescriptionDetails.getText().toString());
                }
            });
            if (task.getLocationDetails() != null && !"".equals(task.getLocationDetails())) {
                mSetLocationWholeLayout.setVisibility(View.VISIBLE);
                mLocationDetails.setText(task.getLocationDetails());
            } else {
                mLocationDetails.setText("");
                task.setLocationDetails(null);
                mSetLocationWholeLayout.setVisibility(View.GONE);
            }
            mLocationDetails.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    task.setLocationDetails(mLocationDetails.getText().toString());
                }
            });
            if (task.getAlarmDetails() != null && !"".equals(task.getAlarmDetails())) {
                mSetAlarmWholeLayout.setVisibility(View.VISIBLE);
                mAlarmDetails.setText(task.getAlarmDetails());
            } else {
                mAlarmDetails.setText("");
                task.setAlarmDetails(null);
                mSetAlarmWholeLayout.setVisibility(View.GONE);
            }
            mAlarmDetails.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    task.setAlarmDetails(mAlarmDetails.getText().toString());
                }
            });

            mAddDetailsButton.setOnClickListener(v -> {
                mSetDescriptionWholeLayout.setVisibility(View.VISIBLE);
                mSetLocationWholeLayout.setVisibility(View.VISIBLE);
                mSetAlarmWholeLayout.setVisibility(View.VISIBLE);
                // Add details button disappears when all details shown:
                mAddDetailsWholeLayout.setVisibility(View.GONE);
            });
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<SingleTask> mTasks;

        private int mExpandedPosition;
        int previousExpandedPosition = -1;

        public TaskAdapter(List<SingleTask> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            SingleTask task = mTasks.get(position);
            holder.mIsCompletedCheckBox.setOnCheckedChangeListener((a, b) -> {
                if (b) {
                    mTasks.get(position).setDone(true);
                    holder.mIsCompletedCheckBox.setChecked(true);
                } else {
                    mTasks.get(position).setDone(false);
                    holder.mIsCompletedCheckBox.setChecked(false);
                }
            });
            final boolean isExpanded = position == mExpandedPosition;
            holder.details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.itemView.setActivated(isExpanded);
            // If I want to expand only one element at a time, I should use these 3 lines:
//            if (isExpanded) {
//                previousExpandedPosition = position;
//            }
            if (isExpanded) {
//                previousExpandedPosition = position;
                holder.mDeleteButton.setVisibility(View.VISIBLE);
            } else {
                holder.mDeleteButton.setVisibility(View.GONE);
            }

            holder.mDescriptionTextView.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            if (mTasks != null) {
                return mTasks.size();
            } else {
                return 0;
            }

        }

        public void setTasks(List<SingleTask> tasks) {
            mTasks = tasks;
        }
    }
}
