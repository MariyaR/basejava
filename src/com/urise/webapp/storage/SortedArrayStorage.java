package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

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
    protected Integer findResumeByKey(String key) {
        Resume searchResume = new Resume(key);
        return Arrays.binarySearch(storage, 0, size, searchResume, Comparator.comparing(Resume::getFullName));
    }
}
