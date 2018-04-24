package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListStorage extends AbstractStorage {

    protected ArrayList<Resume> storage = new ArrayList<>();



    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void delete(String uuid) {
        storage.removeIf(resume->resume.getUuid().equals(uuid));
        storage.trimToSize();
    }

    @Override
    public Resume get(String uuid) {

        int index= storage.indexOf(uuid);
        if (index<0) {throw exception}
        else return storage.get(index);

        return storage.get(storage.indexOf(new Resume(uuid)));
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[storage.size()]);
    }

    @Override
    public void save(Resume r) {
        storage.add(r);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void update(Resume r) {

    }

    @Override
    boolean isNotExist(String uuid) {
        return false;
    }

    @Override
    void doDelete(String uuid) {

    }

    @Override
    void doGet(String uuid) {

    }
}
