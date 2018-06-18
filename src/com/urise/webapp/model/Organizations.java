package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organizations extends SectionBasic {
    private static final long serialVersionUID = 1L;

    private List<Organization> organizations = new ArrayList<>();

    public Organizations() {}

    public Organizations(Organization... organizations) {
        this(Arrays.asList(organizations));
    }

    public Organizations(List<Organization> experience) {
        this.organizations = experience;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public void addOrganization (Organization org) {
        organizations.add(org);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organizations that = (Organizations) o;
        return Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        int size = organizations.size();
        String st="";
        for (int i=0; i< size; i++) {
            st = st + organizations.get(i).toString();
        }
        return st;
    }
}
