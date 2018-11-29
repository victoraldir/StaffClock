package com.quartzo.staffclock;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.quartzo.staffclock.data.source.EventRepository;
import com.quartzo.staffclock.data.source.local.StaffClockDatabase;
import com.quartzo.staffclock.data.source.local.event.EventDataSourceImpl;
import com.quartzo.staffclock.event.EventViewModel;
import com.quartzo.staffclock.utils.AppExecutors;
import com.quartzo.staffclock.worktime.WorkTimeViewModel;

import java.util.Locale;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final EventRepository mEventRepository;

    private static Locale mLocale;

    public ViewModelFactory(EventRepository eventRepository) {
        mEventRepository = eventRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    checkNotNull(application);

                    mLocale = getCurrentLocale(application);

                    StaffClockDatabase database = StaffClockDatabase.getInstance(application);

                    AppExecutors appExecutors = new AppExecutors();

                    INSTANCE = new ViewModelFactory(EventRepository.getInstance(
                            EventDataSourceImpl.getInstance(appExecutors, database.eventDao())));
                }
            }
        }
        return INSTANCE;
    }

    private static Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(EventViewModel.class)) {
            return (T) new EventViewModel(mEventRepository);
        }else if (modelClass.isAssignableFrom(WorkTimeViewModel.class)) {
            return (T) new WorkTimeViewModel(mEventRepository, mLocale);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}