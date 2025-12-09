package me.aidimirov.provider;

import com.j256.ormlite.db.DatabaseType;
import com.zaxxer.hikari.HikariDataSource;
import me.aidimirov.property.DatabaseProperty;

public abstract class HikariDataSourceProvider<DATABASE_TYPE extends DatabaseType> extends DataSourceProvider<HikariDataSource, DATABASE_TYPE> {

    public HikariDataSourceProvider(DATABASE_TYPE databaseType, DatabaseProperty property) {
        super(databaseType, property);
    }
}
