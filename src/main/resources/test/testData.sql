-- clear tables before inserting test data
delete from reservation;
delete from seat;
delete from spectacle;
delete from auditorium;
delete from movie;
delete from spectator;

-- auditorium
insert into auditorium (id, number) values (1, 1);

-- seat
insert into seat (id, auditorium_id, number, is_reserved) values (1, 1, 2, true);

-- movie
insert into movie (id, name) values (1, 'Avatar');

-- spectacle
-- DATEADD trick to truncate time to seconds
insert into spectacle (id, movie_id, auditorium_id, date_time) values (1, 1, 1, DATEADD(second, DATEDIFF(second, '19700101', NOW()), '19700101'));

-- spectator
insert into spectator (id, name, email, phone_number, password, roles) values (1, 'Jan Kowalski', 'jankowalski@mail.com', '123456789', 'admin', '');
