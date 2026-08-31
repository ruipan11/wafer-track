package com.wafertrack.repository;

import com.wafertrack.domain.LotHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LotHistoryRepository extends JpaRepository<LotHistory, Long> {

    Optional<LotHistory> findByLotIdAndTrackOutTimeIsNull(Long lotId);
}