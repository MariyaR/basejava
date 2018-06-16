package com.urise.webapp.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DataStreamSerializeStrategy implements SerializeStrategy {

    private static final String PLAIN_TEXT = "PlainText";
    private static final String LIST_OF_STRINGS = "ListOfStrings";
    private static final String ORGANIZATIONS = "Organizations";

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
                String sectionName = section.getClass().getSimpleName();
                dos.writeUTF(sectionName);
                dos.writeUTF(entry.getKey().name());
                switch (sectionName) {
                    case PLAIN_TEXT:
                        doWrite(dos, (PlainText) section);
                        break;
                    case LIST_OF_STRINGS:
                        doWrite(dos, (ListOfStrings) section);
                        break;
                    case ORGANIZATIONS:
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
        for (int i = 0; i < size; i++) {
            String s = list.get(i);
            dos.writeUTF(s);
        }
    }


    private void doWrite(DataOutputStream dos, Organizations section) throws IOException {
        List<Organization> orgs = section.getOrganizations();
        int size = orgs.size();
        int count;
        dos.writeInt(size);
        for (int i = 0; i < size; i++) {
            Organization org = orgs.get(i);
            dos.writeUTF(org.getTitle());
            List<Organization.DateAndText> periods = org.getPeriods();
            count = periods.size();
            dos.writeInt(count);
            for (int j = 0; j < count; j++) {
                Organization.DateAndText period = periods.get(j);
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
                String className = dis.readUTF();
                resume.addSection(SectionName.valueOf(dis.readUTF()), doRead(className, dis));
            }
            return resume;
        }
    }

    private SectionBasic doRead(String className, DataInputStream dis) throws IOException {
        SectionBasic section = null;
        switch (className) {
            case PLAIN_TEXT:
                section = doReadPlainText(dis);
                break;
            case LIST_OF_STRINGS:
                section = doReadListOfStrings(dis);
                break;
            case ORGANIZATIONS:
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
