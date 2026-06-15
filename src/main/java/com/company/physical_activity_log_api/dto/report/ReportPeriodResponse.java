package com.company.physical_activity_log_api.dto.report;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportPeriodResponse {

	private OffsetDateTime from;
	private OffsetDateTime to;
	private String timezone;

}
