import com.sun.org.apache.regexp.internal.RE;

import java.util.Arrays;

public class ArrayStorageSingleton {

    int length=6;
    int size=0;
    int firstFreeIndex=0;
    Resume[] storage = new Resume[length];

    void clear() {
        for (int i=0; i<firstFreeIndex; i++) {
            if (storage[i]!=null) {
                storage[i]=null;
            }
        }
        size=0;
        firstFreeIndex=0;
    }

    void save(Resume r) throws IndexOutOfBoundsException{
        if (size < length) {
            int j = 0;
            while (j <= firstFreeIndex && storage[j++] != null) ;
            storage[--j] = r;
            size++;
            if (j==firstFreeIndex);
            {firstFreeIndex++;}
        }
        else if (size==length) {
            Resume[] copy=storage;
            length=length+10;
            storage=new Resume[length];
            storage=Arrays.copyOf(copy,length);
            storage[firstFreeIndex]=r;
            firstFreeIndex++;
            size++;
        }
    }

    Resume get(String uuid) {
        Resume r = new Resume();
        for (int i=0; i<firstFreeIndex;i++) {
            if (storage[i]!=null && storage[i].uuid.equals(uuid)) {
                r=storage[i];
                break;
            }
        }

        return r;
    }

    void delete(String uuid) {
        for (int i=0; i<firstFreeIndex; i++) {
            if (storage[i]!=null && storage[i].uuid.equals(uuid)) {
                storage[i]=null;
                size--;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resume = new Resume[firstFreeIndex+1];
        int j=0;
        for (int i=0; i<firstFreeIndex; i++) {
            if (storage[i]!=null) {
                resume[j++]=storage[i];
            }
        }

        int i=0;
        while(i<resume.length && resume[i++]!=null);

        return Arrays.copyOfRange(resume,0,--i);
    }

    int size() {return size;}

    void trim() {

        for (int i=0; i<firstFreeIndex;) {
            if (storage[i]!=null) {
                i++;
            }
            else {
                int j;
                for (j=i; j<firstFreeIndex; j++) {
                    if (storage[j]!=null) {
                        break;
                    }
                }

                if (j<firstFreeIndex) {
                    storage[i] = storage[j];
                    storage[j] = null;
                }
                i++;
            }
        }
        firstFreeIndex=size;
    }
}
