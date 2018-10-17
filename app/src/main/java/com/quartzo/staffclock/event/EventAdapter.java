package com.quartzo.staffclock.event;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.interfaces.Callbacks;
import com.quartzo.staffclock.utils.DateUtils;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> mEventList;
    private Callbacks.EventCallback mCallback;

    public EventAdapter(List<Event> eventList, Callbacks.EventCallback callback) {
        mEventList = eventList;
        mCallback = callback;
    }

    public void swapData(@NonNull List<Event> eventList){
        mEventList = eventList;
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

        final Event event = mEventList.get(position);

        holder.title.setText(DateUtils.formatTime(event.getParseDateTime()));
        holder.subtitle.setText(event.getType());

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCallback.onLongEventClicked(event);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventList != null ? mEventList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView subtitle;
        private View mView;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
        }
    }
}
