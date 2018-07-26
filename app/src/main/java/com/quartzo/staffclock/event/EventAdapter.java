package com.quartzo.staffclock.event;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.data.WorkTime;
import com.quartzo.staffclock.utils.DateUtils;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Map<String, LocalTime> mLocalTimeMap;
    private List<String> mDates;

    public EventAdapter(Map<String, LocalTime> localTimeMap) {
        this.mLocalTimeMap = localTimeMap;
        mDates = new ArrayList<>(mLocalTimeMap.keySet());
    }

    public void swapData(@NonNull Map<String, LocalTime> localTimeMap){
        this.mLocalTimeMap =localTimeMap;
        mDates = new ArrayList<>(mLocalTimeMap.keySet());
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

        String date = mDates.get(position);

        final LocalTime workTime = mLocalTimeMap.get(mDates.get(position));

        holder.title.setText(date);
        holder.subtitle.setText(DateUtils.formatTime(workTime));
    }

    @Override
    public int getItemCount() {
        return mDates != null ? mDates.size() : 0;
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
