package com.quartzo.staffclock.event;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.data.WorkTime;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<WorkTime> mWorkTimeList;

    public EventAdapter(List<WorkTime> workTimeList) {
        this.mWorkTimeList = workTimeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final WorkTime workTime = mWorkTimeList.get(position);

        holder.title.setText(workTime.getWorkedTime().toString());
        holder.subtitle.setText(workTime.getDateTime().toString());
    }

    @Override
    public int getItemCount() {
        return mWorkTimeList != null ? mWorkTimeList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView subtitle;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
        }
    }
}
