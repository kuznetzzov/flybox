package com.flybuilder.flybox.model.db.repository;

import com.flybuilder.flybox.model.db.entity.Fly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlyRepo extends JpaRepository<Fly, Long> {
}
