package com.quartzo.staffclock.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.data.source.local.event.EventDao;

@Database(entities = {Event.class}, version = 1)
public abstract class StaffClockDatabase extends RoomDatabase {

    private static StaffClockDatabase INSTANCE;

    public abstract EventDao eventDao();

    private static final Object sLock = new Object();

    public static StaffClockDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        StaffClockDatabase.class, "CiroSoundBoard.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
