-- Users
INSERT INTO users (id, name) VALUES (RANDOM_UUID(), 'alice');
INSERT INTO users (id, name) VALUES (RANDOM_UUID(), 'bob');
INSERT INTO users (id, name) VALUES (RANDOM_UUID(), 'cara');


-- Calendar Events
INSERT INTO calendar_events (id, meeting_id, user_id, title, start_time, end_time)
VALUES (RANDOM_UUID(), NULL,
        (SELECT id FROM users WHERE name = 'alice'),
        'Daily Standup',
        TIMESTAMP '2025-08-22 09:00:00',
        TIMESTAMP '2025-08-22 10:00:00');


INSERT INTO calendar_events (id, meeting_id, user_id, title, start_time, end_time)
VALUES (RANDOM_UUID(), NULL,
        (SELECT id FROM users WHERE name = 'alice'),
        '1:1 with manager',
        TIMESTAMP '2025-08-22 10:30:00',
        TIMESTAMP '2025-08-22 11:30:00');


INSERT INTO calendar_events (id, meeting_id, user_id, title, start_time, end_time)
VALUES (RANDOM_UUID(), NULL,
        (SELECT id FROM users WHERE name = 'bob'),
        'Code Review',
        TIMESTAMP '2025-08-22 15:00:00',
        TIMESTAMP '2025-08-22 16:00:00');


INSERT INTO calendar_events (id, meeting_id, user_id, title, start_time, end_time)
VALUES (RANDOM_UUID(), NULL,
        (SELECT id FROM users WHERE name = 'cara'),
        'Team Sync',
        TIMESTAMP '2025-08-22 13:00:00',
        TIMESTAMP '2025-08-22 14:00:00');