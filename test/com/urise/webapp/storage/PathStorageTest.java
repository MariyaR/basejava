package com.urise.webapp.storage;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(DIRECTORY, new ObjectStreamSerializeStrategy()));
    }


}
