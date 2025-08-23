package com.meet.scheduler.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Data
@NoArgsConstructor
@Builder
public class CalendarEventDTO {
    private UUID id;
    private UUID meetingId;
    private String title;
    private Instant start;
    private Instant end;

    public CalendarEventDTO(UUID id, UUID meetingId, String title, Instant start, Instant end) {
        this.id = id;
        this.meetingId = meetingId;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public UUID getId(UUID id) {
        return this.id;
    }

    public UUID getMeetingId() {
        return meetingId;
    }

    public String getTitle() {
        return title;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

}
