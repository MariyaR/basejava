package com.urise.webapp.model;

public enum Section {
    Personal("Personal information"),
    CurrentPosition("Current position"),
    Achievements("Achievements"),
    Skills("Skills"),
    Experience("Working Experience"),
    Education("Education");

    private String content;

    Section(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

}
