set serveroutput on

--Modifying the certification table to include UNRATED
ALTER TABLE CERTIFICATIONS
MODIFY NAME varchar2(7 char);

ALTER TABLE MOVIES
MODIFY OVERVIEW varchar2( 2400 char);


ALTER TABLE CERTIFICATIONS
DROP CONSTRAINT cert$name$ck;
ALTER TABLE CERTIFICATIONS
ADD CONSTRAINT cert$name$ck check (NAME in ('G', 'PG', 'PG-13', 'R', 'NC-17','UNRATED'));

CREATE TYPE LANGUAGE_T AS OBJECT (
	code varchar2(100 char),
	name_n varchar2(100 char)
);
/

CREATE TYPE COUNTRY_T AS OBJECT (
  code varchar2(100 char),
  name_n varchar2(100 char)
);
/

CREATE TYPE array_string IS table of varchar2(100 char);
/

CREATE TYPE countries_t IS table of COUNTRY_T;
/

CREATE TYPE languages_t IS table of LANGUAGE_T;
/

CREATE TYPE MOVIE_T AS OBJECT (
	title varchar2(100 char), 
	overview varchar2(100 char),
	released_date DATE,
	vote_average FLOAT,
	vote_count INTEGER,
  certification varchar2(100 char),
	language_n languages_t,
  production_country countries_t,
  runtime INTEGER,
  nb_copies INTEGER,
  artists array_string,
  genre array_string,
  companies array_string,
  directors array_string,
  poster BLOB
	);
/

CREATE TYPE MOVIES_T IS table of MOVIE_T;
/

CREATE TYPE integ_tab IS table of INTEGER;
/
--Sequencers
CREATE SEQUENCE movie_seq 
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE cert_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE log_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE art_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE comp_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE genre_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE img_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE lang_seq
START WITH 1
INCREMENT BY 1;

CREATE DATABASE LINK linkCB CONNECT TO BD_CC1 IDENTIFIED BY dummy USING 'xe';