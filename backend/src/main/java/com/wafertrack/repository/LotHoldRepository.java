package com.wafertrack.repository;

import com.wafertrack.domain.LotHold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotHoldRepository extends JpaRepository<LotHold, Long> {

    List<LotHold> findByLotIdAndReleaseTimeIsNull(Long lotId);

    List<LotHold> findByLotIdOrderByHoldTimeDesc(Long lotId);

    boolean existsByLotIdAndReleaseTimeIsNull(Long lotId);
}