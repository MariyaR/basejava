package com.urise.webapp.model;

import java.util.Objects;

//used for Personal information and Current position
public class PlainText extends SectionBasic {

    private final String Field;

    public PlainText(SectionName section, String field) {
        super(section);
        Objects.requireNonNull(field,"field can not be null");
        this.Field = field;
    }

    public String getField() {
        return Field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlainText plainText = (PlainText) o;
        return Objects.equals(Field, plainText.Field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Field);
    }

    @Override
    public String toString() {
        return this.getSection().toString() + ": " +
                Field + "\n";
    }
}
