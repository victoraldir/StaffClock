package com.quartzo.staffclock.worktime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.data.source.EventRepository;
import com.quartzo.staffclock.data.source.GenericDataSource;

import java.util.List;

public class WorkTimeViewModel extends ViewModel {

    private LiveData<List<Event>> mListEvents;
    private EventRepository mEventRepository;

    public WorkTimeViewModel(EventRepository eventRepository){
        mEventRepository = eventRepository;
    }

    public void getEvents(GenericDataSource.LoadListCallback<Event> callback){
        mEventRepository.getEvents(callback);
    }

    public LiveData<List<Event>> getListEvents() {
        if (mListEvents == null) {
            mListEvents = mEventRepository.getEvents();
        }
        return mListEvents;
    }

    public void insertEvent(Event... events){
        mEventRepository.insertEvents(events);
    }

}
