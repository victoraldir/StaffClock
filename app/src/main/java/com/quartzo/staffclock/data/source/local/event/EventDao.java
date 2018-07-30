package com.quartzo.staffclock.data.source.local.event;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.quartzo.staffclock.data.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM Event WHERE type = 'REAL' GROUP BY datetime(date_time) ORDER BY datetime(date_time) DESC")
    LiveData<List<Event>> getEvents();

    @Query("SELECT * FROM Event WHERE type = :type AND date_time between :dateStart AND :dateEnd GROUP BY datetime(date_time) ORDER BY datetime(date_time) ASC")
    LiveData<List<Event>> getEventsByDate(String dateStart, String dateEnd, String type);

    @Query("SELECT * FROM Event WHERE type = 'REAL'")
    List<Event> getEvent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(Event... events);

    @Delete
    void deleteEvents(Event... events);

}
