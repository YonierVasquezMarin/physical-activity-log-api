package com.company.physical_activity_log_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.CategoryResponse;
import com.company.physical_activity_log_api.model.CategoryActivity;
import com.company.physical_activity_log_api.repository.CategoryActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryActivityRepository categoryActivityRepository;

	@Transactional(readOnly = true)
	public List<CategoryResponse> list() {
		return categoryActivityRepository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	private CategoryResponse toResponse(CategoryActivity category) {
		return new CategoryResponse(
				category.getId(),
				category.getName(),
				category.getDescription());
	}
}

