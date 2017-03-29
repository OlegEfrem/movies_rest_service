create table movies (
  imdb_id  varchar not null,
  screen_id varchar not null,
  movie_title varchar, 
  available_seats int not null,
  reserved_seats int default 0,
  primary key(imdb_id, screen_id)
);