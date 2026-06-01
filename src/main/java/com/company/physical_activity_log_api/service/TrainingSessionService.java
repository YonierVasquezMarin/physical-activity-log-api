package com.company.physical_activity_log_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.TrainingSessionCreateRequest;
import com.company.physical_activity_log_api.dto.TrainingSessionResponse;
import com.company.physical_activity_log_api.dto.TrainingSessionUpdateRequest;
import com.company.physical_activity_log_api.exception.ResourceNotFoundException;
import com.company.physical_activity_log_api.model.Activity;
import com.company.physical_activity_log_api.model.TrainingSession;
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
		return trainingSessionRepository.findByUserIdOrderByDateDesc(user.getId()).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public TrainingSessionResponse getById(User user, Integer id) {
		TrainingSession session = trainingSessionRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
		return toResponse(session);
	}

	@Transactional
	public TrainingSessionResponse create(User user, TrainingSessionCreateRequest request) {
		Activity activity = activityRepository.findById(request.getActivityId())
				.orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

		TrainingSession session = new TrainingSession();
		session.setUser(user);
		session.setActivity(activity);
		session.setDate(request.getDate());
		session.setObservations(request.getObservations());

		TrainingSession saved = trainingSessionRepository.save(session);
		return toResponse(saved);
	}

	@Transactional
	public TrainingSessionResponse update(User user, Integer id, TrainingSessionUpdateRequest request) {
		TrainingSession session = trainingSessionRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));

		Activity activity = activityRepository.findById(request.getActivityId())
				.orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

		session.setActivity(activity);
		session.setDate(request.getDate());
		session.setObservations(request.getObservations());

		return toResponse(session);
	}

	@Transactional
	public void delete(User user, Integer id) {
		if (!trainingSessionRepository.existsByIdAndUserId(id, user.getId())) {
			throw new ResourceNotFoundException("Sesión no encontrada");
		}
		trainingSessionRepository.deleteById(id);
	}

	private TrainingSessionResponse toResponse(TrainingSession session) {
		return new TrainingSessionResponse(
				session.getId(),
				session.getActivity().getId(),
				session.getActivity().getName(),
				session.getActivity().getCategory().getId(),
				session.getActivity().getCategory().getName(),
				session.getDate(),
				session.getObservations());
	}
}

