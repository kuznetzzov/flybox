package com.flybuilder.flybox.model.db.repository;

import com.flybuilder.flybox.model.db.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo extends JpaRepository<History, Long> {
}
