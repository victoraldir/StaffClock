package com.quartzo.staffclock.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.data.source.local.event.EventDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class EventRepository implements EventDataSource {

    private volatile static EventRepository INSTANCE = null;

    private EventDataSource mEventDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Event> mCachedSection;

    private EventRepository(EventDataSource eventDataSource){
        mEventDataSource = eventDataSource;
    }

    public static EventRepository getInstance(EventDataSource eventDataSource){
        if(INSTANCE == null)
            INSTANCE = new EventRepository(eventDataSource);

        return INSTANCE;
    }

    @Override
    public LiveData<List<Event>> getEvents() {
        return mEventDataSource.getEvents();
    }

    @Override
    public void getEvents(@NonNull final LoadListCallback<Event> callback) {
        checkNotNull(callback);

        if (mCachedSection != null) {
            callback.onListLoaded(new ArrayList<>(mCachedSection.values()));
            return;
        }

        mEventDataSource.getEvents(new GenericDataSource.LoadListCallback<Event>() {
            @Override
            public void onListLoaded(List<Event> list) {
                refreshCacheSection(list);
                callback.onListLoaded(list);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public LiveData<List<Event>> getEventsByDate(String date) {
        return mEventDataSource.getEventsByDate(date);
    }

    @Override
    public void insertEvents(Event... events) {
        mEventDataSource.insertEvents(events);
    }

    private void refreshCacheSection(List<Event> eventList) {
        if (mCachedSection == null) {
            mCachedSection = new LinkedHashMap<>();
        }
        mCachedSection.clear();
        for (Event event : eventList) {
            mCachedSection.put(event.getDateTime(), event);
        }
    }
}
