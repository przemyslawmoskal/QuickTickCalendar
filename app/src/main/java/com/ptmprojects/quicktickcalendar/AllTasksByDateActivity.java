package com.ptmprojects.quicktickcalendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ptmprojects.quicktickcalendar.adapter.HeaderItem;
import com.ptmprojects.quicktickcalendar.adapter.ListItem;
import com.ptmprojects.quicktickcalendar.adapter.TaskItem;
import com.ptmprojects.quicktickcalendar.adapter.TasksAdapter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AllTasksByDateActivity  extends AppCompatActivity {

    @NonNull
    private List<ListItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_by_date);

        Map<LocalDate, List<SingleTask>> events = toMap(loadEvents());

        for (LocalDate date : events.keySet()) {
            HeaderItem header = new HeaderItem(date);
            items.add(header);
            for (SingleTask event : events.get(date)) {
                TaskItem item = new TaskItem(event);
                items.add(item);
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TasksAdapter(items));
    }

    @NonNull
    private List<SingleTask> loadEvents() {
        return TasksBank.get(this).getAllTasksList();
    }

    @NonNull
    private Map<LocalDate, List<SingleTask>> toMap(@NonNull List<SingleTask> events) {
        Map<LocalDate, List<SingleTask>> map = new TreeMap<>();
        for (SingleTask event : events) {
            List<SingleTask> value = map.get(event.getDate());
            if (value == null) {
                value = new ArrayList<>();
                map.put(event.getDate(), value);
            }
            value.add(event);
        }
        return map;
    }

}
