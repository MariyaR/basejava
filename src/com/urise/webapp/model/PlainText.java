package com.urise.webapp.model;

//used for Personal information and Current position
public class PlainText extends SectionBasic {

    private final String Field;

    public PlainText(SectionName section, String field) {
        super(section);
        this.Field = field;
    }

    public String getField() {
        return Field;
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(this.getSection().toString()).append(": ");
        st.append(Field).append("\n");
        return st.toString();
    }
}
