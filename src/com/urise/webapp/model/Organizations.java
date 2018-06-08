package com.urise.webapp.model;

import java.util.*;

public class Organizations extends SectionBasic {

    private List<Organization> experience = new ArrayList<>();

    public void addRecord(Organization organization) {

        if (experience.size() == 0) {
            experience.add(organization);
        } else if (!experience.get(experience.size() - 1).equals(organization)) {
            experience.add(organization);
        } else {
            List<DateAndText> list = organization.getPeriods();
            int last = experience.size() - 1;
            experience.get(last).addPeriods(list);
        }
    }

    @Override
    public String toString() {
        return experience.toString();
    }
}
