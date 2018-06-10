package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doDelete(Integer index) {
        compress(index);
        size--;
        storage[size] = null;
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected void doSave(Resume resume, Integer index) {
        if (size < STORAGE_LIMIT) {
            insert(resume, index);
            size++;
        } else {
            throw new StorageException("The storage is full", resume.getUuid());
        }
    }

    @Override
    protected void doUpdate(Integer index, Resume resume) {
        storage[index] = resume;
    }

    @Override
    public List<Resume> getStorage() {
        return Arrays.asList(Arrays.copyOfRange(storage,0,size));
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected abstract Integer findKeyOrIndexBySearchKey(String key);

    protected abstract void compress(int vacancy);

    protected abstract void insert(Resume resume, int index);

}
