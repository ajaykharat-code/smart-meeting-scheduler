package com.meet.scheduler.service;

import com.meet.scheduler.dto.CalendarEventDTO;
import com.meet.scheduler.dto.ScheduleRequest;
import com.meet.scheduler.dto.ScheduleResponse;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleService {
    List<CalendarEventDTO> getUserEvents(UUID userId, Instant start, Instant end);
    Optional<ScheduleResponse> findAvailableSlot(ScheduleRequest request);
}
