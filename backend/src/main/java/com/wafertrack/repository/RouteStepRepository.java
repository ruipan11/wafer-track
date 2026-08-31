package com.wafertrack.repository;

import com.wafertrack.domain.RouteStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteStepRepository extends JpaRepository<RouteStep, Long> {

    Optional<RouteStep> findByRouteIdAndStepSeq(Long routeId, Integer stepSeq);

    Optional<RouteStep> findFirstByRouteIdAndStepSeqGreaterThanOrderByStepSeqAsc(
            Long routeId, Integer stepSeq);
}