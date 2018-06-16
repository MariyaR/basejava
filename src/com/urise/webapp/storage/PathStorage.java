package com.urise.webapp.storage;

import com.urise.webapp.serialization.SerializeStrategy;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathStorage extends AbstractStorage<Path> {

    private Path directory;

    private SerializeStrategy srlStrategy;

    protected PathStorage(String dir, SerializeStrategy srlStrategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        this.srlStrategy = srlStrategy;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("can not count files", null);
        }
    }

    @Override
    protected Path findKeyOrIndexBySearchKey(String uuid) {
        return directory.resolve(uuid);

    }

    @Override
    protected void doUpdate(Path path, Resume r) {
        try {
            srlStrategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid());
        }
    }

    @Override
    protected boolean isExist(Path Path) {
        return Files.isRegularFile(Path);
    }

    @Override
    protected void doSave(Resume r, Path Path) {
        try {
            Files.createFile(Path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + Path.getName(-1), Path.getName(-1).toString());
        }
        doUpdate(Path, r);
    }

    @Override
    protected Resume doGet(Path Path) {
        try {
            return srlStrategy.doRead(new BufferedInputStream(Files.newInputStream(Path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", Path.getName(-1).toString());
        }
    }

    @Override
    protected void doDelete(Path Path) {
        try {
            Files.delete(Path);
        } catch (IOException e) {
            throw new StorageException("can not delete file" + Path, Path.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> getStorage() {

        try {
            return Files.list(directory).map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("can't return storage", null);
        }
    }
}