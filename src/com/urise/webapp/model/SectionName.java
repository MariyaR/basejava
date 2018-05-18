package com.urise.webapp.model;

public enum SectionName {
    Personal("Personal information"),
    CurrentPosition("Current position"),
    Achievements("Achievements"),
    Skills("Skills"),
    Experience("Working Experience"),
    Education("Education");

    private String content;

    SectionName(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

}
