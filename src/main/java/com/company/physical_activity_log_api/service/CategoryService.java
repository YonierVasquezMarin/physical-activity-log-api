package com.company.physical_activity_log_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.CategoryCreateRequest;
import com.company.physical_activity_log_api.dto.CategoryResponse;
import com.company.physical_activity_log_api.dto.CategoryUpdateRequest;
import com.company.physical_activity_log_api.exception.ResourceNotFoundException;
import com.company.physical_activity_log_api.model.CategoryActivity;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.CategoryActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryActivityRepository categoryActivityRepository;

	@Transactional(readOnly = true)
	public List<CategoryResponse> list(User user) {
		return categoryActivityRepository.findByUserIsNullOrUser_IdOrderByNameAsc(user.getId()).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public CategoryResponse getById(User user, Integer id) {
		CategoryActivity category = categoryActivityRepository.findAccessibleById(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
		return toResponse(category);
	}

	@Transactional
	public CategoryResponse create(User user, CategoryCreateRequest request) {
		CategoryActivity category = new CategoryActivity();
		category.setUser(user);
		category.setName(request.getName());
		category.setDescription(request.getDescription());

		CategoryActivity saved = categoryActivityRepository.save(category);
		return toResponse(saved);
	}

	@Transactional
	public CategoryResponse update(User user, Integer id, CategoryUpdateRequest request) {
		CategoryActivity category = categoryActivityRepository.findByIdAndUser_Id(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

		category.setName(request.getName());
		category.setDescription(request.getDescription());

		return toResponse(category);
	}

	@Transactional
	public void delete(User user, Integer id) {
		if (!categoryActivityRepository.existsByIdAndUser_Id(id, user.getId())) {
			throw new ResourceNotFoundException("Categoría no encontrada");
		}
		categoryActivityRepository.deleteById(id);
	}

	private CategoryResponse toResponse(CategoryActivity category) {
		Integer userId = category.getUser() != null ? category.getUser().getId() : null;
		return new CategoryResponse(
				category.getId(),
				userId,
				category.getName(),
				category.getDescription());
	}
}
