package com.urise.webapp.model;

import com.urise.webapp.exception.ModelException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Contacts {

    private Map<ContactName, String> contacts = new HashMap<>();

    public Contacts addContact(ContactName c, String s) {
        checkIfExist(c);
        contacts.put(c, s);
        return this;
    }

    public String getContact(ContactName c) {
        checkKey(c);
        return contacts.get(c);
    }

    public void updateContact (ContactName c, String s) {
        checkKey(c);
        contacts.put(c, s);
    }

    public void removeContact(ContactName c) {
        checkKey(c);
        contacts.remove(c);
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        Stream.of(ContactName.values()).forEach(i -> appendIfExist(st, i));
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacts contacts1 = (Contacts) o;
        return Objects.equals(contacts, contacts1.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts);
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

    private void checkIfExist(ContactName c) {
        if (contacts.containsKey(c)) {
            throw  new ModelException("this contact is a;ready exist");
        }
    }
}
