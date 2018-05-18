package com.urise.webapp.model;

import com.urise.webapp.exception.ModelException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Sections {

    private Map<SectionName, SectionBasic> sections = new HashMap<>();

    public Sections addSection(SectionBasic sectionBasic) {
        sections.put(sectionBasic.getSection(), sectionBasic);
        return this;
    }

    public Sections updateSection(SectionBasic sectionBasic) {
        checkKeyIfExist(sectionBasic.getSection());
        sections.put(sectionBasic.getSection(), sectionBasic);
        return this;
    }

    public void removeSection(SectionName sectionName) {
        checkKeyIfExist(sectionName);
        sections.remove(sectionName);
    }

    public SectionBasic getSection(SectionName sectionName) {
        checkKeyIfExist(sectionName);
        return sections.get(sectionName);
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        Stream.of(SectionName.values()).forEach(i -> appendIfExist(st, i));
        return st.toString();
    }

    private void appendIfExist(StringBuffer st, SectionName s) {
        if (sections.containsKey(s)) {
            st.append(sections.get(s));
        }
    }

    private void checkKeyIfExist(SectionName section) {
        if (!sections.containsKey(section)) {
            throw new ModelException("this resume does not contain the required section");
        }
    }

    private void checkKeyIfNotExist (SectionName sectionName) {
        if (sections.containsKey(sectionName)) {
            throw new ModelException("this resume already contains the required section");
        }
    }
}
