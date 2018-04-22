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
    protected static final String DUMMY = "uuid100500";
    protected static final Resume RESUME_1 = new Resume(UUID_1);
    protected static final Resume RESUME_2 = new Resume(UUID_2);
    protected static final Resume RESUME_3 = new Resume(UUID_3);
    protected static final Resume RESUME_4 = new Resume(UUID_4);
    protected static final Resume RESUME_5 = new Resume(UUID_5);
    protected static final Resume RESUME_DUMMY = new Resume(DUMMY);

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        testSize(0);
        testSize();
    }

    @Test
    public void delete() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_3};
        storage.delete(UUID_2);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
        testSize();
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() throws Exception {
        storage.delete(DUMMY);
        testSize();
        testSize(3);
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
        testSize(3);
        testSize();
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_2, RESUME_3};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
        testSize();
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistException() throws Exception {
        testSize();
        testSize(3);
        storage.get(DUMMY);
    }

    @Test
    public void save() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_2, RESUME_3, RESUME_4};
        storage.save(RESUME_4);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
        testSize();
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistException() throws Exception {
        testSize(3);
        testSize();
        storage.save(RESUME_1);
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
        storage.save(RESUME_DUMMY);
    }

    @Test
    public void size() throws Exception {
        testSize(3);
        testSize();
    }

    @Test
    public void update() throws Exception {
        storage.update(RESUME_2);
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
        testSize(3);
        testSize();
    }

    @Test(expected = StorageException.class)
    public void updateNotExistException() throws Exception {
        testSize();
        testSize(3);
        storage.update(RESUME_DUMMY);
    }

    protected void testSize() {
        Assert.assertEquals(storage.size(), storage.getAll().length);
    }

    protected void testSize(int expectedSize) {
        Assert.assertEquals(expectedSize, storage.size());
    }
}