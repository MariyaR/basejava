

import com.sun.org.apache.regexp.internal.RE;

import java.util.Arrays;
import java.util.Objects;

public class ArrayStorage {

    private static final int LENGTH = 10000;
    private int size = 0;
    private Resume[] storage = new Resume[LENGTH];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    private int find(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }


    public void save(Resume r) {
        if (find(r.uuid) != -1) {
            System.out.println("This resume has been already saved in storage");
        } else {
            if (size < LENGTH) {
                storage[size] = r;
                size++;
            } else if (size == LENGTH) {
                System.out.println("The storage is full, please delete any entry ");
            }
        }
    }

    public Resume get(String uuid) {
        int i=find(uuid);
        if (i>=0) {
            return storage[i];
        } else {
            System.out.println("There is no resume with id: "+uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        int i=find(uuid);
        if (i>=0){
            size--;
            storage[i] = storage[size];
            storage[size] = null;
        }else {
            System.out.println("There is no resume with id: "+uuid);
        }
    }

    public void update (Resume r) {
        int i=find(r.uuid);
        if (i>=0) {
            storage[i]=r;
        } else {
            System.out.println("There is no resume with id: "+r.uuid);
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

}
