package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<KeyOrIndex> implements Storage {

    @Override
    public void delete(String uuid) {
        KeyOrIndex keyOrIndex = findResumeIfExist(uuid);
        doDelete(keyOrIndex);
    }

    @Override
    public Resume get(String uuid) {
        KeyOrIndex keyOrIndex = findResumeIfExist(uuid);
        return doGet(keyOrIndex);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> sortedList = new ArrayList<>();
        copyStorage(sortedList);
        sortedList.sort(Comparator.comparing(Resume::getUuid));
        return sortedList;
    }

    @Override
    public void save(Resume resume) {
        KeyOrIndex keyOrIndex = findPlaceToSave(resume.getUuid());
        doSave(resume, keyOrIndex);
    }

    @Override
    public void update(Resume resume) {
        KeyOrIndex keyOrIndex = findResumeIfExist(resume.getUuid());
        doUpdate(keyOrIndex, resume);
    }

    private KeyOrIndex findResumeIfExist(String uuid) {
        KeyOrIndex keyOrIndex = findResumeById(uuid);
        if (!isExist(keyOrIndex)) {
            throw new NotExistStorageException(uuid);
        }
        return keyOrIndex;
    }

    private KeyOrIndex findPlaceToSave(String uuid) {
        KeyOrIndex keyOrIndex = findResumeById(uuid);
        if (isExist(keyOrIndex)) {
            throw new ExistStorageException(uuid);
        }
        return keyOrIndex;
    }

    protected abstract void copyStorage(List<Resume> sortedList);

    protected abstract void doDelete(KeyOrIndex keyOrIndex);

    protected abstract Resume doGet(KeyOrIndex keyOrIndex);

    protected abstract void doSave(Resume resume, KeyOrIndex keyOrIndex);

    protected abstract void doUpdate(KeyOrIndex keyOrIndex, Resume resume);

    protected abstract boolean isExist(KeyOrIndex keyOrIndex);

    protected abstract KeyOrIndex findResumeById(String uuid);

}



