package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

//used for Skills and Achievements
public class ListOfStrings extends SectionBasic {

    private List<String> list = new ArrayList<>();

    public ListOfStrings(Section section) {
        super(section);
    }

    public void addRecord(String record) {
        list.add(record);
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(this.getSection().toString()).append("\n");
        list.forEach(i -> st.append(i).append("\n"));
        return st.toString();
    }

}
