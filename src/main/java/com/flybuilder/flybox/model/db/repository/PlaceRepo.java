package com.flybuilder.flybox.model.db.repository;

import com.flybuilder.flybox.model.db.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Long> {
}
