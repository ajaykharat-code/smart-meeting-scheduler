package com.meet.scheduler.config;

import com.meet.scheduler.model.CalendarEvent;
import com.meet.scheduler.model.User;
import com.meet.scheduler.repository.CalendarEventRepository;
import com.meet.scheduler.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.Instant;
import java.util.List;

@Configuration
public class DataConfig {

    @Bean
    CommandLineRunner seed(UserRepository users, CalendarEventRepository events) {
        return args -> {
            if (users.count() > 0) return;

            var alice = users.save(User.builder().name("alice").build());
            var bob = users.save(User.builder().name("bob").build());
            var cara = users.save(User.builder().name("cara").build());

            // Seed events for today in UTC
            var day = LocalDate.now(ZoneOffset.UTC);

            Instant d0900 = day.atTime(9, 0).toInstant(ZoneOffset.UTC);
            Instant d1000 = day.atTime(10, 0).toInstant(ZoneOffset.UTC);
            Instant d1030 = day.atTime(10, 30).toInstant(ZoneOffset.UTC);
            Instant d1130 = day.atTime(11, 30).toInstant(ZoneOffset.UTC);
            Instant d1300 = day.atTime(13, 0).toInstant(ZoneOffset.UTC);
            Instant d1400 = day.atTime(14, 0).toInstant(ZoneOffset.UTC);
            Instant d1500 = day.atTime(15, 0).toInstant(ZoneOffset.UTC);
            Instant d1600 = day.atTime(16, 0).toInstant(ZoneOffset.UTC);

            events.saveAll(List.of(
                    CalendarEvent.builder().user(alice).title("Daily Standup")
                            .start(d0900).end(d1000).build(),
                    CalendarEvent.builder().user(alice).title("1:1 with manager")
                            .start(d1030).end(d1130).build(),
                    CalendarEvent.builder().user(bob).title("Code Review")
                            .start(d1500).end(d1600).build(),
                    CalendarEvent.builder().user(cara).title("Team Sync")
                            .start(d1300).end(d1400).build()
            ));
        };
    }
}
