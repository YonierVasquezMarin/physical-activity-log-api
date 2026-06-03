package com.company.physical_activity_log_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.ActivityCreateRequest;
import com.company.physical_activity_log_api.dto.ActivityResponse;
import com.company.physical_activity_log_api.exception.ResourceNotFoundException;
import com.company.physical_activity_log_api.model.Activity;
import com.company.physical_activity_log_api.model.CategoryActivity;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.ActivityRepository;
import com.company.physical_activity_log_api.repository.CategoryActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final CategoryActivityRepository categoryActivityRepository;

	@Transactional
	public ActivityResponse create(User user, ActivityCreateRequest request) {
		CategoryActivity category = categoryActivityRepository
				.findAccessibleById(request.getCategoryId(), user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

		Activity activity = new Activity();
		activity.setCategory(category);
		activity.setName(request.getName());
		activity.setDescription(request.getDescription());

		Activity saved = activityRepository.save(activity);
		return toResponse(saved);
	}

	private ActivityResponse toResponse(Activity activity) {
		return new ActivityResponse(
				activity.getId(),
				activity.getCategory().getId(),
				activity.getName(),
				activity.getDescription());
	}
}

