package com.company.physical_activity_log_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.physical_activity_log_api.model.GoalProgress;

public interface GoalProgressRepository extends JpaRepository<GoalProgress, Integer> {

	@Query("""
			SELECT gp FROM GoalProgress gp
			JOIN FETCH gp.session s
			JOIN FETCH gp.goal g
			WHERE g.user.id = :userId
			ORDER BY s.date ASC
			""")
	List<GoalProgress> findByUserIdWithSessionOrderByDateAsc(@Param("userId") Integer userId);

	@Query("""
			SELECT COUNT(g) FROM Goal g
			WHERE g.user.id = :userId
			AND NOT EXISTS (SELECT 1 FROM GoalProgress gp WHERE gp.goal = g)
			""")
	long countGoalsWithoutProgressByUserId(@Param("userId") Integer userId);

}
