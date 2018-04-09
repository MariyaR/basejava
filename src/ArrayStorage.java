

import java.util.Arrays;

public class ArrayStorage {

    int length = 10000;
    int size = 0;
    Resume[] storage = new Resume[length];

    void clear() {
        storage = new Resume[length];
        size = 0;
    }

    void save(Resume r) {
        if (size < length) {
            int j = 0;
            while (j <= size && storage[j++] != null) ;
            storage[--j] = r;
            size++;
        } else if (size == length) {
            Resume[] copy = storage;
            length = length + 10;
            storage = new Resume[length];
            storage = Arrays.copyOf(copy, length);
            storage[size++] = r;
        }
    }

    Resume get(String uuid) {
        Resume r = new Resume();
        for (int i = 0; i < size; i++) {
            if (storage[i] != null && storage[i].uuid.equals(uuid)) {
                r = storage[i];
                break;
            }
        }

        return r;
    }

    void delete(String uuid) {
        int end = size;
        for (int i = 0; i < end; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                size--;
            }
        }
        trim();
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    int size() {
        return size;
    }

    void trim() {
        for (int i = 0; i < size + 1; ) {
            if (storage[i] != null) {
                i++;
            } else {
                int j;
                for (j = i; j < size + 1; j++) {
                    if (storage[j] != null) {
                        break;
                    }
                }

                if (j < size + 1) {
                    storage[i] = storage[j];
                    storage[j] = null;
                }
                i++;
            }
        }
    }
}
