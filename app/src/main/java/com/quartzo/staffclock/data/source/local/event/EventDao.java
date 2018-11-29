package com.quartzo.staffclock.data.source.local.event;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.quartzo.staffclock.data.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM Event GROUP BY datetime(date_time) ORDER BY datetime(date_time) DESC")
    LiveData<List<Event>> getEvents();

    @Query("SELECT * FROM Event WHERE date_time between :dateStart AND :dateEnd GROUP BY datetime(date_time) ORDER BY datetime(date_time) ASC")
    LiveData<List<Event>> getEventsByDate(String dateStart, String dateEnd);

    @Query("SELECT * FROM Event WHERE type = 'CAMERA' OR type = 'MANUAL'")
    List<Event> getEvent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(Event... events);

    @Delete
    void deleteEvents(Event... events);

}
