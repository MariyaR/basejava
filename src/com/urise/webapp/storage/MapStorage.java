package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage<String> {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }


    @Override
    protected void doDelete(String key) {
        storage.remove(key);
    }

    @Override
    protected Resume doGet(String key) {
        return storage.get(key);
    }

    @Override
    protected void doSave(Resume resume, String key) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doUpdate(String key, Resume resume) {
        storage.put(key, resume);
    }

    @Override
    protected boolean isExist(String key) {
        return (key != null);
    }

    @Override
    protected String findResumeById(String uuid) {
        if (storage.containsKey(uuid)) {
            return uuid;
        }
        return null;
    }
}
