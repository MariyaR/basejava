package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLHelper {
    private final ConnectionFactory connectionFactory;

    public SQLHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public interface SQLExecutor<K> {
        K doHelp(PreparedStatement ps) throws SQLException;
    }

    public interface SqlTransaction<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public <T> T execute(SQLHelper.SQLExecutor<T> helper, String query, String... parameters) {
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

    public <T> T transactionalExecute(PreparedStatement ps, SQLHelper.SqlTransaction<T> executor, String query, String... parameters) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                if (ps == null) {
                    ps = conn.prepareStatement(query);
                }

                if (parameters != null) {
                    int size = parameters.length;
                    for (int i = 0; i < size; i++) {
                        ps.setString(i + 1, parameters[i]);
                    }
                }
                T result = executor.execute(ps);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
