package com.flybox.model.db.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "places")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String coordinates;
    Float flySinking;
    Boolean isSalt;

    @ManyToMany(mappedBy = "places")
    Set<Fly> flies = new HashSet<>();
}
