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

    private final static String CLEAR_DB;
    private final static String COUNT;
    private final static String DELETE_RESUME;
    private static final String INSERT_CONTACT;
    private final static String INSERT_RESUME;
    private final static String GET_ALL;
    private final static String GET_RESUME_CONTACTS;
    private static final String UPDATE_CONTACT;
    private static final String UPDATE_RESUME;

    private static PreparedStatement clearPrepStatement;
    private static PreparedStatement countPrepStatement;
    private static PreparedStatement deletePrepStatement;
    private static PreparedStatement insertContactPrepStatement;
    private static PreparedStatement insertResumePrepStatement;
    private static PreparedStatement getPrepStatement;
    private static PreparedStatement getAllPrepStatement;
    private static PreparedStatement updateContactPrepStatement;
    private static PreparedStatement updateResumePrepStatement;

    static {
        CLEAR_DB = "DELETE FROM resume";
        COUNT = "SELECT COUNT(*) AS total FROM resume";
        DELETE_RESUME = "DELETE FROM resume where uuid=?";
        INSERT_CONTACT = "INSERT INTO contact (value, resume_uuid, type) VALUES (?,?,?)";
        INSERT_RESUME = "INSERT INTO resume (full_name, uuid) VALUES (?,?)";
        GET_ALL = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name";
        GET_RESUME_CONTACTS = "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid =? ";
        UPDATE_CONTACT = "UPDATE contact SET value = ? WHERE resume_uuid =? AND type = ?";
        UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid =?";
    }

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SQLHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        helper.execute(clearPrepStatement, PreparedStatement::executeUpdate, CLEAR_DB);
    }

    @Override
    public Resume get(String uuid) {
        return helper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(GET_RESUME_CONTACTS)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                Resume r = new Resume(uuid, rs.getString("full_name"));
                do {
                    insertContact(rs, r);
                } while (rs.next());
                return r;
            }
        });
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute(conn -> {
                    if (insertOrUpdateResume(r, conn, UPDATE_RESUME) == 0) throw new NotExistStorageException(r.getUuid());
                    insertOrUpdateContacts(r, conn, UPDATE_CONTACT);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(conn -> {
                    insertOrUpdateResume(r, conn, INSERT_RESUME);
                    insertOrUpdateContacts(r, conn, INSERT_CONTACT);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        int result = helper.execute(deletePrepStatement, PreparedStatement::executeUpdate, DELETE_RESUME, uuid);
        if (result == 0) {
            throw new NotExistStorageException("no such resume in data base");
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
                return getListResumes(ps);
            }
        });
    }

    @Override
    public int size() {
        return helper.execute(countPrepStatement, ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt("total");
        }, COUNT);
    }

    private void insertContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        String type = rs.getString("type");
        if (value != null && type != null) {
            ContactName name = ContactName.valueOf(type);
            r.addContact(name, value);
        }
    }

    private List<Resume> getListResumes(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Resume> rList = new ArrayList<>();
        Resume r = new Resume("dummy", "dummy");
        while (rs.next()) {
            if (rs.getString("uuid").equals(r.getUuid())) {
                insertContact(rs, r);
            } else {
                if (!r.getUuid().equals("dummy")) {
                    rList.add(r);
                }
                r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                insertContact(rs, r);
            }
        }
        rList.add(r);
        return rList;
    }

    private void insertOrUpdateContacts(Resume r, Connection conn, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
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


}

