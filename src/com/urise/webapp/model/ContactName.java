package com.urise.webapp.model;

public enum ContactName {
    PhoneNumber("Phone number: "),
    Skype("Skype: ") {
        @Override
        public String toHtml0(String value) {
            return getContent() + toLink("skype:" + value, value);
        }
    },
    Mail("Mail: ") {
        @Override
        public String toHtml0(String value) {
            return getContent() + toLink("mailto:" + value, value);
        }
    },
    LinkedIn("LinkedIn: ") {
        @Override
        public String toHtml0(String value) {
            return getContent() + toLink(value, value);
        }
    },
    GitHub("GitHub: ") {
        @Override
        public String toHtml0(String value) {
            return getContent() + toLink(value, value);
        }
    },
    Stackoverflow("Stackoverflow: ") {
        @Override
        public String toHtml0(String value) {
            return getContent() + toLink(value, value);
        }

    },
    HomePage("Home page: ") {
        @Override
        public String toHtml0(String value) {
            return getContent() + toLink(value, value);
        }
    };

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

    protected String toHtml0(String value) {
        return content + value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    public String toLink(String href) {
        return toLink(href, content);
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }

}
