package com.urise.webapp.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DataStreamSerializeStrategy implements SerializeStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactName, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactName, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionName, SectionBasic> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionName, SectionBasic> entry : sections.entrySet()) {
                SectionBasic section = entry.getValue();
                SectionName sectionName = entry.getKey();
                dos.writeUTF(sectionName.toString());
                switch (sectionName) {
                    case Personal:
                        doWrite(dos, (PlainText) section);
                        break;
                    case CurrentPosition:
                        doWrite(dos, (PlainText) section);
                        break;
                    case Skills:
                        doWrite(dos, (ListOfStrings) section);
                        break;
                    case Achievements:
                        doWrite(dos, (ListOfStrings) section);
                        break;
                    case Experience:
                        doWrite(dos, (Organizations) section);
                        break;
                    case Education:
                        doWrite(dos, (Organizations) section);
                        break;
                }
            }
        }
    }

    private void doWrite(DataOutputStream dos, PlainText section) throws IOException {
        dos.writeUTF(section.getField());
    }

    private void doWrite(DataOutputStream dos, ListOfStrings section) throws IOException {
        List<String> list = section.getList();
        int size = list.size();
        dos.writeInt(size);
        for (String s : list) {
            dos.writeUTF(s);
        }
    }


    private void doWrite(DataOutputStream dos, Organizations section) throws IOException {
        List<Organization> orgs = section.getOrganizations();
        int size = orgs.size();
        int count;
        dos.writeInt(size);
        for (Organization org : orgs) {
            dos.writeUTF(org.getTitle());
            dos.writeUTF(org.getHomePage().getName());
            dos.writeUTF(org.getHomePage().getUrl());
            List<Organization.DateAndText> periods = org.getPeriods();
            count = periods.size();
            dos.writeInt(count);
            for (Organization.DateAndText period : periods) {
                dos.writeUTF(period.getPosition());
                dos.writeUTF(period.getStartDate().toString());
                dos.writeUTF(period.getEndDate().toString());
                dos.writeUTF(period.getResponsibilities());
            }
        }

    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactName.valueOf(dis.readUTF()), dis.readUTF());
            }
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionName sectionName = SectionName.valueOf(dis.readUTF());
                resume.addSection(sectionName, doRead(sectionName, dis));
            }
            return resume;
        }
    }

    private SectionBasic doRead(SectionName sectionName, DataInputStream dis) throws IOException {
        SectionBasic section = null;
        switch (sectionName) {
            case Personal:
                section = doReadPlainText(dis);
                break;
            case CurrentPosition:
                section = doReadPlainText(dis);
                break;
            case Skills:
                section = doReadListOfStrings(dis);
                break;
            case Achievements:
                section = doReadListOfStrings(dis);
                break;
            case Experience:
                section = doReadOrganizations(dis);
                break;
            case Education:
                section = doReadOrganizations(dis);
                break;
        }

        return section;
    }

    private SectionBasic doReadOrganizations(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        Organizations orgs = new Organizations();
        for (int i = 0; i < size; i++) {
            orgs.addOrganization(doReadOrganization(dis));
        }
        return orgs;
    }

    private Organization doReadOrganization(DataInputStream dis) throws IOException {
        Organization org = new Organization();
        org.setTitle(dis.readUTF());
        org.setHomePage(new Link (dis.readUTF(), dis.readUTF()));
        String position;
        LocalDate startDate;
        LocalDate endDate;
        String responsibilities;
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            position = dis.readUTF();
            startDate = LocalDate.parse(dis.readUTF());
            endDate = LocalDate.parse(dis.readUTF());
            responsibilities = dis.readUTF();
            org.addPeriod(new Organization.DateAndText(position, startDate, endDate, responsibilities));
        }

        return org;
    }

    private SectionBasic doReadListOfStrings(DataInputStream dis) throws IOException {
        ListOfStrings section = new ListOfStrings();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String s = dis.readUTF();
            section.addRecord(s);
        }
        return section;
    }

    private SectionBasic doReadPlainText(DataInputStream dis) throws IOException {
        return new PlainText(dis.readUTF());

    }
}
