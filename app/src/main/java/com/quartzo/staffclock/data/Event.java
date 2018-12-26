package com.quartzo.staffclock.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity(tableName = "event")
public class Event {

    @NonNull
    private String type;

    @NonNull
    @ColumnInfo(name = "date_time")
    @PrimaryKey
    private String dateTime;

    @ColumnInfo(name = "timeComma")
    private String timeComma;

    @Ignore
    public Event(@NonNull String type, @NonNull String dateTime) {
        this.type = type;
        this.dateTime = dateTime;
    }

    public Event(@NonNull String type, @NonNull String dateTime, @NonNull String timeComma) {
        this.type = type;
        this.dateTime = dateTime;
        this.timeComma = timeComma;
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

    public String getTimeComma() {
        return timeComma;
    }

    public void setTimeComma(String timeComma) {
        this.timeComma = timeComma;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
