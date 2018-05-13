package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void compress(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    protected void insert(Resume resume, int index) {
        storage[size] = resume;
    }

    @Override
    protected Integer findResumeByKey(String key) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
