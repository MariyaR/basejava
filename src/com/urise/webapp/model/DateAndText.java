package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

//used for education and working experience
public class DateAndText {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String responsibilities;

    public DateAndText( LocalDate startDate, LocalDate endDate, String field) {
        Objects.requireNonNull(startDate, "Start date can not be null");
        Objects.requireNonNull(endDate, "End date can not be null");
        Objects.requireNonNull(field, "Field can not be null");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateAndText that = (DateAndText) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(responsibilities, that.responsibilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, responsibilities);
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate + ", " +
                responsibilities + "\n";
    }
}
