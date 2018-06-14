package com.urise.webapp.storage;

import com.urise.webapp.Util.XmlParser;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLStreamSerializeStrategy implements SerializeStrategy{
    private XmlParser xmlParser;

    public XMLStreamSerializeStrategy() {
        xmlParser = new XmlParser(
                Resume.class, Organization.class,
                OrganizationSection.class, TextSection.class, ListSection.class, Organization.Position.class);
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}
