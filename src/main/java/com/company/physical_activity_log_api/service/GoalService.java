package com.company.physical_activity_log_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.GoalCreateRequest;
import com.company.physical_activity_log_api.dto.GoalResponse;
import com.company.physical_activity_log_api.dto.GoalUpdateRequest;
import com.company.physical_activity_log_api.exception.ResourceNotFoundException;
import com.company.physical_activity_log_api.model.Goal;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.GoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalService {

	private final GoalRepository goalRepository;

	@Transactional(readOnly = true)
	public List<GoalResponse> list(User user) {
		return goalRepository.findByUserIdOrderByStartDateDesc(user.getId()).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public GoalResponse getById(User user, Integer id) {
		Goal goal = goalRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Meta no encontrada"));
		return toResponse(goal);
	}

	@Transactional
	public GoalResponse create(User user, GoalCreateRequest request) {
		validateDates(request.getStartDate(), request.getEndDate());

		Goal goal = new Goal();
		goal.setUser(user);
		goal.setTitle(request.getTitle());
		goal.setDescription(request.getDescription());
		goal.setStartDate(request.getStartDate());
		goal.setEndDate(request.getEndDate());

		Goal saved = goalRepository.save(goal);
		return toResponse(saved);
	}

	@Transactional
	public GoalResponse update(User user, Integer id, GoalUpdateRequest request) {
		validateDates(request.getStartDate(), request.getEndDate());

		Goal goal = goalRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Meta no encontrada"));

		goal.setTitle(request.getTitle());
		goal.setDescription(request.getDescription());
		goal.setStartDate(request.getStartDate());
		goal.setEndDate(request.getEndDate());

		return toResponse(goal);
	}

	@Transactional
	public void delete(User user, Integer id) {
		if (!goalRepository.existsByIdAndUserId(id, user.getId())) {
			throw new ResourceNotFoundException("Meta no encontrada");
		}
		goalRepository.deleteById(id);
	}

	private void validateDates(java.time.OffsetDateTime start, java.time.OffsetDateTime end) {
		if (start.isAfter(end)) {
			throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
		}
	}

	private GoalResponse toResponse(Goal goal) {
		return new GoalResponse(
				goal.getId(),
				goal.getTitle(),
				goal.getDescription(),
				goal.getStartDate(),
				goal.getEndDate());
	}
}

