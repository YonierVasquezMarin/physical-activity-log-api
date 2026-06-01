package com.company.physical_activity_log_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.physical_activity_log_api.model.CategoryActivity;

public interface CategoryActivityRepository extends JpaRepository<CategoryActivity, Integer> {
}

