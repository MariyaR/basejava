package com.urise.webapp.model;

public class SectionBasic {

    private final SectionName section;

    SectionBasic(SectionName section) {
        this.section = section;
    }

    SectionName getSection() {
        return section;
    }
}
