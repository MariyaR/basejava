package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        storage.delete(UUID_1);
        Resume[] expectedArray = {resume2, resume3};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
        testSize();
    }

    @Test
    public void testSaveOrder() throws Exception {
        Resume[] expectedArray = {resume1, resume2, resume3, resume4, resume5};
        storage.save(resume5);
        storage.save(resume4);
        Assert.assertArrayEquals(expectedArray, storage.getAll());
        testSize();
    }
}