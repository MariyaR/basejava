package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test
    public void testSaveOrder() throws Exception {
        storage.save(RESUME_5);
        storage.save(RESUME_4);
        Resume[] expectedArray = {RESUME_1, RESUME_2,
                RESUME_3, RESUME_5, RESUME_4};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }
}