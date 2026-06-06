package com.company.physical_activity_log_api.service;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.ActivityResponse;
import com.company.physical_activity_log_api.dto.TrainingSessionCreateRequest;
import com.company.physical_activity_log_api.dto.TrainingSessionResponse;
import com.company.physical_activity_log_api.dto.TrainingSessionUpdateRequest;
import com.company.physical_activity_log_api.exception.ResourceNotFoundException;
import com.company.physical_activity_log_api.model.Activity;
import com.company.physical_activity_log_api.model.TrainingSession;
import com.company.physical_activity_log_api.model.TrainingSessionActivity;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.ActivityRepository;
import com.company.physical_activity_log_api.repository.TrainingSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingSessionService {

	private final TrainingSessionRepository trainingSessionRepository;
	private final ActivityRepository activityRepository;

	@Transactional(readOnly = true)
	public List<TrainingSessionResponse> list(User user) {
		return trainingSessionRepository.findByUserIdWithActivitiesOrderByDateDesc(user.getId()).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public TrainingSessionResponse getById(User user, Integer id) {
		TrainingSession session = trainingSessionRepository.findByIdAndUserIdWithActivities(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
		return toResponse(session);
	}

	@Transactional
	public TrainingSessionResponse create(User user, TrainingSessionCreateRequest request) {
		List<Activity> activities = resolveActivities(request.getActivityIds());

		TrainingSession session = new TrainingSession();
		session.setUser(user);
		session.setDate(request.getDate());
		session.setObservations(request.getObservations());
		session.setPhotoName(request.getPhotoName());
		setSessionActivities(session, activities);

		TrainingSession saved = trainingSessionRepository.save(session);
		return toResponse(saved);
	}

	@Transactional
	public TrainingSessionResponse update(User user, Integer id, TrainingSessionUpdateRequest request) {
		TrainingSession session = trainingSessionRepository.findByIdAndUserIdWithActivities(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));

		List<Activity> activities = resolveActivities(request.getActivityIds());

		session.setDate(request.getDate());
		session.setObservations(request.getObservations());
		session.setPhotoName(request.getPhotoName());
		syncSessionActivities(session, activities);

		return toResponse(session);
	}

	@Transactional
	public void delete(User user, Integer id) {
		if (!trainingSessionRepository.existsByIdAndUserId(id, user.getId())) {
			throw new ResourceNotFoundException("Sesión no encontrada");
		}
		trainingSessionRepository.deleteById(id);
	}

	private List<Activity> resolveActivities(List<Integer> activityIds) {
		Set<Integer> uniqueIds = new LinkedHashSet<>(activityIds);
		List<Activity> activities = activityRepository.findAllById(uniqueIds);
		if (activities.size() != uniqueIds.size()) {
			throw new ResourceNotFoundException("Una o más actividades no encontradas");
		}
		return activities;
	}

	private void setSessionActivities(TrainingSession session, List<Activity> activities) {
		for (Activity activity : activities) {
			TrainingSessionActivity link = new TrainingSessionActivity();
			link.setTrainingSession(session);
			link.setActivity(activity);
			session.getSessionActivities().add(link);
		}
	}

	private void syncSessionActivities(TrainingSession session, List<Activity> activities) {
		Set<Integer> targetIds = new LinkedHashSet<>();
		for (Activity activity : activities) {
			targetIds.add(activity.getId());
		}

		for (Iterator<TrainingSessionActivity> it = session.getSessionActivities().iterator(); it.hasNext();) {
			if (!targetIds.contains(it.next().getActivity().getId())) {
				it.remove();
			}
		}

		Set<Integer> existingIds = new LinkedHashSet<>();
		for (TrainingSessionActivity link : session.getSessionActivities()) {
			existingIds.add(link.getActivity().getId());
		}

		for (Activity activity : activities) {
			if (!existingIds.contains(activity.getId())) {
				TrainingSessionActivity link = new TrainingSessionActivity();
				link.setTrainingSession(session);
				link.setActivity(activity);
				session.getSessionActivities().add(link);
			}
		}
	}

	private TrainingSessionResponse toResponse(TrainingSession session) {
		List<ActivityResponse> activities = session.getSessionActivities().stream()
				.map(link -> toActivityResponse(link.getActivity()))
				.toList();

		return new TrainingSessionResponse(
				session.getId(),
				activities,
				session.getDate(),
				session.getObservations(),
				session.getPhotoName());
	}

	private ActivityResponse toActivityResponse(Activity activity) {
		return new ActivityResponse(
				activity.getId(),
				activity.getCategory().getId(),
				activity.getName(),
				activity.getDescription());
	}
}
