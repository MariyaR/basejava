package com.urise.webapp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sections {

    private Map<Section,SectionBasic> sections = new HashMap<>();

    public void addSection(Section section, SectionBasic sectionBasic) {
        sections.put(section, sectionBasic);
    }

    @Override
    public String toString(){
        StringBuffer st = new StringBuffer();
        st.append(Section.Personal.toString()).append("\n");
        st.append(sections.get(Section.Personal));


        return st.toString();
    }
}
