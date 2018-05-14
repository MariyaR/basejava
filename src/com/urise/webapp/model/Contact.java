package com.urise.webapp.model;

public enum Contact {
    PhoneNumber("Phone number: "),
    Skype("Skype: "),
    Mail("Mail: "),
    LinkedIn("LinkedIn: "),
    GitHub("GitHub: "),
    Stackoverflow("Stackoverflow: "),
    HomePage("Home page: ");

    private String content;

    Contact(String s) {
        this.content = s;
    }

    @Override
    public String toString() {
        return content;
    }
}
