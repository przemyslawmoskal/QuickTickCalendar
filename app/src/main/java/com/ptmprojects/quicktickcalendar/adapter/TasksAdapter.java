package com.ptmprojects.quicktickcalendar.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ptmprojects.quicktickcalendar.MainActivity;
import com.ptmprojects.quicktickcalendar.R;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    DateTimeFormatter mDateTimeFormatter = DateTimeFormat.forPattern("dd MMMM yyyy");

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_header = (TextView) itemView.findViewById(R.id.txt_header);
        }

    }

    private static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_description;
        CheckBox checkBoxCompleted;

        TaskViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            checkBoxCompleted = (CheckBox) itemView.findViewById(R.id.checkbox_completed);
        }

    }

    @NonNull
    private List<ListItem> items = Collections.emptyList();

    public TasksAdapter(@NonNull List<ListItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.view_list_item_header, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_TASK: {
                View itemView = inflater.inflate(R.layout.view_list_item_event, parent, false);
                return new TaskViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) items.get(position);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                // your logic here
//                holder.txt_header.setText(DateUtils.formatDate(header.getDate()));
                holder.txt_header.setText(header.getDate().toString(mDateTimeFormatter));
                holder.txt_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDate date = header.getDate();
                        String dateString = date.toString("yyyy-MM-dd");
                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                intent.putExtra(MainActivity.DATE_AT_SHOW_UP, dateString);
                                v.getContext().startActivity(intent);
                    }
                });
                break;
            }
            case ListItem.TYPE_TASK: {
                TaskItem event = (TaskItem) items.get(position);
                TaskViewHolder holder = (TaskViewHolder) viewHolder;
                // your logic here
                holder.txt_title.setText(event.getEvent().getTitle());
                holder.txt_description.setText(event.getEvent().getDescription());
                holder.checkBoxCompleted.setChecked(event.getEvent().isDone());
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}
