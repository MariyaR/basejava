package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;


    public static final Resume EMPTY = new Resume();

    static {
        EMPTY.addSection(SectionName.CurrentPosition, PlainText.EMPTY);
        EMPTY.addSection(SectionName.Personal, PlainText.EMPTY);
        EMPTY.addSection(SectionName.Achievements, ListOfStrings.EMPTY);
        EMPTY.addSection(SectionName.Skills, ListOfStrings.EMPTY);
        EMPTY.addSection(SectionName.Experience, new Organizations(Organization.EMPTY));
        EMPTY.addSection(SectionName.Education, new Organizations(Organization.EMPTY));
    }

    private String uuid;
    private String fullName;
    private Map<ContactName, String> contacts = new EnumMap<ContactName, String>(ContactName.class);
    private Map<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);

    public Resume() {
    }

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

    public Map<ContactName, String> getContacts() {
        return contacts;
    }

    public void setContacts(EnumMap<ContactName, String> contacts) {
        this.contacts = contacts;
    }

    public String getContact(ContactName name) {
        return contacts.get(name);
    }

    public Map<SectionName, SectionBasic> getSections() {
        return sections;
    }

    public String getSection(SectionName name) {

        return sections.get(name)!=null ? sections.get(name).toString() : "";
    }

    public SectionBasic getSectionByName (SectionName name) {
        return sections.get(name);
    }

    public void setSections(EnumMap<SectionName, SectionBasic> sections) {
        this.sections = sections;
    }

    public void addContact(ContactName type, String value) {
        contacts.put(type, value);
    }

    public void addSection(SectionName type, SectionBasic section) {
        sections.put(type, section);
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
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("Resume{").append("uuid='").append(uuid).append('\'').append(", fullName='").append(fullName)
                .append('\'').append('}').append("\n");
        Stream.of(ContactName.values()).forEach(i -> appendIfExist(st, i));
        st.append("\n");
        Stream.of(SectionName.values()).forEach(i -> appendIfExist(st, i));
        return st.toString();
    }

    private void appendIfExist(StringBuilder st, ContactName c) {
        if (contacts.containsKey(c)) {

            st.append(c.getContent()).append(contacts.get(c)).append("\n");
        }
    }

    private void appendIfExist(StringBuilder st, SectionName s) {
        if (sections.containsKey(s)) {
            st.append(s.getContent());
            st.append(sections.get(s));
        }
    }
}
