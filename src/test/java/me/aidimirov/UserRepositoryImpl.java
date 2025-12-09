package me.aidimirov;

import com.j256.ormlite.support.ConnectionSource;
import me.aidimirov.repository.DatabaseRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl extends DatabaseRepository<User, UUID> implements UserRepository {

    public UserRepositoryImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, User.class);
    }

    @Override
    public Optional<User> findByName(String name) {
        List<User> user;
        try {
            user = super.queryForEq("name", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user.isEmpty()) return Optional.empty();
        return Optional.of(user.getFirst());
    }
}
