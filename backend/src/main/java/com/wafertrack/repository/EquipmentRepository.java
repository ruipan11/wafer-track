package com.wafertrack.repository;

import com.wafertrack.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findByEqpCode(String eqpCode);
}