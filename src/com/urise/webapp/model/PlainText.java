package com.urise.webapp.model;

//used for Personal information and Current position
public class PlainText extends SectionBasic {

    private String Field;

    public PlainText(Section section) {
        super(section);
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(this.getSection().toString()).append("\n");
        st.append(Field).append("\n");
        return st.toString();
    }
}
