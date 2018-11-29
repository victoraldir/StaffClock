package com.quartzo.staffclock.data.source.local.event;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.data.source.GenericDataSource;

import java.util.List;

public interface EventDataSource extends GenericDataSource {

    LiveData<List<Event>> getEvents();

    void getEvents(@NonNull GenericDataSource.LoadListCallback<Event> callback);

    LiveData<List<Event>> getEventsByDate(String date);

    void insertEvents(Event... events);

    void deleteEvents(Event... events);
}
