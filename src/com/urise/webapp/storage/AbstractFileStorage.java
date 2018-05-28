package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;
    protected int size = 0;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        List<File> fileList = Arrays.asList(directory.listFiles());
        for (File file : fileList) {
            file.delete();
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected File findKeyOrIndexBySearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isExist(File file) {
        return (file.exists() && file.getAbsolutePath().contains(directory.getAbsolutePath()));
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName());
        }
        size++;
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
        size--;
    }

    @Override
    protected List<Resume> getStorage() {
        return null;
    }

    @Override
    protected Resume doGet(File file) {
        return createResumeFromFile(file);
    }

    protected abstract Resume createResumeFromFile(File file);

    protected abstract void doWrite(Resume r, File file) throws IOException;
}
