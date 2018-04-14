package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void delete(String uuid) {
        int index = find(uuid);
        if (index >= 0) {
            compress(index);
            size--;
        } else {
            System.out.println("There is no resume with id: " + uuid);
        }
    }

    public Resume get(String uuid) {
        int index = find(uuid);
        if (index < 0) {
            System.out.println("There is no resume with id: " + uuid);
            return null;
        }
        return storage[index];
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public void save(Resume resume) {
        int index = find(resume.getUuid());
        if (index >= 0) {
            System.out.println("This resume has been already saved in the storage");
        } else if (size < STORAGE_LIMIT) {
            insert(resume, index); //storage[size] = resume;
            size++;
        } else {
            System.out.println("The storage is full, please delete any entry ");
        }
    }

    public int size() {
        return size;
    }

    public void update(Resume resume) {
        int index = find(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("There is no resume with id: " + resume.getUuid());
        }
    }

    protected abstract void compress(int vacancy);

    protected abstract void insert(Resume resume, int index);

    protected abstract int find(String uuid);


}
