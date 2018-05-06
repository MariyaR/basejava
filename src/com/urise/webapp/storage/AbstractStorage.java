package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage<StringOrInteger> implements Storage {

    @Override
    public void delete(String uuid) {
        StringOrInteger keyOrIndex = findResumeById(uuid);
        if (!isExist(keyOrIndex)) {
            throw new NotExistStorageException(uuid);
        }
        doDelete(keyOrIndex);
    }

    @Override
    public Resume get(String uuid) {
        StringOrInteger keyOrIndex = findResumeById(uuid);
        if (!isExist(keyOrIndex)) {
            throw new NotExistStorageException(uuid);
        }
        return doGet(keyOrIndex);
    }

    @Override
    public void save(Resume resume) {
        StringOrInteger keyOrIndex = findResumeById(resume.getUuid());
        if (isExist(keyOrIndex)) {
            throw new ExistStorageException(resume.getUuid());
        }
        doSave(resume, keyOrIndex);
    }

    @Override
    public void update(Resume resume) {
        StringOrInteger keyOrIndex = findResumeById(resume.getUuid());
        if (!isExist(keyOrIndex)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        doUpdate(keyOrIndex, resume);
    }

    protected abstract void doDelete(StringOrInteger keyOrIndex);

    protected abstract Resume doGet(StringOrInteger keyOrIndex);

    protected abstract void doSave(Resume resume, StringOrInteger keyOrIndex);

    protected abstract void doUpdate(StringOrInteger keyOrIndex, Resume resume);

    protected abstract boolean isExist(StringOrInteger keyOrIndex);
    
    protected abstract StringOrInteger findResumeById(String uuid);

}



