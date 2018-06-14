package com.urise.webapp.storage;

public class XMLPathStorageTest extends AbstractStorageTest{

    public XMLPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerializer()));
    }
}
