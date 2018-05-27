package com.urise.webapp.model;

import java.util.*;

//used for education and working experience
public class ListOfDateAndText extends SectionBasic {

    private List<DateAndText> list = new ArrayList<>();

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
        StringBuilder st = new StringBuilder();
        List<DateAndText> sortedList = new ArrayList<>(list);
        sortedList.sort(Comparator.comparing(DateAndText::getStartDate).reversed());
        ListIterator<DateAndText> it = sortedList.listIterator();
        String title = "dummy";
        while (it.hasNext()) {
            DateAndText dat = it.next();
            if (dat.getTitle().equals(title)) {
                st.append(dat.toStringNoTitle());
            } else {
                st.append(dat.toString());
                title = dat.getTitle();
            }
        }
        return st.toString();
    }
}
