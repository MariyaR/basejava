package com.urise.webapp.storage;

import com.urise.webapp.serialization.JsonSerializeStrategy;

public class JsonPathStorageTest extends AbstractStorageTest{

    public JsonPathStorageTest() {
        super(new PathStorage("./src/com/urise/webapp/TestDir", new JsonSerializeStrategy()));
    }
}
