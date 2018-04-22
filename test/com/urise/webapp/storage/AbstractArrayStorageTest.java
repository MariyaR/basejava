package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {

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
        testSize(0);
        testSize();
    }

    @Test
    public void delete() throws Exception {
        Resume[] expectedStorage = {resume1, resume3};
        storage.delete(UUID_2);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
        testSize();
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() throws Exception {
        storage.delete(dummy);
        testSize();
        testSize(3);
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(resume2, storage.get(UUID_2));
        testSize(3);
        testSize();
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expectedStorage = {resume1, resume2, resume3};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
        testSize();
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistException() throws Exception {
        testSize();
        testSize(3);
        storage.get(dummy);
    }

    @Test
    public void save() throws Exception {
        Resume[] expectedStorage = {resume1, resume2, resume3, resume4};
        storage.save(resume4);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
        testSize();
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistException() throws Exception {
        testSize(3);
        testSize();
        storage.save(resume1);
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() throws Exception {
        try {
            for (int i = 3; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("uuid" + (i + 1)));
            }
        } catch (StorageException e) {
            Assert.assertFalse(true);
        }
        testSize(AbstractArrayStorage.STORAGE_LIMIT);
        testSize();
        storage.save(resumeDummy);
    }

    @Test
    public void size() throws Exception {
        testSize(3);
        testSize();
    }

    @Test
    public void update() throws Exception {
        storage.update(resume2);
        Assert.assertEquals(resume2, storage.get(UUID_2));
        testSize(3);
        testSize();
    }

    @Test(expected = StorageException.class)
    public void updateNotExistException() throws Exception {
        testSize();
        testSize(3);
        storage.update(resumeDummy);
    }

    protected void testSize() {
        Assert.assertEquals(storage.size(), storage.getAll().length);
    }

    protected void testSize(int expectedSize) {
        Assert.assertEquals(expectedSize, storage.size());
    }
}