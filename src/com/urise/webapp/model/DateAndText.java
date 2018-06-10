package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

//used for education and working experience
public class DateAndText {

    private final String position;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String responsibilities;

    public DateAndText( String position, LocalDate startDate, LocalDate endDate, String field) {
        Objects.requireNonNull(position, "Position can not be null");
        Objects.requireNonNull(startDate, "Start date can not be null");
        Objects.requireNonNull(endDate, "End date can not be null");
        Objects.requireNonNull(field, "Field can not be null");
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.responsibilities = field;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateAndText that = (DateAndText) o;
        return Objects.equals(position, that.position) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(responsibilities, that.responsibilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, startDate, endDate, responsibilities);
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate + "  " + position + "\n"+
                responsibilities + "\n";
    }
}
