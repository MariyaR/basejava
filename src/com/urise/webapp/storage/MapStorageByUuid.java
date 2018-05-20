package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorageByUuid extends AbstractStorage<String> {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void doDelete(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected Resume doGet(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doSave(Resume resume, String uuid) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doUpdate(String uuid, Resume resume) {
        storage.put(uuid, resume);
    }

    @Override
    protected List<Resume> getStorage() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected boolean isExist(String uuid) {
        return storage.containsKey(uuid);
    }

    @Override
    protected String findResumeByKey(String uuid) {
        return uuid;
    }

    @Override
    public int size() {
        return storage.size();
    }
}
