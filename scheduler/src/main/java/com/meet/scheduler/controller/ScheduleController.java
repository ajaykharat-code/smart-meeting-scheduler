package com.meet.scheduler.controller;

import com.meet.scheduler.dto.ScheduleRequest;
import com.meet.scheduler.dto.ScheduleResponse;
import com.meet.scheduler.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponse> scheduleMeeting(
            @Valid @RequestBody ScheduleRequest request) {

        return scheduleService.findAvailableSlot(request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"No common slot found"));
    }
}
