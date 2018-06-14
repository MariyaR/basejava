package com.urise.webapp.storage;

public class JsonPathStorageTest extends AbstractStorageTest{

    public JsonPathStorageTest() {
        super(new PathStorage("./src/com/urise/webapp/TestDir", new JsonSerializeStrategy()));
    }
}
