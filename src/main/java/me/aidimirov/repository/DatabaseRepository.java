package me.aidimirov.repository;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class DatabaseRepository<TYPE extends Identified<ID>, ID> extends BaseDaoImpl<TYPE, ID>
        implements Repository<TYPE, ID>, Dao<TYPE, ID> {

    public DatabaseRepository(ConnectionSource connectionSource, Class<TYPE> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        TableUtils.createTableIfNotExists(connectionSource, this.getDataClass());
        this.setObjectCache(null);
    }

    @Override
    public Optional<TYPE> findById(@NotNull ID id) {
        try {
            return Optional.ofNullable(super.queryForId(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TYPE> findAll() {
        try {
            return queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TYPE save(@NotNull TYPE type) {
        try {
            ID id = super.extractId(type);
            boolean exists = super.idExists(id);
            if (exists) {
                super.update(type);
            } else {
                type = super.createIfNotExists(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return type;
    }

    @Override
    public int deleteById(@NotNull ID id) {
        try {
            TYPE entity = queryForId(id);
            if (entity != null) {
                return super.delete(entity);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(@NotNull TYPE type)  {
        try {
            ID id = super.extractId(type);
            if (!super.idExists(id)) {
                return 0;
            }
            return super.delete(type);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteAll() {
        try {
            return TableUtils.clearTable(connectionSource, getDataClass());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(@NotNull ID id) {
        try {
            return idExists(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}