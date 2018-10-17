package com.quartzo.staffclock.worktime;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.interfaces.Callbacks;
import com.quartzo.staffclock.utils.DateUtils;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkTimeAdapter extends RecyclerView.Adapter<WorkTimeAdapter.ViewHolder> {

    private Map<String, LocalTime> mLocalTimeMap;
    private List<String> mDates;
    private Callbacks.WorkTimeCallback mCallback;

    public WorkTimeAdapter(Map<String, LocalTime> localTimeMap, Callbacks.WorkTimeCallback callback) {
        this.mLocalTimeMap = localTimeMap;
        mDates = new ArrayList<>(mLocalTimeMap.keySet());
        mCallback = callback;
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

        final String date = mDates.get(position);

        final LocalTime workTime = mLocalTimeMap.get(mDates.get(position));

        holder.title.setText(DateUtils.formatDateDDMMYYY(date));
        holder.subtitle.setText(DateUtils.formatTime(workTime));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onWorkTimeClicked(date,DateUtils.formatTime(workTime));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates != null ? mDates.size() : 0;
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
