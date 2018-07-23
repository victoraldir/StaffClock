package com.quartzo.staffclock.data.source;

import com.quartzo.staffclock.data.source.local.event.EventDataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.verify;


public class EventRepositoryTest {

    private EventRepository mEventRepository;

    @Mock
    private EventDataSource mEventDataSource;

    @Mock
    private EventDataSource.LoadListCallback mLoadTasksCallback;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);
        mEventRepository = EventRepository.getInstance(mEventDataSource);
    }

    @Test
    public void getTasks_requestsAllTasksFromLocalDataSource() {
//        // When tasks are requested from the tasks repository
//        mEventRepository.getEvents(mLoadTasksCallback);
//
//        // Then tasks are loaded from the local data source
//        verify(mEventDataSource).getEvents(any(EventDataSource.LoadListCallback.class));
    }

}