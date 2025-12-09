package me.aidimirov;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.aidimirov.repository.Identified;

import java.util.UUID;

@Getter
@DatabaseTable(tableName = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Identified<UUID> {

    @DatabaseField(id = true)
    private UUID id;
    @DatabaseField(canBeNull = false, unique = true)
    private String name;
}
