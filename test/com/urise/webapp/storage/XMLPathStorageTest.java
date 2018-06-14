package com.urise.webapp.storage;

public class XMLPathStorageTest extends AbstractStorageTest{

    public XMLPathStorageTest() {
        super(new PathStorage("./src/com/urise/webapp/TestDir", new XMLStreamSerializeStrategy()));
    }
}
