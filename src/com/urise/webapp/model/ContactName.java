package com.urise.webapp.model;

public enum ContactName {
    PhoneNumber("Phone number: "),
    Skype("Skype: "),
    Mail("Mail: "),
    LinkedIn("LinkedIn: "),
    GitHub("GitHub: "),
    Stackoverflow("Stackoverflow: "),
    HomePage("Home page: ");

    private String content;

    ContactName(String s) {
        this.content = s;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
