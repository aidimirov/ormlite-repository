package me.aidimirov.provider;

import com.j256.ormlite.db.DatabaseType;
import me.aidimirov.property.DatabaseProperty;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataSourceProvider<DATA_SOURCE extends DataSource, DATABASE_TYPE extends DatabaseType> {

    protected final DATABASE_TYPE databaseType;
    protected final DatabaseProperty property;
    protected final DATA_SOURCE dataSource;

    public DataSourceProvider(DATABASE_TYPE databaseType, DatabaseProperty property) {
        this.databaseType = databaseType;
        this.property = property;
        dataSource = initSource();
    }

    protected abstract DATA_SOURCE initSource();

    protected boolean testConnection(DataSource source) throws SQLException {
        try (Connection conn = source.getConnection()) {
            return conn.isValid(5 * 1000);
        }
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DatabaseProperty getDatabaseProperty() {
        return property;
    }
}