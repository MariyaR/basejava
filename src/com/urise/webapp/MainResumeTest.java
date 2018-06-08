package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.util.EnumMap;

public class MainResumeTest {

    public static void main(String[] args) {

        Resume resume = new Resume("Ivan Petrov");
        EnumMap <ContactName, String> contacts = new EnumMap<ContactName, String>(ContactName.class);
        contacts.put(ContactName.Mail, "IvanPetrov@google.com");
        contacts.put(ContactName.Skype, "IvanPetrov");
        contacts.put(ContactName.PhoneNumber, "123456789");
        resume.setContacts(contacts);

        PlainText personal = new PlainText( "Architecture purist");

        PlainText currentPosition = new PlainText( "architector");

        ListOfStrings skills = new ListOfStrings();
        skills.addRecord("java");
        skills.addRecord("c++");
        skills.addRecord("hadoop");

        DateAndText job1 = new DateAndText( LocalDate.parse("2000-01-01"), LocalDate.parse("2005-01-01"), "some responsibilities");
        DateAndText job2 = new DateAndText( LocalDate.parse("2005-01-01"), LocalDate.parse("2010-01-01"), "some responsibilities");
        DateAndText job3 = new DateAndText( LocalDate.parse("2010-01-01"), LocalDate.parse("2015-01-01"), "some responsibilities");
        DateAndText job4 = new DateAndText( LocalDate.parse("2015-01-01"), LocalDate.parse("2017-01-01"), "some other responsibilities");

        Organization org1 = new Organization("employer1", job1);
        Organization org2 = new Organization("employer2", job2);
        Organization org3 = new Organization("employer3", job3);
        Organization org4 = new Organization("employer3", job4);

        Organizations workingExperience = new Organizations();
        workingExperience.addRecord(org1);
        workingExperience.addRecord(org2);
        workingExperience.addRecord(org3);
        workingExperience.addRecord(org4);

        EnumMap<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);
        sections.put(SectionName.Personal, personal);
        sections.put(SectionName.CurrentPosition,currentPosition);
        sections.put(SectionName.Skills,skills);
        sections.put(SectionName.Experience,workingExperience);
        resume.setSections(sections);
        System.out.println(resume);
    }
}