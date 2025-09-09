package com.TrungTinhBackend.check_link_backend.repository;

import com.TrungTinhBackend.check_link_backend.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {
}
