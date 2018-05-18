package com.urise.webapp.model;

//used for education and working experience
public class DateAndText {

    private final String startDate;
    private final String endDate;
    private final String field;
    private final String title;

    public DateAndText(String title, String startDate, String endDate, String field) {
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
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(title).append("\n");
        st.append(startDate).append(" - ").append(endDate).append(", ");
        st.append(field).append("\n");
        return st.toString();
    }
}
