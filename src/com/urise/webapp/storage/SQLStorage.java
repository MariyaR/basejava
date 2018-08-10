package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SQLHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
                addPlainText(rs, resume);
            } while (rs.next());
            return resume;
        }, "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "LEFT JOIN plain_text p " +
                "ON r.uuid = p.plain_resume_uuid " +
                "WHERE r.uuid =? ");
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute(conn -> {
                    if (insertOrUpdateResume(r, conn, "UPDATE resume SET full_name = ? WHERE uuid =?") == 0)
                        throw new NotExistStorageException(r.getUuid());
                    deleteContacts(r, conn);
                    insertContacts(r, conn);
                    deletePlainText(r, conn);
                    insertPlaintText(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(conn -> {
                    insertOrUpdateResume(r, conn, "INSERT INTO resume (full_name, uuid) VALUES (?,?)");
                    insertContacts(r, conn);
                    insertPlaintText(r, conn);
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
            Map<String, Resume> map = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    map.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    addContact(rs, map.get(uuid));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM plain_text")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("plain_resume_uuid");
                    addPlainText(rs, map.get(uuid));
                }
            }

            List<Resume> list = new ArrayList<>(map.values());
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

    private void deletePlainText(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE  FROM plain_text WHERE plain_resume_uuid=?")) {
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

    private void addPlainText(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("plain_value");
        String type = rs.getString("plain_type");
        if (value != null && type != null) {
            SectionName name = SectionName.valueOf(type);
            switch (name) {
                case Personal:
                case CurrentPosition: {
                    PlainText section = new PlainText(value);
                    r.addSection(name, section);
                }
                break;
                case Skills:
                case Achievements: {
                    ListOfStrings section = new ListOfStrings();
                    Arrays.stream(value.split("\n")).forEach(s -> section.addRecord(s));
                    r.addSection(name, section);
                }
                break;

            }
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

    private void insertPlaintText(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO plain_text (plain_value, plain_resume_uuid, plain_type) VALUES (?,?,?)")) {
            for (Map.Entry<SectionName, SectionBasic> e : r.getSections().entrySet()) {
                if (e.getValue() instanceof PlainText) {
                    ps.setString(1, ((PlainText) e.getValue()).getField());
                    ps.setString(2, r.getUuid());
                    ps.setString(3, e.getKey().name());
                    ps.addBatch();
                }
                if (e.getValue() instanceof ListOfStrings) {
                    ListOfStrings lOfS = (ListOfStrings) e.getValue();
                    String listOfString = lOfS.getList().stream().collect(Collectors.joining("\n"));
                    ps.setString(1, listOfString);
                    ps.setString(2, r.getUuid());
                    ps.setString(3, e.getKey().name());
                    ps.addBatch();
                }
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

