package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

public abstract class AbstractStorageTest {

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    protected Storage storage;

    protected static final String DIRECTORY = "./src/com/urise/webapp/TestDir";

    protected static final String UUID_1 = UUID.randomUUID().toString();
    protected static final String UUID_2 = UUID.randomUUID().toString();
    protected static final String UUID_3 = UUID.randomUUID().toString();
    protected static final String UUID_4 = UUID.randomUUID().toString();
    protected static final String UUID_5 = UUID.randomUUID().toString();
    protected static final String DUMMY = UUID.randomUUID().toString();
    protected static final Resume RESUME_1 = new Resume(UUID_1, "Aleksandrov Ivan");
    protected static final Resume RESUME_2 = new Resume(UUID_2, "Ivanov Ivan");
    protected static final Resume RESUME_3 = new Resume(UUID_3, "Petrov Ivan");
    protected static final Resume RESUME_4 = new Resume(UUID_4, "Sokolov Aleksandr");
    protected static final Resume RESUME_5 = new Resume(UUID_5, "Sokolov Ivan");
    protected static final Resume RESUME_DUMMY = new Resume(DUMMY);

    {
        EnumMap<ContactName, String> contacts = new EnumMap<ContactName, String>(ContactName.class);
        contacts.put(ContactName.Mail, "IvanPetrov@google.com");
        contacts.put(ContactName.Skype, "IvanPetrov");
        contacts.put(ContactName.PhoneNumber, "123456789");
        RESUME_3.setContacts(contacts);

        PlainText personal = new PlainText("Architecture purist");

        PlainText currentPosition = new PlainText("architector");

        ListOfStrings skills = new ListOfStrings();
        skills.addRecord("java");
        skills.addRecord("c++");
        skills.addRecord("hadoop");

        Organization.DateAndText job1 = new Organization.DateAndText("position1", LocalDate.parse("2000-01-01"), LocalDate.parse("2005-01-01"), "some responsibilities");
        Organization.DateAndText job2 = new Organization.DateAndText( "position2", LocalDate.parse("2005-01-01"), LocalDate.parse("2010-01-01"), "some responsibilities");
        Organization.DateAndText job3 = new Organization.DateAndText( "position3", LocalDate.parse("2010-01-01"), LocalDate.parse("2015-01-01"), "some responsibilities");
        Organization.DateAndText job4 = new Organization.DateAndText( "position4", LocalDate.parse("2015-01-01"), LocalDate.parse("2017-01-01"), "some other responsibilities");

        Organization org1 = new Organization("employer1", job1);
        org1.setHomePage(new Link ("name", "url1"));
        Organization org2 = new Organization("employer2", job2);
        org2.setHomePage(new Link("name","url2"));
        Organization org3 = new Organization("employer3", Arrays.asList(job3, job4));
        org3.setHomePage(new Link("name", "url3"));
        Organizations workingExperience = new Organizations(Arrays.asList(org1,org2,org3));

        EnumMap<SectionName, SectionBasic> sections = new EnumMap<SectionName, SectionBasic>(SectionName.class);
        sections.put(SectionName.Personal, personal);
        sections.put(SectionName.CurrentPosition, currentPosition);
        sections.put(SectionName.Skills, skills);
        sections.put(SectionName.Achievements, skills);
        sections.put(SectionName.Experience, workingExperience);
        sections.put(SectionName.Education, workingExperience);
        RESUME_2.setSections(sections);
        RESUME_1.setSections(sections);
        RESUME_3.setSections(sections);
    }



    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_3);
        storage.save(RESUME_2);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        testSize(0);

    }

    @Test
    public void delete() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_3};
        storage.delete(UUID_2);
        Arrays.sort(expectedStorage, RESUME_COMPARATOR);
        Assert.assertArrayEquals(expectedStorage, getStorageAsArray());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() throws Exception {
        storage.delete(DUMMY);
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void getAllSorted() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_2, RESUME_3};
        Arrays.sort(expectedStorage, RESUME_COMPARATOR);
        Assert.assertArrayEquals(expectedStorage, getStorageAsArray());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistException() throws Exception {
        storage.get(DUMMY);
    }

    @Test
    public void save() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_2, RESUME_3, RESUME_4};
        storage.save(RESUME_4);
        Arrays.sort(expectedStorage, RESUME_COMPARATOR);
        Assert.assertArrayEquals(expectedStorage, getStorageAsArray());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistException() throws Exception {
        storage.save(RESUME_1);
    }

    @Test
    public void size() throws Exception {
        testSize(3);
        Assert.assertEquals(storage.size(), getStorageAsArray().length);
    }

    @Test
    public void update() throws Exception {
        storage.update(RESUME_3);
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test(expected = StorageException.class)
    public void updateNotExistException() throws Exception {
        storage.update(RESUME_DUMMY);
    }

    protected Resume[] getStorageAsArray() {
        return storage.getAllSorted().toArray(new Resume[storage.size()]);
    }

    protected void testSize(int expectedSize) {
        Assert.assertEquals(expectedSize, storage.size());
    }

}