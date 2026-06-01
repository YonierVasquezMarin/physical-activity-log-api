package com.company.physical_activity_log_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.physical_activity_log_api.model.TrainingSession;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {

	List<TrainingSession> findByUserIdOrderByDateDesc(Integer userId);

	Optional<TrainingSession> findByIdAndUserId(Integer id, Integer userId);

	boolean existsByIdAndUserId(Integer id, Integer userId);
}

