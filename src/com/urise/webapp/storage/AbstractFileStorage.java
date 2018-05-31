package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        File[] fileArray = directory.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                file.delete();
            }
        }
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
        size--;
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return ResumeFromFile(file);
        } catch (IOException e) {
            throw new StorageException("error in doGet", file.getName());
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            writeToFile(resume, file);
        } catch (IOException e) {
            throw new StorageException("error in doSave", file.getName());
        }
        size++;
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            writeToFile(r, file);
        } catch (IOException e) {
            throw new StorageException("error in doUpdate", file.getName());
        }
    }

    @Override
    protected List<Resume> getStorage() {
        List<Resume> resumeList = new ArrayList<>();
        File[] fileArray = directory.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                try {
                    resumeList.add(ResumeFromFile(file));
                } catch (IOException e) {
                    throw new StorageException("error in getStorage", file.getName());
                }
            }
        }
        return resumeList;
    }

    @Override
    protected File findKeyOrIndexBySearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return (file.exists() && file.getParentFile().getAbsolutePath().equals(directory.getAbsolutePath()));
    }

    @Override
    public int size() {
        return size;
    }

    protected abstract Resume ResumeFromFile(File file) throws IOException;

    protected abstract void writeToFile(Resume r, File file) throws IOException;
}
