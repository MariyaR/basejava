package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage extends AbstractStorage<String> {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
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
        storage.put(resume.getFullName(), resume);
    }

    @Override
    protected void doUpdate(String key, Resume resume) {
        storage.put(key, resume);
    }

    @Override
    protected List<Resume> getStorage() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected boolean isExist(String key) {
        return (key != null);
    }

    @Override
    protected String findResumeByKey(String key) {
        if (storage.containsKey(key)) {
            return key;
        }
        return null;
    }

    @Override
    public int size() {
        return storage.size();
    }
}
