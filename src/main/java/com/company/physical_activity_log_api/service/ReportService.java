package com.company.physical_activity_log_api.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.physical_activity_log_api.dto.report.CategoryBreakdownResponse;
import com.company.physical_activity_log_api.dto.report.GoalSummaryItemResponse;
import com.company.physical_activity_log_api.dto.report.MostFrequentCategoryResponse;
import com.company.physical_activity_log_api.dto.report.ReportActivitiesResponse;
import com.company.physical_activity_log_api.dto.report.ReportCategoriesResponse;
import com.company.physical_activity_log_api.dto.report.ReportConsistencyResponse;
import com.company.physical_activity_log_api.dto.report.ReportGoalsResponse;
import com.company.physical_activity_log_api.dto.report.ReportOverviewResponse;
import com.company.physical_activity_log_api.dto.report.ReportPeriodResponse;
import com.company.physical_activity_log_api.dto.report.SessionsByWeekResponse;
import com.company.physical_activity_log_api.dto.report.SummaryReportResponse;
import com.company.physical_activity_log_api.dto.report.TopActivityResponse;
import com.company.physical_activity_log_api.model.Activity;
import com.company.physical_activity_log_api.model.CategoryActivity;
import com.company.physical_activity_log_api.model.Goal;
import com.company.physical_activity_log_api.model.GoalProgress;
import com.company.physical_activity_log_api.model.TrainingSession;
import com.company.physical_activity_log_api.model.TrainingSessionActivity;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.GoalProgressRepository;
import com.company.physical_activity_log_api.repository.GoalRepository;
import com.company.physical_activity_log_api.repository.TrainingSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private static final ZoneOffset REPORT_ZONE = ZoneOffset.UTC;
	private static final int DEFAULT_RANGE_DAYS = 30;

	private final TrainingSessionRepository trainingSessionRepository;
	private final GoalRepository goalRepository;
	private final GoalProgressRepository goalProgressRepository;

	@Transactional(readOnly = true)
	public SummaryReportResponse getSummary(
			User user,
			OffsetDateTime from,
			OffsetDateTime to,
			int topActivitiesLimit) {
		OffsetDateTime resolvedTo = to != null ? to : OffsetDateTime.now(REPORT_ZONE);
		OffsetDateTime resolvedFrom = from != null ? from : resolvedTo.minusDays(DEFAULT_RANGE_DAYS);

		validatePeriod(resolvedFrom, resolvedTo);
		if (topActivitiesLimit < 1) {
			throw new IllegalArgumentException("topActivitiesLimit debe ser al menos 1");
		}

		List<TrainingSession> sessions = trainingSessionRepository.findByUserIdAndDateBetweenWithActivities(
				user.getId(), resolvedFrom, resolvedTo);
		List<OffsetDateTime> allSessionDates = trainingSessionRepository.findDatesByUserId(user.getId());
		List<Goal> goals = goalRepository.findByUserIdOrderByStartDateDesc(user.getId());
		List<GoalProgress> goalProgressEntries = goalProgressRepository.findByUserIdWithSessionOrderByDateAsc(
				user.getId());

		OffsetDateTime now = OffsetDateTime.now(REPORT_ZONE);

		return new SummaryReportResponse(
				buildPeriod(resolvedFrom, resolvedTo),
				buildOverview(sessions, resolvedFrom, resolvedTo),
				buildConsistency(sessions, allSessionDates, resolvedFrom, resolvedTo),
				buildCategories(sessions),
				buildActivities(sessions, topActivitiesLimit),
				buildGoals(user, goals, goalProgressEntries, resolvedFrom, resolvedTo, now));
	}

	private void validatePeriod(OffsetDateTime from, OffsetDateTime to) {
		if (from.isAfter(to)) {
			throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
		}
	}

	private ReportPeriodResponse buildPeriod(OffsetDateTime from, OffsetDateTime to) {
		return new ReportPeriodResponse(from, to, REPORT_ZONE.getId());
	}

	private ReportOverviewResponse buildOverview(
			List<TrainingSession> sessions,
			OffsetDateTime from,
			OffsetDateTime to) {
		int totalSessions = sessions.size();
		Set<LocalDate> activeDates = sessions.stream()
				.map(session -> toLocalDate(session.getDate()))
				.collect(Collectors.toCollection(TreeSet::new));

		long weeksInPeriod = Math.max(1, ChronoUnit.WEEKS.between(toLocalDate(from), toLocalDate(to)) + 1);
		double averageSessionsPerWeek = roundOneDecimal((double) totalSessions / weeksInPeriod);

		Map<Integer, CategoryCounter> categoryStats = countSessionsByCategory(sessions);
		MostFrequentCategoryResponse mostFrequentCategory = categoryStats.values().stream()
				.max(Comparator.comparingInt(CategoryCounter::getSessionCount))
				.map(counter -> new MostFrequentCategoryResponse(
						counter.getId(),
						counter.getName(),
						counter.getSessionCount()))
				.orElse(null);

		return new ReportOverviewResponse(
				totalSessions,
				activeDates.size(),
				averageSessionsPerWeek,
				mostFrequentCategory);
	}

	private ReportConsistencyResponse buildConsistency(
			List<TrainingSession> sessions,
			List<OffsetDateTime> allSessionDates,
			OffsetDateTime from,
			OffsetDateTime to) {
		Set<LocalDate> periodActiveDates = sessions.stream()
				.map(session -> toLocalDate(session.getDate()))
				.collect(Collectors.toCollection(TreeSet::new));

		Set<LocalDate> allActiveDates = allSessionDates.stream()
				.map(this::toLocalDate)
				.collect(Collectors.toCollection(TreeSet::new));

		LocalDate periodStart = toLocalDate(from);
		LocalDate periodEnd = toLocalDate(to);
		int periodDays = (int) ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;

		Map<String, Integer> sessionsByWeek = new LinkedHashMap<>();
		for (TrainingSession session : sessions) {
			String week = toIsoWeek(toLocalDate(session.getDate()));
			sessionsByWeek.merge(week, 1, Integer::sum);
		}

		List<SessionsByWeekResponse> sessionsByWeekList = sessionsByWeek.entrySet().stream()
				.map(entry -> new SessionsByWeekResponse(entry.getKey(), entry.getValue()))
				.toList();

		return new ReportConsistencyResponse(
				calculateCurrentStreak(allActiveDates),
				calculateLongestStreak(periodActiveDates),
				sessionsByWeekList,
				Math.max(0, periodDays - periodActiveDates.size()));
	}

	private ReportCategoriesResponse buildCategories(List<TrainingSession> sessions) {
		Map<Integer, CategoryCounter> categoryStats = countSessionsByCategory(sessions);
		int totalSessions = sessions.size();

		List<CategoryBreakdownResponse> breakdown = categoryStats.values().stream()
				.sorted(Comparator.comparingInt(CategoryCounter::getSessionCount).reversed())
				.map(counter -> new CategoryBreakdownResponse(
						counter.getId(),
						counter.getName(),
						counter.getSessionCount(),
						totalSessions == 0 ? 0.0 : roundOneDecimal(counter.getSessionCount() * 100.0 / totalSessions)))
				.toList();

		return new ReportCategoriesResponse(totalSessions, breakdown);
	}

	private ReportActivitiesResponse buildActivities(List<TrainingSession> sessions, int topActivitiesLimit) {
		Map<Integer, ActivityCounter> activityStats = new HashMap<>();

		for (TrainingSession session : sessions) {
			for (TrainingSessionActivity link : session.getSessionActivities()) {
				Activity activity = link.getActivity();
				activityStats
						.computeIfAbsent(activity.getId(), id -> new ActivityCounter(
								activity.getId(),
								activity.getName(),
								activity.getCategory().getName()))
						.increment();
			}
		}

		List<TopActivityResponse> top = activityStats.values().stream()
				.sorted(Comparator.comparingInt(ActivityCounter::getOccurrences).reversed()
						.thenComparing(ActivityCounter::getActivityName))
				.limit(topActivitiesLimit)
				.map(counter -> new TopActivityResponse(
						counter.getActivityId(),
						counter.getActivityName(),
						counter.getCategoryName(),
						counter.getOccurrences()))
				.toList();

		return new ReportActivitiesResponse(top, activityStats.size());
	}

	private ReportGoalsResponse buildGoals(
			User user,
			List<Goal> goals,
			List<GoalProgress> goalProgressEntries,
			OffsetDateTime from,
			OffsetDateTime to,
			OffsetDateTime now) {
		int activeCount = 0;
		int expiredCount = 0;

		for (Goal goal : goals) {
			if (isActiveGoal(goal, now)) {
				activeCount++;
			} else if (goal.getEndDate().isBefore(now)) {
				expiredCount++;
			}
		}

		long withoutProgressCount = goalProgressRepository.countGoalsWithoutProgressByUserId(user.getId());

		List<GoalSummaryItemResponse> items = goals.stream()
				.filter(goal -> isActiveGoal(goal, now))
				.map(goal -> toGoalSummaryItem(goal, goalProgressEntries, from, to))
				.toList();

		return new ReportGoalsResponse(
				activeCount,
				expiredCount,
				(int) withoutProgressCount,
				items);
	}

	private GoalSummaryItemResponse toGoalSummaryItem(
			Goal goal,
			List<GoalProgress> goalProgressEntries,
			OffsetDateTime from,
			OffsetDateTime to) {
		List<GoalProgress> progressInPeriod = goalProgressEntries.stream()
				.filter(entry -> entry.getGoal().getId().equals(goal.getId()))
				.filter(entry -> isWithinPeriod(entry.getSession().getDate(), from, to))
				.toList();

		Integer firstLevel = progressInPeriod.isEmpty() ? null : progressInPeriod.get(0).getLevel();
		Integer latestLevel = progressInPeriod.isEmpty()
				? null
				: progressInPeriod.get(progressInPeriod.size() - 1).getLevel();
		Integer levelChange = (firstLevel == null || latestLevel == null) ? null : latestLevel - firstLevel;

		return new GoalSummaryItemResponse(
				goal.getId(),
				goal.getTitle(),
				"ACTIVE",
				progressInPeriod.size(),
				latestLevel,
				firstLevel,
				levelChange);
	}

	private Map<Integer, CategoryCounter> countSessionsByCategory(List<TrainingSession> sessions) {
		Map<Integer, CategoryCounter> categoryStats = new HashMap<>();

		for (TrainingSession session : sessions) {
			Set<Integer> categoriesInSession = new HashSet<>();
			for (TrainingSessionActivity link : session.getSessionActivities()) {
				CategoryActivity category = link.getActivity().getCategory();
				if (categoriesInSession.add(category.getId())) {
					categoryStats
							.computeIfAbsent(category.getId(), id -> new CategoryCounter(category.getId(), category.getName()))
							.incrementSessionCount();
				}
			}
		}

		return categoryStats;
	}

	private int calculateCurrentStreak(Set<LocalDate> activeDates) {
		if (activeDates.isEmpty()) {
			return 0;
		}

		LocalDate today = LocalDate.now(REPORT_ZONE);
		LocalDate cursor = activeDates.contains(today) ? today : today.minusDays(1);

		if (!activeDates.contains(cursor)) {
			return 0;
		}

		int streak = 0;
		while (activeDates.contains(cursor)) {
			streak++;
			cursor = cursor.minusDays(1);
		}

		return streak;
	}

	private int calculateLongestStreak(Set<LocalDate> activeDates) {
		if (activeDates.isEmpty()) {
			return 0;
		}

		List<LocalDate> sortedDates = new ArrayList<>(activeDates);
		int longest = 1;
		int current = 1;

		for (int i = 1; i < sortedDates.size(); i++) {
			if (ChronoUnit.DAYS.between(sortedDates.get(i - 1), sortedDates.get(i)) == 1) {
				current++;
			} else {
				current = 1;
			}
			longest = Math.max(longest, current);
		}

		return longest;
	}

	private boolean isActiveGoal(Goal goal, OffsetDateTime now) {
		return !goal.getStartDate().isAfter(now) && !goal.getEndDate().isBefore(now);
	}

	private boolean isWithinPeriod(OffsetDateTime date, OffsetDateTime from, OffsetDateTime to) {
		return !date.isBefore(from) && !date.isAfter(to);
	}

	private LocalDate toLocalDate(OffsetDateTime dateTime) {
		return dateTime.atZoneSameInstant(REPORT_ZONE).toLocalDate();
	}

	private String toIsoWeek(LocalDate date) {
		WeekFields weekFields = WeekFields.ISO;
		int week = date.get(weekFields.weekOfWeekBasedYear());
		int year = date.get(weekFields.weekBasedYear());
		return String.format(Locale.ROOT, "%d-W%02d", year, week);
	}

	private double roundOneDecimal(double value) {
		return Math.round(value * 10.0) / 10.0;
	}

	private static final class CategoryCounter {

		private final Integer id;
		private final String name;
		private int sessionCount;

		private CategoryCounter(Integer id, String name) {
			this.id = id;
			this.name = name;
		}

		private Integer getId() {
			return id;
		}

		private String getName() {
			return name;
		}

		private int getSessionCount() {
			return sessionCount;
		}

		private void incrementSessionCount() {
			sessionCount++;
		}
	}

	private static final class ActivityCounter {

		private final Integer activityId;
		private final String activityName;
		private final String categoryName;
		private int occurrences;

		private ActivityCounter(Integer activityId, String activityName, String categoryName) {
			this.activityId = activityId;
			this.activityName = activityName;
			this.categoryName = categoryName;
		}

		private Integer getActivityId() {
			return activityId;
		}

		private String getActivityName() {
			return activityName;
		}

		private String getCategoryName() {
			return categoryName;
		}

		private int getOccurrences() {
			return occurrences;
		}

		private void increment() {
			occurrences++;
		}
	}
}
