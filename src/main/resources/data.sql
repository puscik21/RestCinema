-- auditorium
insert into auditorium (id, number) values (1, 1);

-- movie
insert into movie (id, name) values (1, 'Avatar');

-- seat
insert into seat (id, auditorium_id, number, is_reserved) values (1, 1, 2, true);

-- spectacle
-- DATEADD trick to truncate time to seconds
insert into spectacle (id, movie_id, auditorium_id, date_time) values (1, 1, 1, DATEADD(second, DATEDIFF(second, '19700101', NOW()), '19700101'));

-- spectator
-- need to be added by API

-- reservation
insert into reservation (id, seat_id, spectacle_id, spectator_id) values (1, 1, 1, 1);
