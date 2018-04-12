package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {

    private static final int LENGTH = 10000;
    private int size = 0;
    private Resume[] storage = new Resume[LENGTH];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        if (find(resume.getUuid()) != -1) {
            System.out.println("This resume has been already saved in the storage");
        } else if (size < LENGTH) {
            storage[size] = resume;
            size++;
        } else {
            System.out.println("The storage is full, please delete any entry ");
        }
    }

    public Resume get(String uuid) {
        int index = find(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("There is no resume with id: " + uuid);
        return null;
    }

    public void delete(String uuid) {
        int index = find(uuid);
        if (index >= 0) {
            size--;
            storage[index] = storage[size];
            storage[size] = null;
        } else {
            System.out.println("There is no resume with id: " + uuid);
        }
    }

    public void update(Resume resume) {
        int index = find(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("There is no resume with id: " + resume.getUuid());
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int find(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
