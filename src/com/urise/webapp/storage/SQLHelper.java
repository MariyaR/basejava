package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.*;

public class SQLHelper<T> {
    public final ConnectionFactory connectionFactory;

    public SQLHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public interface Helper<K> {
        K doHelp(PreparedStatement ps) throws SQLException;
    }

    public T help(Helper<T> helper, String query, String... parameters) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            if (parameters != null) {
                int size = parameters.length;
                for (int i = 0; i < size; i++) {
                    ps.setString(i+1, parameters[i]);
                }
            }
            return helper.doHelp(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }


}
