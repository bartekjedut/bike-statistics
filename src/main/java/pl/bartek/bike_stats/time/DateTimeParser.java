package pl.bartek.bike_stats.time;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeParser {

    public static LocalTime createTimeFromString(String inputTime) throws IllegalArgumentException {
        String[] splited = inputTime.split(":");
        if (splited.length < 2) {
            throw new IllegalArgumentException();
        }
        int hour = Integer.parseInt(splited[0]);
        int minute = Integer.parseInt(splited[1]);
        LocalTime time = LocalTime.of(hour, minute);
        return time;
    }

    public static LocalDate createDateFromString(String inputDate) throws IllegalArgumentException {
        String[] splited = inputDate.split("-");
        if (splited.length < 3) {
            throw new IllegalArgumentException();
        }
        int year = Integer.parseInt(splited[0]);
        int month = Integer.parseInt(splited[1]);
        int day = Integer.parseInt(splited[2]);
        LocalDate date = LocalDate.of(year, month, day);
        return date;
    }
}
