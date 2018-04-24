package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage <T> implements Storage {

    @Override
    public void delete(String uuid) {
        if (isNotExist(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        doDelete(uuid);
    }

    @Override
    public Resume get(String uuid) {
        T findResult = findResume(uuid);
        if (isNotExist(findResult)) {
            throw new NotExistStorageException(uuid);
        }
        return doGet(findResult);
    }

    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }

    @Override
    public void save(Resume r) {

    }

    @Override
    public void update(Resume r) {

    }

    abstract boolean isNotExist(T result);
    abstract void doDelete(String uuid);
    abstract Resume doGet(T findResult);
    abstract T findResume(String uuid);

}



