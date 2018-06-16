package com.urise.webapp.storage;

import com.urise.webapp.serialization.DataStreamSerializeStrategy;

public class DataPathStorageTest extends AbstractStorageTest{

    public DataPathStorageTest() {
        super(new PathStorage(DIRECTORY, new DataStreamSerializeStrategy()));
    }
}
