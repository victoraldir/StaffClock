package com.quartzo.staffclock.data.source.local;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

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
                        StaffClockDatabase.class, "StaffClock.db")
                        .addCallback(callback)
                        .build();
            }
            return INSTANCE;
        }
    }

    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            initialScript(database);
            super.onCreate(database);
        }
    };

    private static void initialScript(SupportSQLiteDatabase database){
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-25 07:00:00')");
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-25 12:00:00')");
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-25 13:00:00')");
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-25 17:00:00')");

        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-26 07:00:00')");
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-26 12:00:00')");
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-26 13:00:00')");
        database.execSQL("insert into event(type,date_time) values ('REAL','2018-07-26 17:00:00')");
    }
}
