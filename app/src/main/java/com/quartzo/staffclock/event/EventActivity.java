package com.quartzo.staffclock.event;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.ViewModelFactory;
import com.quartzo.staffclock.data.Event;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements Observer<List<Event>>{

    public static final String ARG_DATE = ".date";
    private EventAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        EventViewModel mEventViewModel = ViewModelFactory.getInstance(getApplication()).create(EventViewModel.class);
        mEventViewModel.getListEvents().observe(this,this);

        RecyclerView mWorkTimeRecycle = (RecyclerView) findViewById(R.id.work_time_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mWorkTimeRecycle.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mWorkTimeRecycle.getContext(),
                linearLayoutManager.getOrientation());
        mWorkTimeRecycle.addItemDecoration(dividerItemDecoration);

        List<Event> mEventList = new ArrayList<>();
        mAdapter = new EventAdapter(mEventList);

        mWorkTimeRecycle.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(@Nullable List<Event> eventList) {
        mAdapter.swapData(eventList);
    }
}
