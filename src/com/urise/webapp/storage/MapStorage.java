package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapStorage extends AbstractStorage <Resume> {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
    storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return  storage.values().toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }


    @Override
    protected void doDelete(Resume foundResult) {

    }

    @Override
    protected Resume doGet(Resume foundResult) {
        return null;
    }

    @Override
    protected void doSave(Resume resume) {

    }

    @Override
    protected void doUpdate(Resume resume) {

    }

    @Override
    protected boolean isExist(Resume foundResult) {
        return (foundResult!=null);
    }

    @Override
    protected boolean isNotExist(Resume foundResult) {
        return (foundResult==null);
    }

    @Override
    protected Resume findResumeById(String uuid) {
        return storage.get(uuid);
    }
}
