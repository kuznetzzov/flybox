package com.flybox.model.db.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "histories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String hook;
    String tail;
    String ribbing;
    String body;
    String hackle;
    String wing;
    String legs;
    String head;

    @ManyToOne
    @JsonBackReference(value = "fly_histories")
    Fly fly;
}
