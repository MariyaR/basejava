package com.urise.webapp.model;

//used for education and working exprience
public class DateAndText {

    private String startDate;
    private String endDate;
    private String field;
    private String titel;

    public DateAndText(String titel, String startDate, String endDate, String field) {
        this.titel = titel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.field = field;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(titel).append("\n");
        st.append(startDate).append(" - ").append(endDate).append(", ");
        st.append(field).append("\n");
        return st.toString();
    }
}
