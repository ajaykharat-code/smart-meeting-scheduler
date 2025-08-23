package com.meet.scheduler.controller;

import com.meet.scheduler.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.meet.scheduler.dto.CalendarEventDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/calendar")
public class CalendarController {

    private final ScheduleService scheduleService;

    public CalendarController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<CalendarEventDTO>> getEvents(@PathVariable UUID userId,
                                                            @RequestParam Instant start,
                                                            @RequestParam Instant end) {
        if (!end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`end` must be after `start`");
        }
        return ResponseEntity.ok(scheduleService.getUserEvents(userId, start, end));
    }
}
