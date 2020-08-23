package pl.bartek.bike_stats.model;

import pl.bartek.bike_stats.exception.CreateRecordException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Record {
    private int id;
    private String startEndPoint;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private double distance;
    private int breakTime;

    public Record(String startEndPoint, LocalDate date, LocalTime startTime, LocalTime endTime, double distance, int breakTime) throws CreateRecordException {
        if(startEndPoint.isEmpty() || breakTime < 0) {
            throw new CreateRecordException("Wprowadzono niepoprawne dane lub nie wprowadzono wszystkich danych");
        }

        this.startEndPoint = startEndPoint;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.breakTime = breakTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartEndPoint() {
        return startEndPoint;
    }

    public void setStartEndPoint(String startEndPoint) {
        this.startEndPoint = startEndPoint;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public double calculateAverageSpeed(){
        Duration duration = Duration.between(startTime, endTime);
        long durationMinutes = duration.toMinutes();
        durationMinutes -= breakTime;
        return distance * 60 / durationMinutes;
    }


    @Override
    public String toString() {
        String averageSpeed = String.format("%.2f", calculateAverageSpeed());
        return "Miejscowości: " + startEndPoint + "\nData: " + date + "  " + " Godzina:" + startTime + " - " + endTime
                + "\nCzas przerw: " + breakTime + "     Dystans: " + distance + "     Średnia prędkość: " + averageSpeed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return id == record.id &&
                Double.compare(record.distance, distance) == 0 &&
                breakTime == record.breakTime &&
                Objects.equals(startEndPoint, record.startEndPoint) &&
                Objects.equals(date, record.date) &&
                Objects.equals(startTime, record.startTime) &&
                Objects.equals(endTime, record.endTime);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, startEndPoint, date, startTime, endTime, distance, breakTime);
    }

}
