package com.quartzo.staffclock.event;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.ViewModelFactory;
import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.interfaces.Callbacks;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment implements Observer<List<Event>>{

    public static final String ARG_DATE = ".date";
    public static final String ARG_TIME = ".time";
    public static final String ARG_TYPE = ".type";
    private EventAdapter mAdapter;
    private Callbacks.EventCallback mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_event, container, false);

        Bundle args = getArguments();

        EventViewModel mEventViewModel = ViewModelFactory.getInstance(getActivity()
                .getApplication()).create(EventViewModel.class);

        String dateArgument = args.getString(ARG_DATE);
        String timeArgument = args.getString(ARG_TIME);
        String type = args.getString(ARG_TYPE);

        mEventViewModel.getListEventsByDate(dateArgument, type).observe(this,this);

        RecyclerView mWorkTimeRecycle = (RecyclerView) rootView.findViewById(R.id.work_time_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mWorkTimeRecycle.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mWorkTimeRecycle.getContext(),
                linearLayoutManager.getOrientation());
        mWorkTimeRecycle.addItemDecoration(dividerItemDecoration);

        List<Event> mEventList = new ArrayList<>();
        mAdapter = new EventAdapter(mEventList,mCallback);
        mWorkTimeRecycle.setAdapter(mAdapter);

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks.EventCallback) {
            mCallback = (Callbacks.EventCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EventCallback");
        }

    }
    // 30/07/2018 08:45
    @Override
    public void onChanged(@Nullable List<Event> eventList) {
        mAdapter.swapData(eventList);
    }
}
