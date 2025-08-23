package com.meet.scheduler.service.impl;

import com.meet.scheduler.dto.CalendarEventDTO;
import com.meet.scheduler.dto.ScheduleRequest;
import com.meet.scheduler.dto.ScheduleResponse;
import com.meet.scheduler.model.CalendarEvent;
import com.meet.scheduler.model.User;
import com.meet.scheduler.repository.CalendarEventRepository;
import com.meet.scheduler.repository.UserRepository;
import com.meet.scheduler.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final UserRepository userRepo;
    private final CalendarEventRepository eventRepo;

    @Value("${app.scheduling.buffer-minutes:15}")
    private int bufferMinutes;

    @Value("${app.scheduling.work-hours.start:09:00}")
    private String workStart;

    @Value("${app.scheduling.work-hours.end:17:00}")
    private String workEnd;

    public ScheduleServiceImpl(UserRepository userRepo, CalendarEventRepository eventRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public List<CalendarEventDTO> getUserEvents(UUID userId, Instant start, Instant end) {
        if (!end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`end` must be after `start`");
        }
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));


        return eventRepo.findOverlapping(user, start, end).stream()
                .map(e -> new CalendarEventDTO(e.getId(), e.getMeetingId(), e.getTitle(), e.getStart(), e.getEnd()))
                .toList();
    }

    @Override
    public Optional<ScheduleResponse> findAvailableSlot(ScheduleRequest request) {
        List<CalendarEvent> events = eventRepo.findAllByUserIdsAndRange(
                request.getParticipantIds(),
                request.getWindowStart(),
                request.getWindowEnd()
        );

        // Convert to busy intervals
        List<Interval> busyIntervals = events.stream()
                .map(e -> new Interval(e.getStart(), e.getEnd()))
                .sorted(Comparator.comparing(i -> i.start))
                .collect(Collectors.toList());

        // Merge overlaps
        List<Interval> merged = mergeIntervals(busyIntervals);

        // Find free slot
        Optional<ScheduleResponse> ans = findFirstFreeSlot(request.getTitle(), merged,
                request.getWindowStart(),
                request.getWindowEnd(),
                request.getDurationMinutes()
        );
        ScheduleResponse res = null;
        if (ans.isPresent()) {
            res = ans.get();
            res.setTitle("New Meet Schedule");
            res.setMeetingId(UUID.randomUUID());
            res.setParticipantIds(request.getParticipantIds());
        }
        //save to db
        Instant meetingStart = res.getStart();
        Instant meetingEnd = res.getEnd();

        for (UUID participantId : request.getParticipantIds()) {
            User user = userRepo.findById(participantId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + participantId));

            CalendarEvent event = new CalendarEvent();
            event.setMeetingId(res.getMeetingId());
            event.setTitle(res.getTitle());
            event.setStart(meetingStart);
            event.setEnd(meetingEnd);
            event.setUser(user);

            eventRepo.save(event);
        }
        return Optional.of(res);
    }

    // --- Merge Overlapping Intervals ---
    private List<Interval> mergeIntervals(List<Interval> intervals) {
        if (intervals.isEmpty()) return Collections.emptyList();

        List<Interval> merged = new ArrayList<>();
        Interval prev = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            Interval curr = intervals.get(i);
            if (!curr.start.isAfter(prev.end)) {
                prev.end = prev.end.isAfter(curr.end) ? prev.end : curr.end;
            } else {
                merged.add(prev);
                prev = curr;
            }
        }
        merged.add(prev);
        return merged;
    }

    // --- Find First Free Slot ---
    private Optional<ScheduleResponse> findFirstFreeSlot(String title,
            List<Interval> busyIntervals,
            Instant rangeStart,
            Instant rangeEnd,
            int durationMinutes) {

        Duration meetingDuration = Duration.ofMinutes(durationMinutes);

        Instant current = rangeStart;

        for (Interval busy : busyIntervals) {
            if (Duration.between(current, busy.start).compareTo(meetingDuration) >= 0) {
                return Optional.of(ScheduleResponse.builder()
                        .start(current)
                        .end(current.plus(meetingDuration))
                        .build());
            }
            if (busy.end.isAfter(current)) {
                current = busy.end;
            }
        }

        // Check after last busy slot
        if (Duration.between(current, rangeEnd).compareTo(meetingDuration) >= 0) {
            return Optional.of(ScheduleResponse.builder()
                    .start(current)
                    .end(current.plus(meetingDuration))
                    .build());
        }

        return Optional.empty();
    }

    // --- Helper class ---
    private static class Interval {
        Instant start;
        Instant end;
        Interval(Instant start, Instant end) {
            this.start = start;
            this.end = end;
        }
    }
}