package com.quartzo.staffclock.event;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.data.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> mEventList;

    public EventAdapter(List<Event> eventList) {
        this.mEventList = eventList;
    }

    public void swapData(@NonNull List<Event> eventList){
        this.mEventList = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Event event = mEventList.get(position);

        holder.title.setText(event.getDateTime());
        holder.subtitle.setText(event.getType());
    }

    @Override
    public int getItemCount() {
        return mEventList != null ? mEventList.size() : 0;
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
