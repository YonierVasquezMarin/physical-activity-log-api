package com.company.physical_activity_log_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.physical_activity_log_api.model.Goal;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

	List<Goal> findByUserIdOrderByStartDateDesc(Integer userId);

	Optional<Goal> findByIdAndUserId(Integer id, Integer userId);

	boolean existsByIdAndUserId(Integer id, Integer userId);
}

