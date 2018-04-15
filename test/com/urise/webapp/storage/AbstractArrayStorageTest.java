package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractArrayStorageTest {

    protected Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }


    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void delete() throws Exception {
        Resume[] expectedStorage = {new Resume(UUID_1), new Resume(UUID_3)};
        storage.delete(UUID_2);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() throws Exception {
        storage.delete("uuid7");
    }

    @Test
    public void get() throws Exception {
        Resume expectedResume = new Resume(UUID_2);
        Assert.assertEquals(storage.get(UUID_2), expectedResume);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expectedStorage = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistException() throws Exception {
        storage.get("uuid7");
    }

    @Test
    public void save() throws Exception {
        Resume resume = new Resume("uuid4");
        storage.save(resume);
        Resume[] expectedStorage = {new Resume(UUID_1), new Resume(UUID_2),
                new Resume(UUID_3), resume};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistException() throws Exception {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() throws Exception {
        for (int i = 3; i < storage.getStorageLimit(); i++) {
            storage.save(new Resume("uuid" + (i + 1)));
        }
        storage.save(new Resume("uuid1001"));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(UUID_2);
        storage.update(resume);
        Assert.assertEquals(storage.get(UUID_2), resume);
    }

    @After
    public void tearDown() throws Exception {
        storage.clear();
    }
}