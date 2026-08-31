package com.wafertrack.repository;

import com.wafertrack.domain.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Long> {
    Optional<Lot> findByLotNo(String lotNo);
}