package com.urise.webapp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sections {

    private Map<Section, SectionBasic> sections = new HashMap<>();

    public void addSection(SectionBasic sectionBasic) {
        sections.put(sectionBasic.getSection(), sectionBasic);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public String getSection(Section section) {
        return sections.get(section).toString();
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append(Section.Personal.toString()).append("\n");
        st.append(sections.get(Section.Personal));


        return st.toString();
    }
}
