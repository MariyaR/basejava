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

        PlainText personal = new PlainText(SectionName.Personal, "Architecture purist");

        PlainText currentPosition = new PlainText(SectionName.CurrentPosition, "architector");

        ListOfStrings skills = new ListOfStrings(SectionName.Skills);
        skills.addRecord("java");
        skills.addRecord("c++");
        skills.addRecord("hadoop");

        DateAndText work1 = new DateAndText("employer1", LocalDate.parse("2000-01-01"), LocalDate.parse("2005-01-01"), "some responsibilities");
        DateAndText work2 = new DateAndText("employer2", LocalDate.parse("2005-01-01"), LocalDate.parse("2010-01-01"), "some responsibilities");
        DateAndText work3 = new DateAndText("employer3", LocalDate.parse("2010-01-01"), LocalDate.parse("2015-01-01"), "some responsibilities");

        ListOfDateAndText workingExperience = new ListOfDateAndText(SectionName.Experience);
        workingExperience.addRecord(work1);
        workingExperience.addRecord(work2);
        workingExperience.addRecord(work3);

        EnumMap<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);
        sections.put(personal.getSection(), personal);
        sections.put(currentPosition.getSection(),currentPosition);
        sections.put(skills.getSection(),skills);
        sections.put(workingExperience.getSection(),workingExperience);
        resume.setSections(sections);
        System.out.println(resume);
    }
}