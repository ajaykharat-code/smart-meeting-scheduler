package com.meet.scheduler.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

import java.util.UUID;

@Entity
@Table(name = "calendar_events",
        indexes = {
                @Index(name = "idx_event_user_start_end", columnList = "user_id,start_time,end_time")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "meeting_id")
    private UUID meetingId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_time", nullable = false)
    private Instant start;

    @Column(name = "end_time", nullable = false)
    private Instant end;
}
