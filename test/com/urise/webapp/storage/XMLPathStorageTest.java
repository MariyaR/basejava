package com.urise.webapp.storage;

import com.urise.webapp.serialization.XMLStreamSerializeStrategy;

public class XMLPathStorageTest extends AbstractStorageTest{

    public XMLPathStorageTest() {
        super(new PathStorage("./src/com/urise/webapp/TestDir", new XMLStreamSerializeStrategy()));
    }
}
