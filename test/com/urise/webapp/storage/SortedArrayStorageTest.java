package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        Resume[] expectedArray = {new Resume(UUID_2), new Resume(UUID_3)};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }

    @Test
    public void save() throws Exception {
        storage.save(new Resume("uuid5"));
        storage.save(new Resume("uuid4"));
        Resume[] expectedArray = {new Resume(UUID_1), new Resume(UUID_2),
                new Resume(UUID_3), new Resume("uuid4"), new Resume("uuid5")};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }

}