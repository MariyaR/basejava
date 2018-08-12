package com.urise.webapp.storage;

import com.urise.webapp.Config;

public class SQLStorageTest extends AbstractStorageTest {

    public SQLStorageTest() {
        super(new SQLStorage(Config.get().getProps().getProperty("db.url"), Config.get().getProps().getProperty("db.user"),Config.get().getProps().getProperty("db.password")));
    }
}
