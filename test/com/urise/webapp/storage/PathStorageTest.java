package com.urise.webapp.storage;

import com.urise.webapp.Serialization.ObjectStreamSerializeStrategy;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(DIRECTORY, new ObjectStreamSerializeStrategy()));
    }


}
