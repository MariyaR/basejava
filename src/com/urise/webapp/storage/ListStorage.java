package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListStorage extends AbstractStorage {

    protected List<Resume> storage = new ArrayList<>();


    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void delete(String uuid) {
        Iterator it = storage.listIterator();
        while (it.hasNext()) {
            
        }
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
