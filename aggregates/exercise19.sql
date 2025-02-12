CREATE TABLE cd.bookings
(
    booking_id serial PRIMARY KEY,
    fac_id     INTEGER,
    mem_id     INTEGER,
    start_time TIMESTAMP,
    slots      INTEGER,
    FOREIGN KEY (fac_id) REFERENCES cd.facilities (fac_id),
    FOREIGN KEY (mem_id) REFERENCES cd.members (mem_id)
);

CREATE TABLE cd.facilities
(
    fac_id              INTEGER PRIMARY KEY,
    name                VARCHAR(200),
    member_cost         INTEGER,
    guest_cost          INTEGER,
    initial_outlay      INTEGER,
    monthly_maintenance INTEGER
);

INSERT INTO cd.bookings (fac_id, mem_id, start_time, slots)
VALUES (3, 1, '2012-07-03 11:00:00', 2),
       (4, 1, '2012-07-03 08:00:00', 2),
       (6, 0, '2012-07-03 18:00:00', 2),
       (7, 1, '2012-07-03 19:00:00', 2),
       (8, 1, '2012-07-03 10:00:00', 1),
       (8, 1, '2012-07-03 15:00:00', 1),
       (0, 2, '2012-07-04 09:00:00', 3),
       (0, 2, '2012-07-04 15:00:00', 3),
       (4, 3, '2012-07-04 13:30:00', 2),
       (4, 0, '2012-07-04 15:00:00', 2),
       (4, 0, '2012-07-04 17:30:00', 2),
       (6, 0, '2012-07-04 12:30:00', 2),
       (6, 0, '2012-07-04 14:00:00', 2),
       (6, 1, '2012-07-04 15:30:00', 2),
       (7, 2, '2012-07-04 14:00:00', 2),
       (8, 2, '2012-07-04 12:00:00', 1),
       (8, 3, '2012-07-04 18:00:00', 1),
       (1, 0, '2012-07-05 17:30:00', 3),
       (2, 1, '2012-07-05 09:30:00', 3),
       (3, 3, '2012-07-05 09:00:00', 2),
       (3, 1, '2012-07-05 19:00:00', 2),
       (4, 3, '2012-07-05 18:30:00', 2),
       (6, 0, '2012-07-05 13:00:00', 2),
       (6, 1, '2012-07-05 14:30:00', 2),
       (7, 2, '2012-07-05 18:30:00', 2),
       (8, 3, '2012-07-05 12:30:00', 1);

INSERT INTO cd.facilities
VALUES (0, 'Tennis Court 1', 5, 25, 10000, 200),
       (1, 'Tennis Court 2', 5, 25, 8000, 200),
       (2, 'Badminton Court', 0, 15.5, 4000, 50),
       (3, 'Table Tennis', 0, 5, 320, 10),
       (4, 'Massage Room 1', 35, 80, 4000, 3000),
       (5, 'Massage Room 2', 35, 80, 4000, 3000),
       (6, 'Squash Court', 3.5, 17.5, 5000, 80),
       (7, 'Snooker Table', 0, 5, 450, 15),
       (8, 'Pool Table', 0, 5, 400, 15);

SELECT name,
       rank() over (ORDER BY total_revenue DESC) AS rank
FROM (SELECT f.name,
             SUM(CASE WHEN b.mem_id = 0 THEN b.slots * f.guest_cost ELSE b.slots * f.member_cost END) AS total_revenue
      FROM cd.facilities f
               LEFT JOIN cd.bookings b ON f.fac_id = b.fac_id
      GROUP BY f.fac_id, f.name) AS facility_revenue
ORDER BY rank, name LIMIT 3;