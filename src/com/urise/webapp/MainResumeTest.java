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

        DateAndText work1 = new DateAndText("employer1", LocalDate.parse("2000-01-01"), LocalDate.parse("2005-01-01"), "some responsibilities");
        DateAndText work2 = new DateAndText("employer2", LocalDate.parse("2005-01-01"), LocalDate.parse("2010-01-01"), "some responsibilities");
        DateAndText work3 = new DateAndText("employer3", LocalDate.parse("2010-01-01"), LocalDate.parse("2015-01-01"), "some responsibilities");
        DateAndText work4  = new DateAndText("employer3", LocalDate.parse("2015-01-01"), LocalDate.parse("2017-01-01"), "some other responsibilities");

        ListOfDateAndText workingExperience = new ListOfDateAndText();
        workingExperience.addRecord(work4);
        workingExperience.addRecord(work3);
        workingExperience.addRecord(work2);
        workingExperience.addRecord(work1);

        EnumMap<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);
        sections.put(SectionName.Personal, personal);
        sections.put(SectionName.CurrentPosition,currentPosition);
        sections.put(SectionName.Skills,skills);
        sections.put(SectionName.Experience,workingExperience);
        resume.setSections(sections);
        System.out.println(resume);
    }
}