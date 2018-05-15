package com.urise.webapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Contacts {

    Map<Contact, String> contacts = new HashMap<>();

    public void addContact(Contact c, String s) {
        contacts.put(c, s);
    }

    public void removeContact(Contact c) {
        contacts.remove(c);
    }

    public String getContact(Contact c) {
        return contacts.get(c);
    }

    @Override
    public String toString() {
        StringBuffer st = new StringBuffer();
        Stream.of(Contact.values()).forEach(i -> appendIfExist(st, i));
//        st.append("Mobile phone: ").append(contacts.get(Contact.PhoneNumber)).append("\n");
//        st.append("Skype: ").append(contacts.get(Contact.Skype)).append("\n");
//        st.append("Mail: ").append(contacts.get(Contact.Mail)).append("\n");
//        st.append("LinkedIn: ").append(contacts.get(Contact.LinkedIn)).append("\n");
//        st.append("GitHub: ").append(contacts.get(Contact.GitHub)).append("\n");
//        st.append("StackOverflow: ").append(contacts.get(Contact.Stackoverflow)).append("\n");
//        st.append("Home page: ").append(contacts.get(Contact.HomePage)).append("\n");
        return st.toString();
    }

    private void appendIfExist(StringBuffer st, Contact c) {
        if (contacts.containsKey(c)) {
            st.append(c.toString()).append(contacts.get(c)).append("\n");
        }
    }
}
