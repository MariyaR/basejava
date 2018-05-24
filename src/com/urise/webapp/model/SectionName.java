package com.urise.webapp.model;

public enum SectionName {
    Personal("Personal information: "),
    CurrentPosition("Current position: "),
    Achievements("Achievements: \n"),
    Skills("Skills: \n"),
    Experience("Working Experience: \n"),
    Education("Education: \n");

    private String content;

    SectionName(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

}
