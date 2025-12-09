package me.aidimirov.repository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface Repository<TYPE extends Identified<ID>, ID> {

    TYPE save(@NotNull TYPE type);

    Optional<TYPE> findById(@NotNull ID id);

    List<TYPE> findAll();

    int delete(@NotNull TYPE type);

    int deleteById(@NotNull ID id);

    int deleteAll();

    boolean existsById(@NotNull ID id);

}