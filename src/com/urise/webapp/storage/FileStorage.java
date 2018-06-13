package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private SerializeStrategy srlStrategy = new ObjectStreamSerializeStrategy();

    private File directory;

    protected FileStorage(File directory) {
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
                doDelete(file);
            }
        } else throw new StorageException("error in clear", "empty directory");
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("error in doDelete", file.getName());
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return srlStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("error in doGet", file.getName());
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("error in doSave", file.getName());
        }
        doUpdate(file, resume);
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            srlStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
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
                resumeList.add(doGet(file));
            }
        } else throw new StorageException("error in getStorage", "empty directory");
        return resumeList;
    }

    @Override
    protected File findKeyOrIndexBySearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    public int size() {
        String[] fileArray = directory.list();
        if (fileArray != null)
            return fileArray.length;
        else return 0;
    }

}
