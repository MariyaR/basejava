package com.urise.webapp.model;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumMap;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class Resume implements Comparable<Resume> {

    private final String uuid;
    private String fullName;
    private EnumMap<ContactName, String> contacts = new EnumMap<ContactName, String>(ContactName.class);
    private EnumMap<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(fullName, "fullName can not be null");
        Objects.requireNonNull(uuid, "uuid can not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public EnumMap<ContactName, String> getContacts() {
        return contacts;
    }

    public void setContacts(EnumMap<ContactName, String> contacts) {
        this.contacts = contacts;
    }

    public EnumMap<SectionName, SectionBasic> getSections() {
        return sections;
    }

    public void setSections(EnumMap<SectionName, SectionBasic> sections) {
        this.sections = sections;
    }

    @Override
    public int compareTo(Resume o) {
        if (fullName.equals(o.fullName)) {
            return uuid.compareTo(o.uuid);
        }
        return fullName.compareTo(o.getFullName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append("Resume{").append("uuid='").append(uuid).append('\'').append(", fullName='").append(fullName)
                .append('\'').append('}').append("\n");
        Stream.of(ContactName.values()).forEach(i -> appendIfExist(st, i));
        st.append("\n");
        Stream.of(SectionName.values()).forEach(i -> appendIfExist(st, i));
        return st.toString();
    }

    private void appendIfExist(StringBuffer st, ContactName c) {
        if (contacts.containsKey(c)) {

            st.append(c.toString()).append(contacts.get(c)).append("\n");
        }
    }

    private void appendIfExist(StringBuffer st, SectionName s) {
        if (sections.containsKey(s)) {
            st.append(sections.get(s));
        }
    }
}
