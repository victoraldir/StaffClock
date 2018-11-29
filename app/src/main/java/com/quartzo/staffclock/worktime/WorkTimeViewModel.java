package com.quartzo.staffclock.worktime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.data.source.EventRepository;
import com.quartzo.staffclock.data.source.GenericDataSource;
import com.quartzo.staffclock.exceptions.DateTimeNotFoundException;
import com.quartzo.staffclock.utils.Constants;
import com.quartzo.staffclock.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorkTimeViewModel extends ViewModel {


    private LiveData<List<Event>> mListEvents;
    private EventRepository mEventRepository;
    private Calendar mDateTime;

    public WorkTimeViewModel(EventRepository eventRepository, Locale currentLocale){
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

    public void setDate(int day, int month, int year){
        mDateTime = Calendar.getInstance();

        mDateTime.set(Calendar.DAY_OF_MONTH,day);
        mDateTime.set(Calendar.MONTH,month);
        mDateTime.set(Calendar.YEAR,year);
    }

    public void setTime(int hour, int minute, int second){

        mDateTime.set(Calendar.HOUR_OF_DAY,hour);
        mDateTime.set(Calendar.MINUTE,minute);
        mDateTime.set(Calendar.SECOND,second);
    }


    public String getDateTime(){
         return DateTimeUtils.formatDate(mDateTime.getTime());
    }

    public void saveEvent() throws DateTimeNotFoundException {

        if(mDateTime == null)
            throw new DateTimeNotFoundException("Date and Time haven't been set");

        Event event = new Event(Constants.TYPE_MANUAL,getDateTime());

        insertEvent(event);

    }
}
