package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        Resume[] expectedArray = {resume2, resume3};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }

    @Test
    public void save() throws Exception {
        Resume[] expectedArray = {resume1, resume2, resume3, resume4, resume5};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() throws Exception {
        for (int i = 3; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
            storage.save(new Resume("uuid" + (i + 1)));
        }
        storage.save(resumeDummy);
    }
}