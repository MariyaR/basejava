package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void compress(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }

    @Override
    protected void insert(Resume resume, int index) {
        int vacancy = -index - 1;
        System.arraycopy(storage, vacancy, storage, vacancy + 1, size - vacancy);
        storage[vacancy] = resume;
    }

    @Override
    protected int find(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
