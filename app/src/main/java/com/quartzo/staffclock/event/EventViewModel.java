package com.quartzo.staffclock.event;

import android.arch.lifecycle.ViewModel;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.data.source.EventRepository;
import com.quartzo.staffclock.data.source.GenericDataSource;

import java.util.List;

public class EventViewModel extends ViewModel {

    private List<Event> mListEvents;
    private EventRepository mEventRepository;

    public EventViewModel(EventRepository eventRepository){
        mEventRepository = eventRepository;
    }

    public void getEvents(GenericDataSource.LoadListCallback<Event> callback){
        mEventRepository.getEvents(callback);
    }

    public void insertEvent(Event... events){
        mEventRepository.insertEvents(events);
    }

}
