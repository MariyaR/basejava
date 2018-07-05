package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLStorage implements Storage {

    private final SQLHelper<Boolean> Helper;
    private final SQLHelper<Resume> resumeHelper;
    private final SQLHelper<List<Resume>> listHelper;
    private final SQLHelper<Integer> intHelper;

    final static String CLEAR_QUERY;
    final static String GET_QUERY;
    final static String UPDATE_QUERY;
    final static String SAVE_QUERY;
    final static String DELETE_QUERY;
    final static String GET_ALL_QUERY;
    final static String SIZE_QUERY;

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
        Helper = new SQLHelper<Boolean>(dbUrl, dbUser, dbPassword);
        resumeHelper = new SQLHelper<Resume>(dbUrl, dbUser, dbPassword);
        listHelper = new SQLHelper<List<Resume>>(dbUrl, dbUser, dbPassword);
        intHelper = new SQLHelper<Integer>(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        Helper.help(PreparedStatement::execute, CLEAR_QUERY, null);
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
        Helper.help(PreparedStatement::execute, UPDATE_QUERY, r.getFullName(), r.getUuid());
    }

    @Override
    public void save(Resume r) {
        Helper.help(ps -> {
            try {
                return ps.execute();
            } catch (PSQLException e) {
                throw new ExistStorageException("this resume is already exist");
            }
        }, SAVE_QUERY, r.getUuid(), r.getFullName());
    }

    @Override
    public void delete(String uuid) {
        Helper.help(PreparedStatement::execute, DELETE_QUERY, uuid);
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
}
