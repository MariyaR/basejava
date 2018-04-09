

import java.util.Arrays;

public class ArrayStorage {

    static final int LENGTH = 10000;
    int size = 0;
    Resume[] storage = new Resume[LENGTH];

    void clear() {
        Arrays.fill(storage,0,size,null);
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
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }

        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                size--;
                storage[i] = storage[size];
                storage[size] = null;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }

}
