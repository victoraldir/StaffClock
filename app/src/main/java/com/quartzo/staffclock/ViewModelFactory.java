package com.quartzo.staffclock;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.quartzo.staffclock.data.source.EventRepository;
import com.quartzo.staffclock.data.source.local.StaffClockDatabase;
import com.quartzo.staffclock.data.source.local.event.EventDataSourceImpl;
import com.quartzo.staffclock.event.EventViewModel;
import com.quartzo.staffclock.utils.AppExecutors;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final EventRepository mEventRepository;

    public ViewModelFactory(EventRepository eventRepository) {
        mEventRepository = eventRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    checkNotNull(application);

                    StaffClockDatabase database = StaffClockDatabase.getInstance(application);

                    AppExecutors appExecutors = new AppExecutors();

                    INSTANCE = new ViewModelFactory(EventRepository.getInstance(
                            EventDataSourceImpl.getInstance(appExecutors, database.eventDao())));
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(EventViewModel.class)) {
            return (T) new EventViewModel(mEventRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}