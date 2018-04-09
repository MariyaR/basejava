

import java.util.Arrays;

public class ArrayStorage {

    static final int LENGTH = 3;
    int size = 0;
    Resume[] storage = new Resume[LENGTH];

    void clear() {
        Arrays.fill(storage, null);
        size = 0;
    }

    void save(Resume r) {

        if (size < LENGTH) {
            storage[size] = r;
            size++;
        } else if (size == LENGTH) {
            System.out.println("The storage is full, please delete any entry ");
        }
    }

    Resume get(String uuid) {
        Resume r = new Resume();
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                r = storage[i];
                break;
            }
        }

        return r;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
            }
        }
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

}
