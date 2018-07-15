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
        T execute(Connection con) throws SQLException;
    }

    public <T> T execute(PreparedStatement ps, SQLHelper.SQLExecutor<T> helper, String query, String... parameters) {
        try (Connection conn = connectionFactory.getConnection()) {

            if (ps == null) {
                ps = conn.prepareStatement(query);
            }
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

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
