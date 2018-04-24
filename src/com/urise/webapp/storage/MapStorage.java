package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapStorage extends AbstractStorage {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
    storage.clear();
    }

    @Override
    public void delete(String uuid) {
        storage.remove(uuid);
    }

    @Override
    public Resume get(String uuid) {

        Resume r= storage.get(uuid);

        if (r!=null) {
            return r;
        }
        else throw exception;
    }

    @Override
    public Resume[] getAll() {
        return  storage.values().toArray(new Resume[storage.size()]);
    }

    @Override
    public void save(Resume r) {
        storage.put(UUID.randomUUID().toString(),r);
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
