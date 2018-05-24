package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//used for Skills and Achievements
public class ListOfStrings extends SectionBasic {

    private List<String> list = new ArrayList<>();

    public void addRecord(String record) {
        list.add(record);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListOfStrings that = (ListOfStrings) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        list.forEach(i -> st.append(i).append(", "));
        st.append("\n");

        return st.toString();
    }

}
