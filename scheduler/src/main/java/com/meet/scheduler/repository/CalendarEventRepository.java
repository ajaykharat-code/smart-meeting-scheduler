package com.meet.scheduler.repository;

import com.meet.scheduler.model.CalendarEvent;
import com.meet.scheduler.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, UUID> {
    /**
     * Finds all events for a single user that overlap with a given time window
     * return: All overlapping events for one user
     */
    @Query("""
        select e from CalendarEvent e
        where e.user = :user
        and e.end > :start
        and e.start < :end
        order by e.start asc
    """)
    List<CalendarEvent> findOverlapping(@Param("user") User user,
                                        @Param("start") Instant start,
                                        @Param("end") Instant end);

    /**
     * Finds all events for multiple users that overlap the given time window.
     * return: All overlapping events for multiple users
     */
    @Query("""
        select e from CalendarEvent e
        where e.user.id in :userIds
        and e.end > :start
        and e.start < :end
        order by e.user.id, e.start asc
    """)
    List<CalendarEvent> findAllForUsersInWindow(@Param("userIds") List<UUID> userIds,
                                                @Param("start") Instant start,
                                                @Param("end") Instant end);
}
