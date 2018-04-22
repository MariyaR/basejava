package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class AbstractStorage implements Storage {

    protected int size = 0;

    @Override
    public void clear() {

    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public Resume get(String uuid) {
        return null;
    }

    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }

    @Override
    public void save(Resume r) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void update(Resume r) {

    }
}
