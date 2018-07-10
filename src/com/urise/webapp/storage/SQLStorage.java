package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactName;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLStorage implements Storage {

    private final SQLHelper helper;

    private final static String CLEAR_QUERY;
    private final static String GET_QUERY;
    private final static String UPDATE_QUERY;
    private final static String INSERT_RESUME;
    private final static String DELETE_QUERY;
    private final static String GET_ALL_QUERY;
    private final static String SIZE_QUERY;
    private final static String SELECT_LEFT_JOIN;
    private static final String INSERT_CONTACT;
    private static final String UPDATE_RESUME;
    private static final String UPDATE_CONTACT;

    static {
        CLEAR_QUERY = "DELETE FROM resume";
        GET_QUERY = "SELECT * FROM resume r WHERE r.uuid =?";
        UPDATE_QUERY = "UPDATE resume SET full_name = ? WHERE uuid =?";
        INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        DELETE_QUERY = "DELETE FROM resume where uuid=?";
        GET_ALL_QUERY = "SELECT * FROM resume ORDER BY uuid";
        SIZE_QUERY = "SELECT COUNT(*) AS total FROM resume";
        SELECT_LEFT_JOIN = "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid =? ";
        INSERT_CONTACT = "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)";
        UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid =?";
        UPDATE_CONTACT = "UPDATE contact SET value = ? WHERE resume_uuid =? AND type = ?";
    }

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SQLHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        helper.execute(CLEAR_QUERY);
    } //каскадное удление

    @Override
    public Resume get(String uuid) {
        return helper.transactionalExecute(
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String value = rs.getString("value");
                        String type = rs.getString("type");
                        if (value != null && type != null) {
                            ContactName name = ContactName.valueOf(type);
                            r.addContact(name, value);
                        } else throw new StorageException("no contacts for this resume");
                    } while (rs.next());
                    return r;
                }, SELECT_LEFT_JOIN, uuid);
    }

    @Override
    public void update(Resume r) {
        if (helper.transactionalExecute(PreparedStatement::executeUpdate,
                UPDATE_RESUME, r.getFullName(), r.getUuid()) == 0)
            throw new NotExistStorageException("there is no such resume");

        helper.transactionalExecute(ps -> {
            executeBatchContacts(r, ps, 2, 3, 1);
            return null;
        }, UPDATE_CONTACT);
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(PreparedStatement::execute, INSERT_RESUME, r.getUuid(), r.getFullName());

        helper.transactionalExecute(ps -> {
            executeBatchContacts(r, ps, 1, 2, 3);
            return null;
        }, INSERT_CONTACT);
    }

    @Override
    public void delete(String uuid) {
        if (helper.execute(DELETE_QUERY, uuid) == 0) //каскадное удаление
            throw new NotExistStorageException("there is no such uuid");
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.execute(ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> rList = new ArrayList<>();
            while (rs.next()) {
                rList.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return rList;
        }, GET_ALL_QUERY);
    }

    @Override
    public int size() {
        return helper.execute(ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("result set is empty");
            }
            return rs.getInt("total");
        }, SIZE_QUERY);
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


}
