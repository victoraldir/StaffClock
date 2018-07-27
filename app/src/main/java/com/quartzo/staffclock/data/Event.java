package com.quartzo.staffclock.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity(tableName = "event")
public class Event {

    @NonNull
    private final String type;

    @NonNull
    @ColumnInfo(name = "date_time")
    @PrimaryKey
    private final String dateTime;

    public Event(@NonNull String type, @NonNull String dateTime) {
        this.type = type;
        this.dateTime = dateTime;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getDateTime() {
        return dateTime;
    }

    public DateTime getParseDateTime(){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(this.dateTime);
        return dt;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
