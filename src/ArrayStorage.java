import com.sun.org.apache.regexp.internal.RE;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {

    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i=0; i<storage.length; i++) {
            if (storage[i]!=null) {
                storage[i]=null;
            }
        }
    }

    void save(Resume r) throws IndexOutOfBoundsException{
        int j=0;
        while(j<storage.length && storage[j++]!=null);
        if (j==storage.length && storage[j-1]!=null) {
            throw new IndexOutOfBoundsException();
        }
        else if(j<=storage.length){
          storage[--j]=r;
        }
    }

    Resume get(String uuid) {
        Resume r = new Resume();
        for (int i=0; i<storage.length;i++) {
            if (storage[i]!=null && storage[i].uuid.equals(uuid)) {
                r=storage[i];
                break;
            }
        }

        return r;
    }

    void delete(String uuid) {
        for (int i=0; i<storage.length; i++) {
            if (storage[i]!=null && storage[i].uuid.equals(uuid)) {
                storage[i]=null;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resume = new Resume[10000];
        int j=0;
        for (int i=0; i<storage.length; i++) {
            if (storage[i]!=null) {
                resume[j++]=storage[i];
            }
        }

        int i=0;
        while(i<resume.length && resume[i++]!=null);

        return Arrays.copyOfRange(resume,0,--i);
    }

    int size() {

        return this.getAll().length;
    }

    void trim() {
        for (int i=0; i<storage.length;) {
            if (storage[i]!=null) {
                i++;
            }
            else {
                int j;
                for (j=i; j<storage.length; j++) {
                    if (storage[j]!=null) {
                        break;
                    }
                }

                if (j<storage.length) {
                    storage[i] = storage[j];
                    storage[j] = null;
                }
                i++;
            }
        }
    }
}
