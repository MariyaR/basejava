package com.urise.webapp.storage;

import com.urise.webapp.serialization.DataStreamSerializeStrategy;
import com.urise.webapp.serialization.DataStreamSerializeStrategy_2;

public class DataPathStorageTest extends AbstractStorageTest{

    public DataPathStorageTest() {
        super(new PathStorage(DIRECTORY, new DataStreamSerializeStrategy_2()));
    }
}
