package com.quartzo.staffclock.data.source.local.event;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.utils.AppExecutors;

import java.util.List;

public class EventDataSourceImpl implements EventDataSource {

    private static volatile EventDataSourceImpl INSTANCE;

    private EventDao mEventDao;

    private AppExecutors mAppExecutors;

    private EventDataSourceImpl(AppExecutors mAppExecutors, EventDao mEventDao) {
        this.mEventDao = mEventDao;
        this.mAppExecutors = mAppExecutors;
    }

    public static EventDataSourceImpl getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull EventDao eventDao){
        if(INSTANCE == null) {
            synchronized (EventDataSourceImpl.class) {
                INSTANCE = new EventDataSourceImpl(appExecutors, eventDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<Event>> getEvents() {
        return mEventDao.getEvents();
    }

    @Override
    public void getEvents(@NonNull final LoadListCallback<Event> callback) {

        Runnable getEventsTask = new Runnable() {

            @Override
            public void run() {
                final List<Event> sectionList = mEventDao.getEvent();

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(sectionList.isEmpty()){
                            callback.onDataNotAvailable();
                        }else {
                            callback.onListLoaded(sectionList);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(getEventsTask);

    }

    @Override
    public void insertEvents(final Event... events) {

        Runnable insertEvent = new Runnable() {
            @Override
            public void run() {
                mEventDao.insertEvents(events);
            }
        };

        mAppExecutors.diskIO().execute(insertEvent);
    }
}
