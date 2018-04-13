package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void compress(int index) {
        for (int i = index; i < size; i++) {
            storage[i] = storage[i + 1];
        }
    }

    @Override
    protected void insert(Resume resume, int index) {
        int vacancy = -index - 1;
        for (int i = size; i > vacancy; i--) {
            storage[i] = storage[i - 1];
        }
        storage[vacancy] = resume;
    }

    @Override
    protected int find(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
