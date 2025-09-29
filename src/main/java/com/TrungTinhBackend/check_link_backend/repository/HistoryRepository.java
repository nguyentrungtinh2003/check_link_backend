package com.TrungTinhBackend.check_link_backend.repository;

import com.TrungTinhBackend.check_link_backend.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findByUserId(Long userId);
    Page<History> findByUserIdAndUrlCheckContainingIgnoreCase(Long userId, String keyword, Pageable pageable);
}
