package com.urise.webapp.model;

import java.util.*;

public class Organizations extends SectionBasic {

    private List<Organization> experience = new ArrayList<>();

    public Organizations(List<Organization> experience) {
        this.experience = experience;
    }

    //    public void addRecord(Organization organization) {

//        if (experience.size() == 0) {
//            experience.add(organization);
//        } else if (!experience.get(experience.size() - 1).getTitle().
//                equals(organization.getTitle())) {
//            experience.add(organization);
//        } else {
//            List<DateAndText> list = organization.getPeriods();
//            int last = experience.size() - 1;
//            experience.get(last).addPeriods(list);
//        }
//    }

    public List<Organization> getExperience() {
        return experience;
    }

    public void setExperience(List<Organization> experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organizations that = (Organizations) o;
        return Objects.equals(experience, that.experience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(experience);
    }

    @Override
    public String toString() {
        return experience.toString();
    }
}
