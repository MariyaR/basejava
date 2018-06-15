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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
