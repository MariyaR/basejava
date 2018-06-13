package com.urise.webapp.model;

import com.urise.webapp.Util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.urise.webapp.Util.DateUtil.of;
import static com.urise.webapp.Util.DateUtil.NOW;

public class Organization implements Serializable {

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

    public void addPeriod(DateAndText period) {
        periods.add(period);
    }

    public void addPeriods(List<DateAndText> Periods) {
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

    //used for education and working experience
    public static class DateAndText implements Serializable{

        private final String position;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String responsibilities;

        public DateAndText(String title, int startYear, Month startMonth, String description) {
            this(title, of(startYear, startMonth), NOW, description);
        }

        public DateAndText(String title, int startYear, Month startMonth, int endYear, Month endMonth, String description) {
            this(title, of(startYear, startMonth), of(endYear, endMonth), description);
        }

        public DateAndText(String position, LocalDate startDate, LocalDate endDate, String field) {
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
            return startDate + " - " + endDate + "  " + position + "\n" +
                    responsibilities + "\n";
        }
    }
}
