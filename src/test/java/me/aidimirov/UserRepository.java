package me.aidimirov;

import me.aidimirov.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {

    Optional<User> findByName(String name);

}
