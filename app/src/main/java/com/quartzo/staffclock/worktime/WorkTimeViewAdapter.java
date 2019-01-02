package com.quartzo.staffclock.worktime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.interfaces.Callbacks;
import com.quartzo.staffclock.utils.DateTimeUtils;
import com.quartzo.staffclock.utils.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkTimeViewAdapter extends RecyclerView.Adapter<WorkTimeViewAdapter.ViewHolder> {

    private List<Event> mEvents;
    private Callbacks.WorkTimeCallback mCallback;

    public WorkTimeViewAdapter(List<Event> eventList, Callbacks.WorkTimeCallback callback) {
        this.mEvents = eventList;
        mCallback = callback;
    }

    public void swapData(List<Event> eventList){
        this.mEvents = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Event event = mEvents.get(position);

        String workedTime;

        if(event.getTimeComma() == null){
            workedTime = "NO MARK"; //TODO externalize it
        }else{
            workedTime = DateTimeUtils.calculateWorkedTime(event.getTimeComma());
        }

        holder.title.setText(DateUtils.formatDateDDMMYYY(event.getDateTime()));
        holder.subtitle.setText(workedTime);

        holder.view.setOnClickListener(v -> mCallback.onWorkTimeClicked(event.getDateTime(),workedTime));
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView subtitle;
        private View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
        }
    }
}
