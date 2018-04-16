package com.urise.webapp.model;

import java.util.Objects;
import java.util.UUID;

/**
 * com.urise.webapp.model.Resume class
 */
public class Resume implements Comparable<Resume> {

    private String uuid;

    public Resume() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        int uuidNumber = Integer.parseInt(uuid.substring(4));
        int oNumber = Integer.parseInt(o.uuid.substring(4));

        return uuidNumber - oNumber;
        //return uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return uuid;
    }
}
