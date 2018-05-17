package com.urise.webapp;

import com.urise.webapp.model.*;

public class MainResumeTest {

    public static void main(String[] args) {

        Resume resume = new Resume("Ivan Petrov");
        Contacts contacts = new Contacts();
        contacts.addContact(Contact.Mail, "IvanPetrov@google.com");
        contacts.addContact(Contact.Skype, "IvanPetrov");
        contacts.addContact(Contact.PhoneNumber, "123456789");
        resume.setContacts(contacts);

        PlainText personal = new PlainText(Section.Personal);
        personal.setField("Architecture purist");

        PlainText currentPosition = new PlainText(Section.CurrentPosition);
        currentPosition.setField("architector");

        ListOfStrings skills = new ListOfStrings(Section.Skills);
        skills.addRecord("java");
        skills.addRecord("c++");
        skills.addRecord("hadoop");

        DateAndText work1 = new DateAndText("employer1", "01-01-2000", "01-01-2005", "some responsibilities");
        DateAndText work2 = new DateAndText("employer2", "01-01-2005", "01-01-2010", "some responsibilities");
        DateAndText work3 = new DateAndText("employer3", "01-01-2010", "01-01-2015", "some responsibilities");

        ListOfDateAndText workingExperience = new ListOfDateAndText(Section.Experience);
        workingExperience.addRecord(work1);
        workingExperience.addRecord(work2);
        workingExperience.addRecord(work3);

        Sections sections = new Sections();
        sections.addSection(personal).addSection(currentPosition).addSection(skills).addSection(workingExperience);
        resume.setSections(sections);

        System.out.println(resume);
    }
}