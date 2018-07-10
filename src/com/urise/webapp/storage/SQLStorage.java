package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactName;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

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
        INSERT_CONTACT = "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)";
        INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        GET_ALL = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "ORDER BY uuid";
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
        helper.transactionalExecute(clearPrepStatement, PreparedStatement::executeUpdate, CLEAR_DB);
    }

    @Override
    public Resume get(String uuid) {
        return helper.transactionalExecute(getPrepStatement,
                ExecuteGetQuery(uuid), GET_RESUME_CONTACTS, uuid);
    }

    @Override
    public void update(Resume r) {
        int result = helper.transactionalExecute(updateResumePrepStatement, PreparedStatement::executeUpdate,
                UPDATE_RESUME, r.getFullName(), r.getUuid());
        if (result == 0) {
            throw new NotExistStorageException("no such resume in data base");
        }
        helper.transactionalExecute(updateContactPrepStatement, ps -> {
            executeBatchContacts(r, ps, 2, 3, 1);
            return null;
        }, UPDATE_CONTACT);
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(insertResumePrepStatement, PreparedStatement::execute, INSERT_RESUME, r.getUuid(), r.getFullName());
        helper.transactionalExecute(insertContactPrepStatement, preparedStatement -> {
            executeBatchContacts(r, preparedStatement, 1, 2, 3);
            return null;
        }, INSERT_CONTACT);
    }


    @Override
    public void delete(String uuid) {
        int result = helper.transactionalExecute(deletePrepStatement, PreparedStatement::executeUpdate, DELETE_RESUME, uuid);
        if (result==0) {throw  new NotExistStorageException("no such resume in data base");}
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.transactionalExecute(getAllPrepStatement, this::getListResumes, GET_ALL);
    }

    @Override
    public int size() {
        return helper.transactionalExecute(countPrepStatement, ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt("total");
        }, COUNT);
    }

    private SQLHelper.SqlTransaction<Resume> ExecuteGetQuery(String uuid) {
        return ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                insertContact(rs, r);
            } while (rs.next());
            return r;
        };
    }

    private void executeBatchContacts(Resume r, PreparedStatement ps, int UuidIndex, int typeIndex, int valueIndex) throws SQLException {
        for (Map.Entry<ContactName, String> e : r.getContacts().entrySet()) {
            ps.setString(UuidIndex, r.getUuid());
            ps.setString(typeIndex, e.getKey().name());
            ps.setString(valueIndex, e.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
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

}

