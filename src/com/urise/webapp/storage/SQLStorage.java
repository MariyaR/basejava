package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLStorage implements Storage {

    private final SQLHelper<Resume> resumeHelper;
    private final SQLHelper<List<Resume>> listHelper;
    private final SQLHelper<Integer> intHelper;

    private final static String CLEAR_QUERY;
    private final static String GET_QUERY;
    private final static String UPDATE_QUERY;
    private final static String SAVE_QUERY;
    private final static String DELETE_QUERY;
    private final static String GET_ALL_QUERY;
    private final static String SIZE_QUERY;

    static {
        CLEAR_QUERY = "DELETE FROM resume";
        GET_QUERY = "SELECT * FROM resume r WHERE r.uuid =?";
        UPDATE_QUERY = "UPDATE resume SET full_name = ? WHERE uuid =?";
        SAVE_QUERY = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        DELETE_QUERY = "DELETE FROM resume where uuid=?";
        GET_ALL_QUERY = "SELECT * FROM resume";
        SIZE_QUERY = "SELECT COUNT(*) AS total FROM resume";
    }

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
        resumeHelper = new SQLHelper<Resume>(dbUrl, dbUser, dbPassword);
        listHelper = new SQLHelper<List<Resume>>(dbUrl, dbUser, dbPassword);
        intHelper = new SQLHelper<Integer>(dbUrl, dbUser, dbPassword, PreparedStatement::executeUpdate);
    }

    @Override
    public void clear() {
        intHelper.help(CLEAR_QUERY);
    }

    @Override
    public Resume get(String uuid) {
        return resumeHelper.help(ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        }, GET_QUERY, uuid);
    }

    @Override
    public void update(Resume r) {
        if (intHelper.help(UPDATE_QUERY, r.getFullName(), r.getUuid()) == 0)
            throw new NotExistStorageException("there is no such resume");
    }

    @Override
    public void save(Resume r) {
        intHelper.help(pr -> {
            try {
                return pr.executeUpdate();
            } catch (PSQLException e) {
                throw new ExistStorageException("this resume is already exist");
            }
        }, SAVE_QUERY, r.getUuid(), r.getFullName());

    }

    @Override
    public void delete(String uuid) {
        if (intHelper.help(DELETE_QUERY, uuid) == 0)
            throw new NotExistStorageException("there is no such uuid");
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = listHelper.help(ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> rList = new ArrayList<>();
                    while (rs.next()) {
                        rList.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                    }
                    return rList;
                }
                , GET_ALL_QUERY, null);

        Collections.sort(list);
        return list;
    }

    @Override
    public int size() {
        return intHelper.help(ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("result set is empty");
            }
            return rs.getInt("total");
        }, SIZE_QUERY, null);
    }

    public static class SQLHelper<T> {
        private final ConnectionFactory connectionFactory;
        private Helper<T> helper;

        SQLHelper(String dbUrl, String dbUser, String dbPassword, Helper<T> helper) {
            this(dbUrl, dbUser, dbPassword);
            this.helper = helper;
        }

        SQLHelper(String dbUrl, String dbUser, String dbPassword) {
            connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }

        public interface Helper<K> {
            K doHelp(PreparedStatement ps) throws SQLException;
        }

        T help(Helper<T> helper, String query) {
            return this.help(helper, query, null);
        }

        T help(String query, String... parameters) {
            return this.help(helper, query, parameters);
        }

        T help(String query) {
            return this.help(helper, query);
        }

        T help(Helper<T> helper, String query, String... parameters) {
            try (Connection conn = connectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                if (parameters != null) {
                    int size = parameters.length;
                    for (int i = 0; i < size; i++) {
                        ps.setString(i + 1, parameters[i]);
                    }
                }
                return helper.doHelp(ps);
            } catch (SQLException e) {
                throw new StorageException(e);
            }
        }
    }
}
