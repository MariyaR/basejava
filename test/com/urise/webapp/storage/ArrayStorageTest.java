package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayStorageTest extends AbstractArrayStorageTest {


    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test
    public void save() throws Exception {
        storage.save(new Resume("uuid5"));
        storage.save(new Resume("uuid4"));
        Resume[] expectedArray = {new Resume(UUID_1), new Resume(UUID_2),
                new Resume(UUID_3), new Resume("uuid5"), new Resume("uuid4")};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }

}