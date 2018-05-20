package com.urise.webapp.model;

import java.util.Objects;

//used for education and working experience
public class DateAndText {

    private final String startDate;
    private final String endDate;
    private final String field;
    private final String title;

    public DateAndText(String title, String startDate, String endDate, String field) {
        Objects.requireNonNull(title, "Title can not be null");
        Objects.requireNonNull(startDate, "Start date can not be null");
        Objects.requireNonNull(endDate, "End date can not be null");
        Objects.requireNonNull(field, "Field can not be null");
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.field = field;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getField() {
        return field;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateAndText that = (DateAndText) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(field, that.field) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, field, title);
    }

    @Override
    public String toString() {
        return title + "\n" +
                startDate + " - " + endDate + ", " +
                field + "\n";
    }
}
