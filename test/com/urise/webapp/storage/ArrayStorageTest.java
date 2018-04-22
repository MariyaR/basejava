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
        storage.save(resume5);
        storage.save(resume4);
        Resume[] expectedArray = {resume1, resume2,
                resume3, resume5, resume4};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
        testSize();
    }

}