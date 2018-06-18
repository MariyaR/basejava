package com.urise.webapp.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializeStrategy_2 implements SerializeStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactName, String> contacts = r.getContacts();
            doWriteCollectionUTF(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            Map<SectionName, SectionBasic> sections = r.getSections();
            doWriteCollectionUTF(dos, r.getSections().entrySet(), entry -> {
                SectionBasic section = entry.getValue();
                SectionName sectionName = entry.getKey();
                dos.writeUTF(sectionName.toString());
                doWrite(dos, sectionName, section);
            });
        }
    }

    private void doWrite(DataOutputStream dos, SectionName sectionName, SectionBasic section) throws IOException {
        switch (sectionName) {
            case Personal:
                doWritePlainText(dos, (PlainText) section);
                break;
            case CurrentPosition:
                doWritePlainText(dos, (PlainText) section);
                break;
            case Skills:
                doWriteListOfStrings(dos, (ListOfStrings) section);
                break;
            case Achievements:
                doWriteListOfStrings(dos, (ListOfStrings) section);
                break;
            case Experience:
                doWriteOrganizations(dos, (Organizations) section);
                break;
            case Education:
                doWriteOrganizations(dos, (Organizations) section);
                break;
        }
    }

    private void doWritePlainText(DataOutputStream dos, PlainText section) throws IOException {
        dos.writeUTF(section.getField());
    }

    private void doWriteListOfStrings(DataOutputStream dos, ListOfStrings section) throws IOException {
        doWriteCollectionUTF(dos, section.getList(), dos::writeUTF);
    }

    private void doWriteOrganizations(DataOutputStream dos, Organizations section) throws IOException {
        doWriteCollectionUTF(dos, section.getOrganizations(), org -> {
            dos.writeUTF(org.getTitle());
            dos.writeUTF(org.getHomePage().getName());
            dos.writeUTF(org.getHomePage().getUrl());
            doWriteCollectionUTF(dos, org.getPeriods(), period -> {
                dos.writeUTF(period.getPosition());
                dos.writeUTF(period.getStartDate().toString());
                dos.writeUTF(period.getEndDate().toString());
                dos.writeUTF(period.getResponsibilities());
            });
        });
    }

    @FunctionalInterface
    private interface Writer<T> {
        void doWrite(T t) throws IOException;
    }

    private <T> void doWriteCollectionUTF(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            writer.doWrite(t);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            doReadCollectionUTF(dis, () -> {
                ContactName contactName = ContactName.valueOf(dis.readUTF());
                resume.addContact(contactName, dis.readUTF());
            });

            doReadCollectionUTF(dis, () -> {
                SectionName sectionName = SectionName.valueOf(dis.readUTF());
                resume.addSection(sectionName, doRead(sectionName, dis));
            });
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

    private SectionBasic doReadPlainText(DataInputStream dis) throws IOException {
        return new PlainText(dis.readUTF());
    }

    private SectionBasic doReadListOfStrings(DataInputStream dis) throws IOException {
        ListOfStrings section = new ListOfStrings();
        doReadCollectionUTF(dis, () -> section.addRecord(dis.readUTF()));
        return section;
    }

    private SectionBasic doReadOrganizations(DataInputStream dis) throws IOException {
        Organizations orgs = new Organizations();
        doReadCollectionUTF(dis, () -> {
            Organization org = new Organization();
            org.setTitle(dis.readUTF());
            org.setHomePage(new Link(dis.readUTF(), dis.readUTF()));
            doReadCollectionUTF(dis, () -> {
                org.addPeriod(new Organization.DateAndText(dis.readUTF(), LocalDate.parse(dis.readUTF()),
                        LocalDate.parse(dis.readUTF()), dis.readUTF()));
            });
            orgs.addOrganization(org);
        });
        return orgs;
    }

    @FunctionalInterface
    private interface Reader {
        void doRead() throws IOException;
    }

    private void doReadCollectionUTF(DataInputStream dos, Reader reader) throws IOException {
        int size = dos.readInt();
        for (int i = 0; i < size; i++) {
            reader.doRead();
        }
    }
}
