package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

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
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertNull(storage);
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(UUID_2);
        storage.update(resume);
        Assert.assertEquals(storage.get(UUID_2), resume);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expectedStorage = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test
    public void save() throws Exception {

    }

    @Test
    public void delete() throws Exception {
        Resume[] expectedStorage = {new Resume(UUID_1), new Resume(UUID_3)};
        storage.delete(UUID_2);
        Assert.assertArrayEquals(expectedStorage, storage.getAll());
    }

    @Test
    public void get() throws Exception {
        Resume expectedResume = new Resume(UUID_2);
        Assert.assertEquals(storage.get(UUID_2), expectedResume);

    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }


}