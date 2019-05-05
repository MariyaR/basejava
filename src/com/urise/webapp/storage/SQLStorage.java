package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SQLHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLStorage implements Storage {
    private final SQLHelper helper;

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SQLHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        helper.execute(PreparedStatement::executeUpdate, "DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return helper.execute(ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                addContact(rs, resume);
                addSection(rs, resume);
            } while (rs.next());
            return resume;
        }, "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "LEFT JOIN section s " +
                "ON r.uuid = s.resume_uuid " +
                "WHERE r.uuid =? ");
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute(conn -> {
                    if (insertOrUpdateResume(r, conn, "UPDATE resume SET full_name = ? WHERE uuid =?") == 0)
                        throw new NotExistStorageException(r.getUuid());
                    deleteContacts(r, conn);
                    insertContacts(r, conn);
                    deletesection(r, conn);
                    insertSection(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(conn -> {
                    insertOrUpdateResume(r, conn, "INSERT INTO resume (full_name, uuid) VALUES (?,?)");
                    insertContacts(r, conn);
                    insertSection(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        helper.execute(ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0)
                throw new NotExistStorageException("no such resume in data base");
            return null;
        }, "DELETE FROM resume where uuid=?");
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    addContact(rs, resumes.get(uuid));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    addSection(rs, resumes.get(uuid));
                }
            }

            List<Resume> list = new ArrayList<>(resumes.values());
            Collections.sort(list);
            return list;
        });
    }


    @Override
    public int size() {
        return helper.execute(ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt("total");
        }, "SELECT COUNT(*) AS total FROM resume");
    }

    private void deleteContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE  FROM contact WHERE resume_uuid=?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void deletesection(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE  FROM section WHERE resume_uuid=?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        String type = rs.getString("type");
        if (value != null && type != null) {
            ContactName name = ContactName.valueOf(type);
            r.addContact(name, value);
        }
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("section_value");
        if (value != null) {
            SectionName sectionName = SectionName.valueOf(rs.getString("section_type"));
            resume.addSection(sectionName, JsonParser.read(value, SectionBasic.class));
        }
    }


    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, resume_uuid, type) VALUES (?,?,?)")) {
            for (Map.Entry<ContactName, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(2, r.getUuid());
                ps.setString(3, e.getKey().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (section_value, resume_uuid, section_type) VALUES (?,?,?)")) {
            for (Map.Entry<SectionName, SectionBasic> e : r.getSections().entrySet()) {

                ps.setString(1, JsonParser.write(e.getValue(), SectionBasic.class));
                ps.setString(2, r.getUuid());
                ps.setString(3, e.getKey().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }

    }

    private int insertOrUpdateResume(Resume r, Connection conn, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            return ps.executeUpdate();
        }
    }

}

