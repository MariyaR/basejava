package com.urise.webapp.model;

import com.urise.webapp.exception.ModelException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Contacts {

    private Map<ContactName, String> contacts = new HashMap<>();

    public Contacts addContact(ContactName c, String s) {
        contacts.put(c, s);
        return this;
    }

    public void removeContact(ContactName c) {
        checkKey(c);
        contacts.remove(c);
    }

    public String getContact(ContactName c) {
        checkKey(c);
        return contacts.get(c);
    }

    public void updateContact (ContactName c, String s) {
        checkKey(c);
        contacts.put(c, s);
    }
    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        Stream.of(ContactName.values()).forEach(i -> appendIfExist(st, i));
        return st.toString();
    }

    private void appendIfExist(StringBuffer st, ContactName c) {
        if (contacts.containsKey(c)) {
            st.append(c.toString()).append(contacts.get(c)).append("\n");
        }
    }

    private void checkKey(ContactName c) {
        if (!contacts.containsKey(c)) {
            throw new ModelException("no such contact in this resume");
        }
    }
}
