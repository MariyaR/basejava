package com.urise.webapp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

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
        Stream.of(Section.values()).forEach(i -> appendIfExist(st, i));
        return st.toString();
    }

    private void appendIfExist(StringBuffer st, Section s) {
        if (sections.containsKey(s)) {
            st.append(sections.get(s));
        }
    }
}
