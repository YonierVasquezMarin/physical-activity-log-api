package com.company.physical_activity_log_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.physical_activity_log_api.model.CategoryActivity;

public interface CategoryActivityRepository extends JpaRepository<CategoryActivity, Integer> {

	List<CategoryActivity> findByUser_IdOrderByNameAsc(Integer userId);

	@Query("SELECT c FROM CategoryActivity c WHERE c.user IS NULL OR c.user.id = :userId ORDER BY c.name ASC")
	List<CategoryActivity> findAccessibleByUserIdOrderByNameAsc(@Param("userId") Integer userId);

	boolean existsByUserIsNullAndName(String name);

	Optional<CategoryActivity> findByIdAndUser_Id(Integer id, Integer userId);

	boolean existsByIdAndUser_Id(Integer id, Integer userId);

	@Query("SELECT c FROM CategoryActivity c WHERE c.id = :id AND (c.user IS NULL OR c.user.id = :userId)")
	Optional<CategoryActivity> findAccessibleById(@Param("id") Integer id, @Param("userId") Integer userId);
}

