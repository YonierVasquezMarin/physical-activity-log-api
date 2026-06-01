package com.company.physical_activity_log_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.ActivityResponse;
import com.company.physical_activity_log_api.exception.ResourceNotFoundException;
import com.company.physical_activity_log_api.model.Activity;
import com.company.physical_activity_log_api.repository.ActivityRepository;
import com.company.physical_activity_log_api.repository.CategoryActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final CategoryActivityRepository categoryActivityRepository;

	@Transactional(readOnly = true)
	public List<ActivityResponse> listAll() {
		return activityRepository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ActivityResponse> listByCategory(Integer categoryId) {
		if (!categoryActivityRepository.existsById(categoryId)) {
			throw new ResourceNotFoundException("Categoría no encontrada");
		}

		return activityRepository.findByCategoryId(categoryId).stream()
				.map(this::toResponse)
				.toList();
	}

	private ActivityResponse toResponse(Activity activity) {
		return new ActivityResponse(
				activity.getId(),
				activity.getCategory().getId(),
				activity.getName(),
				activity.getDescription());
	}
}

