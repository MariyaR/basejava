package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractStorageTest {

    protected Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String UUID_5 = "uuid5";
    protected static final String DUMMY = "uuid100500";
    protected static final Resume RESUME_1 = new Resume(UUID_1, "Name");
    protected static final Resume RESUME_2 = new Resume(UUID_2, "Name");
    protected static final Resume RESUME_3 = new Resume(UUID_3, "Name");
    protected static final Resume RESUME_4 = new Resume(UUID_4, "Name");
    protected static final Resume RESUME_5 = new Resume(UUID_5, "Name");
    protected static final Resume RESUME_DUMMY = new Resume(DUMMY);

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
        Resume[] expectedStorageArray = {RESUME_1, RESUME_3};
        storage.delete(UUID_2);
        Assert.assertArrayEquals(expectedStorageArray, getStorageAsArray());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() throws Exception {
        storage.delete(DUMMY);
    }

    @Test
    public void get() throws Exception {

        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
    }

    @Test
    public void getAllSorted() throws Exception {
        Resume[] expectedStorage = {RESUME_1, RESUME_2, RESUME_3};
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
        storage.update(RESUME_2);
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
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