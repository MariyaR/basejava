package com.urise.webapp;

import com.urise.webapp.serialization.DataStreamSerializeStrategy;
import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
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

        Organization.DateAndText job1 = new Organization.DateAndText( "position1", LocalDate.parse("2000-01-01"), LocalDate.parse("2005-01-01"), "some responsibilities");
        Organization.DateAndText job2 = new Organization.DateAndText( "position2", LocalDate.parse("2005-01-01"), LocalDate.parse("2010-01-01"), "some responsibilities");
        Organization.DateAndText job3 = new Organization.DateAndText( "position3", LocalDate.parse("2010-01-01"), LocalDate.parse("2015-01-01"), "some responsibilities");
        Organization.DateAndText job4 = new Organization.DateAndText( "position4", LocalDate.parse("2015-01-01"), LocalDate.parse("2017-01-01"), "some other responsibilities");

        Organization org1 = new Organization("employer1", job1);
        Organization org2 = new Organization("employer2", job2);
        Organization org3 = new Organization("employer3", Arrays.asList(job3, job4));

        Organizations workingExperience = new Organizations(Arrays.asList(org1,org2,org3));


        EnumMap<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);
        sections.put(SectionName.Personal, personal);
        sections.put(SectionName.CurrentPosition,currentPosition);
        sections.put(SectionName.Skills,skills);
        sections.put(SectionName.Experience,workingExperience);
        resume.setSections(sections);
        //System.out.println(resume);

        Resume r = new Resume();

        DataStreamSerializeStrategy sr = new DataStreamSerializeStrategy();
        try (FileOutputStream fos = new FileOutputStream(new File("./src/com/urise/webapp/TestDir/test"));
             FileInputStream fis = new FileInputStream(new File("./src/com/urise/webapp/TestDir/test") )){

            sr.doWrite(resume,fos);
            r = (Resume) sr.doRead(fis);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}