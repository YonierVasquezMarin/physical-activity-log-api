package com.company.physical_activity_log_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.physical_activity_log_api.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

	List<Activity> findByCategoryId(Integer categoryId);
}

