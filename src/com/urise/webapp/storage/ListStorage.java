package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.ArrayList;

public class ListStorage extends AbstractStorage <Integer>{

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
    protected void doDelete(Integer foundResult) {
        storage.remove(foundResult);
        storage.trimToSize();
    }

    @Override
    protected Resume doGet(Integer foundResult) {
        return storage.get(foundResult);
    }


    @Override
    protected void doSave(Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void doUpdate(Resume resume) {

    }

    @Override
    protected boolean isExist(Integer foundResult) {
        return (foundResult >= 0);
    }

    @Override
    protected boolean isNotExist(Integer foundResult) {
        return (foundResult < 0);
    }

    @Override
    protected Integer findResumeById(String uuid) {
        for (int i=0; i< storage.size(); i++) {
            if (storage.get(i).equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
