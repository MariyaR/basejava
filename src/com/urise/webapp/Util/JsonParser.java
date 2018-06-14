package com.urise.webapp.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionBasic;
import sun.swing.SwingUtilities2;

import java.io.Reader;
import java.io.Writer;
import java.util.EnumMap;

public class JsonParser {
    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SectionBasic.class, new JsonSectionAdapter())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }
}
