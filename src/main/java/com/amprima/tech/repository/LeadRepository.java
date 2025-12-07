package com.amprima.tech.repository;

import com.amprima.tech.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    List<Lead> findByStatus(String status);

    List<Lead> findByEmail(String email);

    List<Lead> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT l.source, COUNT(l) FROM Lead l GROUP BY l.source")
    List<Object[]> countLeadsBySource();

    @Query("SELECT l FROM Lead l WHERE l.createdAt >= :sinceDate ORDER BY l.createdAt DESC")
    List<Lead> findRecentLeads(LocalDateTime sinceDate);
}
