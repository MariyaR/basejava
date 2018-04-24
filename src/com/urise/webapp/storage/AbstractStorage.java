package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage <T> implements Storage {

    @Override
    public void delete(String uuid) {
        T foundResult = findResumeById(uuid);
        if (isNotExist(foundResult)) {
            throw new NotExistStorageException(uuid);
        }
        doDelete(foundResult);
    }

    @Override
    public Resume get(String uuid) {
        T foundResult = findResumeById(uuid);
        if (isNotExist(foundResult)) {
            throw new NotExistStorageException(uuid);
        }
        return doGet(foundResult);
    }

    @Override
    public void save(Resume resume) {
        T foundResult = findResumeById(resume.getUuid());
        if (isExist(foundResult)) {
            throw new ExistStorageException(resume.getUuid());
        }
        doSave(resume);
    }

    @Override
    public void update(Resume resume) {
        T foundResult = findResumeById(resume.getUuid());
        if (isNotExist(foundResult)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        doUpdate(resume);
    }

    protected abstract void doDelete(T foundResult);

    protected abstract Resume doGet(T foundResult);

    protected abstract void doSave(Resume resume);

    protected abstract void doUpdate(Resume resume);

    protected abstract boolean isExist(T foundResult);

    protected abstract boolean isNotExist(T foundResult);

    protected abstract T findResumeById(String uuid);

}



