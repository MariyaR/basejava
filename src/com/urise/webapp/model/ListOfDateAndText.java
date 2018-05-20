package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//used for education and working experience
public class ListOfDateAndText extends SectionBasic {

    private List<DateAndText> list = new ArrayList<>();

    public ListOfDateAndText(SectionName section) {
        super(section);
    }

    public void addRecord(DateAndText dateAndText) {
        list.add(dateAndText);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListOfDateAndText that = (ListOfDateAndText) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override

    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(this.getSection().toString()).append(":\n");
        list.forEach(st::append);
        return st.toString();
    }
}
