package com.quartzo.staffclock.interfaces;

import com.quartzo.staffclock.data.Event;

public interface Callbacks {
    interface WorkTimeCallback{
        void onWorkTimeClicked(String date, String workedTime);
    }

    interface EventCallback{
        void onLongEventClicked(Event event);
    }
}
