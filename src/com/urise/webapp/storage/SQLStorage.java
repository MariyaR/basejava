package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactName;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                insertContact(rs, resume);
            } while (rs.next());
            return resume;
        }, "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid =? ");
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute(conn -> {
                    if (insertOrUpdateResume(r, conn, "UPDATE resume SET full_name = ? WHERE uuid =?") == 0)
                        throw new NotExistStorageException(r.getUuid());
                    deleteContacts(r, conn);
                    insertContacts(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(conn -> {
                    insertOrUpdateResume(r, conn, "INSERT INTO resume (full_name, uuid) VALUES (?,?)");
                    insertContacts(r, conn);
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
        return helper.execute(this::getListResumes, "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name");
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

    private void insertContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        String type = rs.getString("type");
        if (value != null && type != null) {
            ContactName name = ContactName.valueOf(type);
            r.addContact(name, value);
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

    private int insertOrUpdateResume(Resume r, Connection conn, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            return ps.executeUpdate();
        }
    }

    private List<Resume> getListResumes(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Resume> rList = new ArrayList<>();
        Resume r = new Resume("", "dummy");
        while (rs.next()) {
            if (!rs.getString("uuid").equals(r.getUuid())) {
                if (!r.getUuid().equals("")) {
                    rList.add(r);
                }
                r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
            }
            insertContact(rs, r);
        }
        rList.add(r);
        return rList;
    }

}

