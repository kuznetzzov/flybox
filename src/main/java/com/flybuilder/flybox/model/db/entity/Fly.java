package com.flybuilder.flybox.model.db.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.model.enums.FlyType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "flies")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    FlyType flyType;
    String description;
    String pic;
    String video;
    Status status;
    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;
    @Column(name = "updated_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updatedAt;

    @OneToMany
    @JsonManagedReference(value = "fly_histories")
    Set<History> histories;

    @ManyToMany
    @JoinTable(name = "fly_material",
            joinColumns = @JoinColumn(name = "fly_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    Set<Material> materials = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "fly_place",
            joinColumns = @JoinColumn(name = "fly_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id"))
    Set<Place> places = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "fly_user",
            joinColumns = @JoinColumn(name = "fly_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> users = new HashSet<>();
}
