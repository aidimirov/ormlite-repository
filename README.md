# ormlite‑repository

[![Maven Central](https://img.shields.io/badge/maven-central-available-brightgreen)](#)  
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](#LICENSE)  

**Обёртка над ORMLite — Репозитории как в Spring JPA для упрощённой работы с базой данных на Java.**

## 🚀 Зачем это

Разрабатывая свой игровой сервер для майнкрафта я столкнулся с тем, что у меня уже накопилось очень много сущностей: User, Weapon, Backpack, UserFriend и т.д.
И весь этот шаблонный код занимал огромное время. Наткнулся на ORMLite, так как использовать тяжелые ORM для своего проекта не хотелось.
ORMLite довольно старая библиотека, но для меня это подошло, единственное что мне там не хватало — Spring JPA Repositiories. 
В этом проекте я реализовал взаимодействие с ORMLite через привычные мне репозитории, что сделало работу с сущностями еще проще.

Я не стал публиковать этот репозиторий в maven или другие хранилища, так как здесь всего 5 классов, их можно просто адаптировать под себя, добавить новые реализации провайдеров БД.

## ✨ Основные возможности

- Структура, похожая на подход с репозиториями (repository interface + реализация).  
- Базовый репозиторий с CRUD‑операциями (Create / Read / Update / Delete).
- Удобная работа с сущностями — минимальные настройки.  
- Лёгкая интеграция в существующие Java‑проекты с ORMLite.

## Как это использовать?

1. Для начала необходимо создать сущность с нужными вам аннотациями ORMLite, в данном случае я создал сущность User. Обязательно реализуйте интерфейс Identified<ID>, где ID - тип идентификатора.
```java
package me.aidimirov;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.aidimirov.repository.Identified;

import java.util.UUID;

@Getter
@DatabaseTable(tableName = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Identified<UUID> {

    @DatabaseField(id = true)
    private UUID id;
    @DatabaseField(canBeNull = false, unique = true)
    private String name;
}
```

2. Создать репозиторий сущности, где помимо основных CRUD-операций я хочу чтобы был метод findByName
```java
package me.aidimirov;

import me.aidimirov.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {

    Optional<User> findByName(String name);

}
```

3. Реализовать репозиторий на основе класса DatabaseRepository

```java
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
```

4. В результате вы получаете удобное взаимодействие с вашими сущностями

```java
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

```
