package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Organization {

    private final String title;
    private List<DateAndText> periods = new ArrayList<>();

    public Organization(String title) {
        this.title = title;
    }

    public Organization(String title, DateAndText period) {
        this(title);
        periods.add(period);
    }
    public Organization(String title, List<DateAndText> periods) {
        this(title);
        this.periods = periods;
    }

    public void setPeriods(List<DateAndText> periods) {
        this.periods = periods;
    }

    public String getTitle() {
        return title;
    }

    public List<DateAndText> getPeriods() {
        return periods;
    }

    public void addPeriod (DateAndText period) {
        periods.add(period);
    }

    public void addPeriods (List<DateAndText> Periods) {
        periods.addAll(Periods);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(periods, that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, periods);
    }

    @Override
    public String toString() {
        ArrayList<DateAndText> sortedPeriods = new ArrayList<>(periods);
        sortedPeriods.sort(Comparator.comparing(DateAndText::getStartDate).reversed());
        return title + '\n' + sortedPeriods;
    }
}
