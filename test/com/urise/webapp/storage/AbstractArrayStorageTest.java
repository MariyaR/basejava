package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractArrayStorageTest {

    protected Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String UUID_5 = "uuid5";
    protected static final String dummy = "uuid100500";
    protected static final Resume resume1 = new Resume(UUID_1);
    protected static final Resume resume2 = new Resume(UUID_2);
    protected static final Resume resume3 = new Resume(UUID_3);
    protected static final Resume resume4 = new Resume(UUID_4);
    protected static final Resume resume5 = new Resume(UUID_5);
    protected static final Resume resumeDummy = new Resume(dummy);

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }


    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void delete() throws Exception {
        Resume[] expectedStorage = {resume1, resume3};
        storage.delete(UUID_2);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() throws Exception {
        storage.delete(dummy);
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(resume2, storage.get(UUID_2));
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expectedStorage = {resume1, resume2, resume3};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistException() throws Exception {
        storage.get(dummy);
    }

    @Test
    public void save() throws Exception {
        Resume[] expectedStorage = {resume1, resume2, resume3, resume4};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistException() throws Exception {
        storage.save(resume1);
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void update() throws Exception {
        storage.update(resume2);
        Assert.assertEquals(resume2, storage.get(UUID_2));
    }

    @Test(expected = StorageException.class)
    public void updateNotExistException() throws Exception {
        storage.update(resumeDummy);
    }
}