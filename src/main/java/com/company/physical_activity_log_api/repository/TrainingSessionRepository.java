package com.company.physical_activity_log_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.physical_activity_log_api.model.TrainingSession;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {

	@Query("""
			SELECT DISTINCT ts FROM TrainingSession ts
			JOIN FETCH ts.sessionActivities sa
			JOIN FETCH sa.activity a
			JOIN FETCH a.category
			WHERE ts.user.id = :userId
			ORDER BY ts.date DESC
			""")
	List<TrainingSession> findByUserIdWithActivitiesOrderByDateDesc(@Param("userId") Integer userId);

	@Query("""
			SELECT ts FROM TrainingSession ts
			JOIN FETCH ts.sessionActivities sa
			JOIN FETCH sa.activity a
			JOIN FETCH a.category
			WHERE ts.id = :id AND ts.user.id = :userId
			""")
	Optional<TrainingSession> findByIdAndUserIdWithActivities(
			@Param("id") Integer id,
			@Param("userId") Integer userId);

	boolean existsByIdAndUserId(Integer id, Integer userId);
}

