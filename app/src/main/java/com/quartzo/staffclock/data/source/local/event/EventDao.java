package com.quartzo.staffclock.data.source.local.event;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.quartzo.staffclock.data.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM Event GROUP BY datetime(date_time) ORDER BY datetime(date_time) DESC")
    LiveData<List<Event>> getEvents();

    @Query("SELECT * FROM Event")
    List<Event> getEvent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(Event... events);

}
