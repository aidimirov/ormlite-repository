package me.aidimirov.provider.impl;

import com.j256.ormlite.jdbc.db.MariaDbDatabaseType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.aidimirov.property.DatabaseProperty;
import me.aidimirov.provider.HikariDataSourceProvider;

import java.util.Properties;

public class MariaDbHikariDataSourceProvider extends HikariDataSourceProvider<MariaDbDatabaseType> {

    public MariaDbHikariDataSourceProvider(DatabaseProperty property) {
        super(new MariaDbDatabaseType(), property);
    }

    @Override
    protected HikariDataSource initSource() {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.mariadb.jdbc.MariaDbDataSource");
        DatabaseProperty.setProps(props, getDatabaseProperty());

        HikariConfig config = new HikariConfig(props);
        config.setMaximumPoolSize(getDatabaseProperty().getMaxConnections());

        return new HikariDataSource(config);
    }
}
