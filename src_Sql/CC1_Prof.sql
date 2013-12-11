drop table movies cascade constraints;
drop table artists cascade constraints;
drop table certifications cascade constraints;
drop table companies cascade constraints;
drop table countries cascade constraints;
drop table genres cascade constraints;
drop table languages cascade constraints;
drop table logs cascade constraints;
drop table images cascade constraints;
drop table copies cascade constraints;
drop table production_companies cascade constraints;
drop table production_countries cascade constraints;
drop table spoken_lang cascade constraints;
drop table movie_direct cascade constraints;
drop table movie_genres cascade constraints;
drop table movie_play cascade constraints;
drop table projection_archives cascade constraints;
drop table rooms cascade constraints;
drop table time_slots cascade constraints;
drop table projection cascade constraints;

create table artists
  (
    id   number (6),
    name varchar2 (50 char),
    constraint artists$pk primary key (id),
    constraint artists$name$nn check (name is not null)
  );

create table certifications
  (
    id          number (4),
    name        varchar2 (7 char),
    description varchar2 (250 char),
    constraint cert$pk primary key (id),
    constraint cert$name$nn check (name is not null),
    constraint cert$name$ck check (name in ('G', 'PG', 'PG-13', 'R', 'NC-17','UNRATED'))
  );

create table companies
  (
    id   number (6),
    name varchar2 (70 char),
    constraint companies$pk primary key (id),
    constraint companies$name_nn check (name is not null)
  );

create table countries
  (
    code char (2 char),
    name varchar2 (40 char),
    constraint countries$pk primary key (code),
    constraint countries$name$nn check (name is not null),
    constraint countries$name$un unique (name)
  ) ;

create table genres
  (
    id   number (6),
    name varchar2 (20 char),
    constraint genres$pk primary key (id),
    constraint genres$name$nn check (name is not null),
    constraint genres$name$un unique (name)
  );

create table languages
  (
    code char (2 char),
    name varchar2 (20 char),
    constraint languages$pk primary key (code),
    constraint languages$name$nn check (name is not null),
    constraint languages$name$un unique (name)
  );

create table logs
  (
    id       number (12),
    date_log date default sysdate,
    errcode  integer default 0,
    message  varchar2 (4000 char),
    constraint logs$pk primary key (id)
  );

create table movies
  (
    id                 number (8),
    title              varchar2 (100 char),
    overview           varchar2 (1500 char),
    released_date      date,
    vote_average       number (3,1),
    vote_count         number (6),
    certification      number (4),
    --production_country char (2 char),
    runtime            number (3),
    nb_copies          number (3) default 0,
    constraint movies$pk primary key (id),
    constraint movies$title$nn check (title is not null),
    --constraint movies$countries$fk foreign key (production_country) references countries(code),
    constraint movies$cert$fk foreign key (certification) references certifications(id),
    constraint movies$runtime$ck check (runtime >=0),
    constraint movies$vote_count$ck check (vote_count >=0),
    constraint movies$vote_average$ck check (vote_average between 0 and 10)
  );

create table images
  (
    id number (10),
    image blob default empty_blob(),
    movie number (8),
    constraint images$pk primary key (id),
    constraint images$movie$nn check (movie is not null),
    constraint images$movies$fk foreign key (movie) references movies(id)
  );

create table copies
  (
    num_copy number (6),
    movie    number (8),
    constraint copies$pk primary key (num_copy, movie),
    constraint copies$movies$fk foreign key (movie) references movies(id)
  );

create table production_companies
  (
    company number (6),
    movie   number (8),
    constraint p_c$pk primary key (company, movie),
    constraint p_c$companies$fk foreign key (company) references companies(id),
    constraint p_c$movies$fk foreign key (movie) references movies (id)
  );

create table production_countries
  (
    code char (2 char),
    movies number(8),
    constraint prodcountries$pk primary key (code,movies),
    constraint prodcountries$code$fk foreign key (code) references countries(code),
    constraint prodcountries$movies$fk foreign key (movies) references movies(id)
  );

create table spoken_lang
  (
    language char (2 char),
    movie    number (8),
    constraint s_l$pk primary key (language, movie),
    constraint s_l$languages$fk foreign key (language) references languages(code),
    constraint s_l$movies$fk foreign key (movie) references movies(id)
  );

create table movie_direct
  (
    movie    number (8),
    director number (6),
    constraint m_d$pk primary key (movie, director),
    constraint m_d$movies$fk foreign key (movie) references movies(id),
    constraint m_d$artists$fk foreign key (director) references artists(id)
  );

create table movie_genres
  (
    genre number (6),
    movie number (8),
    constraint m_g$pk primary key (genre, movie),
    constraint m_g$genres$fk foreign key (genre) references genres(id),
    constraint m_g$movies$fk foreign key (movie) references movies(id)
  ) ;

create table movie_play
  (
    movie  number (8),
    artist number (6),
    constraint m_p$pk primary key (movie, artist),
    constraint m_p$movies$fk foreign key (movie) references movies(id),
    constraint m_p$artists$fk foreign key (artist) references artists(id)
  );

create table projection_archives
  (
    movie       number (8),
    total_days  number (6) default 0,
    total_seats number (5) default 0,
    constraint p_a$pk primary key (movie),
    constraint p_a$total_days$nn check (total_days is not null),
    constraint p_a$total_seats$nn check (total_seats is not null),
    constraint p_a$total_days$ck check (total_days >= 0),
    constraint p_a$total_seats$ck check (total_seats >= 0),
    constraint p_a$movies$fk foreign key (movie) references movies (id)
  ) ;

create table rooms
  (
    id number (1),
    seats number (3),
    constraint rooms$pk primary key (id),
    constraint rooms$seats$nn check (seats is not null),
    constraint rooms$seats$ck check (seats >= 0)
  );

create table time_slots
  (
    id number (1),
    slot varchar2 (7 char),
    constraint t_s$pk primary key (id)
  );

create table projection
  (
    movie           number (8),
    num_copy        number (6),
    time_slot       number (1),
    day             date,
    room            number (1),
    remaining_seats number (3),
    constraint proj$pk primary key (movie, num_copy, time_slot, day),
    constraint proj$room$nn check (room is not null),
    constraint proj$remaining_seats$nn check (remaining_seats is not null),
    constraint proj$time_slot$ck check (time_slot between 1 and 4),
    constraint proj$room$ck check (room between 1 and 3),
    constraint proj$remaining_seats$ck check (remaining_seats >= 0),
    constraint proj$movies$fk foreign key (movie) references movies (id),
    constraint proj$copies$fk foreign key (movie, num_copy) references copies (movie, num_copy),
    constraint proj$rooms$fk foreign key (room) references rooms (id),
    constraint proj$time_slots$fk foreign key (time_slot) references time_slots (id)
  );
