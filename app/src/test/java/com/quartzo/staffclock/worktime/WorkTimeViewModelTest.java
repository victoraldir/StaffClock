package com.quartzo.staffclock.worktime;

import android.content.Context;

import com.quartzo.staffclock.data.source.EventRepository;
import com.quartzo.staffclock.exceptions.DateTimeNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkTimeViewModelTest {

    @Mock
    private EventRepository mEventRepository;

    private WorkTimeViewModel mWorkTimeViewModel;

    @Before
    public void setupAddEditTaskViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mWorkTimeViewModel = new WorkTimeViewModel(mEventRepository, Locale.getDefault());
    }


    @Test
    public void shouldGetDateTimeFormated(){

        mWorkTimeViewModel.setDate(1,Calendar.JUNE,1990);
        mWorkTimeViewModel.setTime(15,12,13);

        String dateTime = mWorkTimeViewModel.getDateTime();

        assertNotNull(dateTime);
        assertEquals("1990-06-01 15:12:13",dateTime);

    }

    @Test(expected = DateTimeNotFoundException.class)
    public void shouldThrowDateTimeNotFoundException() throws DateTimeNotFoundException {
        mWorkTimeViewModel.saveEvent();
    }

    @Test
    public void shouldSaveEvent() throws DateTimeNotFoundException {

        mWorkTimeViewModel.setDate(1,Calendar.JUNE,1990);
        mWorkTimeViewModel.setTime(15,12,13);

        mWorkTimeViewModel.saveEvent();

        verify(mEventRepository).insertEvents(anyObject());

    }
}