package com.meet.scheduler.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {
    @NotEmpty
    private List<UUID> participantIds;

    @NotNull
    @Positive
    private Integer durationMinutes;

    @NotNull
    private Instant windowStart;

    @NotNull
    private Instant windowEnd;

    @Size(max = 120)
    private String title;
}

