package me.aidimirov;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.junit.jupiter.api.*;
import java.util.*;

public class UserRepositoryTest {

    private ConnectionSource connectionSource;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite::memory:");

        userRepository = new UserRepositoryImpl(connectionSource);
    }

    @AfterEach
    void tearDown() throws Exception {
        connectionSource.close();
    }

    @Test
    void testSaveAndFindById() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "TestUser");

        userRepository.save(user);

        Optional<User> loaded = userRepository.findById(id);

        Assertions.assertTrue(loaded.isPresent());
        Assertions.assertEquals("TestUser", loaded.get().getName());
    }

    @Test
    void testFindByName() {
        User user = new User(UUID.randomUUID(), "Aidimirov");
        userRepository.save(user);

        Optional<User> found = userRepository.findByName("Aidimirov");

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Aidimirov", found.get().getName());
    }

    @Test
    void testFindAll() {
        userRepository.save(new User(UUID.randomUUID(), "Aidimirov"));
        userRepository.save(new User(UUID.randomUUID(), "Aidimirov 2"));

        List<User> all = userRepository.findAll();

        Assertions.assertEquals(2, all.size());
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();
        userRepository.save(new User(id, "Aidimirov"));

        Assertions.assertTrue(userRepository.existsById(id));
    }

    @Test
    void testDeleteById() {
        User user = new User(UUID.randomUUID(), "Aidimirov");
        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Assertions.assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void testDeleteUser() {
        User user = new User(UUID.randomUUID(), "Aidimirov");
        userRepository.save(user);
        userRepository.delete(user);

        Assertions.assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void testDeleteAll() {
        userRepository.save(new User(UUID.randomUUID(), "Aidimirov"));
        userRepository.save(new User(UUID.randomUUID(), "Aidimirov 2"));

        userRepository.deleteAll();

        Assertions.assertEquals(0, userRepository.findAll().size());
    }
}
