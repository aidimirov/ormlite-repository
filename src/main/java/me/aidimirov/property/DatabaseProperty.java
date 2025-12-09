package me.aidimirov.property;

import java.util.Properties;

public interface DatabaseProperty {

    String getAddress();

    String getPort();

    String getUsername();

    String getPassword();

    String getDatabase();

    int getMaxConnections();

    static void setProps(Properties props, DatabaseProperty property) {
        props.setProperty("dataSource.serverName", property.getAddress());
        props.setProperty("dataSource.portNumber", property.getPort());
        props.setProperty("dataSource.user", property.getUsername());
        props.setProperty("dataSource.password", property.getPassword());
        props.setProperty("dataSource.databaseName", property.getDatabase());
    }
}