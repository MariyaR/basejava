package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage<Integer> {

    protected ArrayList<Resume> storage = new ArrayList<>();


    @Override
    public void clear() {
        storage.clear();
    }


    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }


    @Override
    protected void doDelete(Integer index) {
        storage.remove(index);
        storage.trimToSize();
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage.get(index);
    }


    @Override
    protected void doSave(Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void doUpdate(Integer index, Resume resume) {
        storage.remove(index);
        storage.add(resume);
    }

    @Override
    protected boolean isExist(Integer index) {
        return (index >= 0);
    }

    @Override
    protected boolean isNotExist(Integer index) {
        return (index < 0);
    }

    @Override
    protected Integer findResumeById(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
