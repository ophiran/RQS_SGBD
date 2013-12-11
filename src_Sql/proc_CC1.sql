create or replace procedure addCountry(code_v IN COUNTRIES.CODE%type,name_v IN COUNTRIES.NAME%type,movie_id IN MOVIES.ID%type)
AS
code_v2 COUNTRIES.CODE%type;
coutry_cnt INTEGER;
BEGIN

  IF name_v IS NOT NULL THEN
    SELECT COUNT(CODE) into coutry_cnt
    FROM COUNTRIES WHERE CODE = code_v;

    IF coutry_cnt = 0 THEN
      INSERT INTO COUNTRIES (CODE,NAME) VALUES (code_v,name_v);
      COMMIT;
    ELSE
      SELECT CODE into code_v2
      FROM COUNTRIES WHERE CODE = code_v;
    END IF;

    SELECT CODE into code_v2
    FROM PRODUCTION_COUNTRIES
    WHERE CODE = code_v
    AND MOVIES = movie_id;
  END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO PRODUCTION_COUNTRIES (CODE,MOVIES) VALUES (code_v,movie_id);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addCountry;
/

create or replace procedure addCertification(cert_v IN CERTIFICATIONS.NAME%type,desc_v IN CERTIFICATIONS.DESCRIPTION%type)
AS
cert_v2 CERTIFICATIONS.NAME%type;
BEGIN
  SELECT NAME into cert_v2
  FROM CERTIFICATIONS WHERE cert_v = NAME;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO CERTIFICATIONS (ID,NAME,DESCRIPTION) VALUES (cert_seq.nextval,cert_v,desc_v);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addCertification;
/

create or replace procedure addLogs(errcode_v IN LOGS.ERRCODE%type,message_v IN LOGS.MESSAGE%type)
AS
date_v DATE;
BEGIN

  SELECT SYSDATE into date_v FROM DUAL;
  INSERT INTO LOGS (ID,DATE_LOG,ERRCODE,MESSAGE) values
  (log_seq.nextval,date_v,errcode_v,message_v);
  COMMIT;
END addLogs;
/

create or replace procedure addArtist(name_v IN ARTISTS.NAME%type,movie_v IN MOVIES.ID%type)
AS
artist_cnt INTEGER;
artist_id ARTISTS.ID%type;
BEGIN

  IF name_v IS NOT NULL THEN
    SELECT COUNT(NAME) into artist_cnt
    FROM ARTISTS
    WHERE name_v = NAME;
    
    IF artist_cnt > 0 THEN
    SELECT ID into artist_id
    FROM ARTISTS
    WHERE name_v = NAME;
    ELSE
    artist_id := art_seq.nextval;
    INSERT INTO ARTISTS (ID,NAME) values (artist_id,name_v);
    COMMIT;
    END IF;

    SELECT ARTIST into artist_cnt
    FROM MOVIE_PLAY
    WHERE ARTIST = artist_id
    AND MOVIE = movie_v;
  END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO MOVIE_PLAY (MOVIE,ARTIST) values (movie_v,artist_id);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addArtist;
/

create or replace procedure addDirector(name_v IN ARTISTS.NAME%type,movie_v IN MOVIES.ID%type)
AS
director_cnt INTEGER;
director_id ARTISTS.ID%type;
BEGIN

  IF name_v IS NOT NULL THEN
    SELECT COUNT(NAME) into director_cnt
    FROM ARTISTS
    WHERE name_v = NAME;
    
    IF director_cnt = 0 THEN
    director_id := art_seq.nextval;
    INSERT INTO ARTISTS (ID,NAME) values (director_id,name_v);
    COMMIT;
    ELSE
    SELECT ID into director_id
    FROM ARTISTS
    WHERE name_v = NAME;
    END IF;
    
    SELECT DIRECTOR into director_cnt
    FROM MOVIE_DIRECT
    WHERE DIRECTOR = director_id
    AND MOVIE = movie_v;
  END IF;
  
EXCEPTION
  WHEN NO_DATA_FOUND THEN
  INSERT INTO MOVIE_DIRECT (MOVIE,DIRECTOR) values (movie_v,director_id);
  COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addDirector;
/

create or replace procedure addCompany(name_v IN COMPANIES.NAME%type,movie_v IN MOVIES.ID%type)
AS
comp_cnt INTEGER;
comp_id COMPANIES.ID%type;
BEGIN


  IF name_v IS NOT NULL THEN
    SELECT COUNT(NAME) into comp_cnt
    FROM COMPANIES
    WHERE name_v = NAME;
    
    IF comp_cnt = 0 THEN
    comp_id := comp_seq.nextval;
    INSERT INTO COMPANIES (ID,NAME) values (comp_id,name_v);
    COMMIT;
    ELSE
    SELECT ID into comp_id
    FROM COMPANIES
    WHERE name_v = NAME;
    END IF;
    
    SELECT COMPANY into comp_cnt
    FROM PRODUCTION_COMPANIES
    WHERE movie_v = MOVIE
    AND comp_id = COMPANY;
  END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO PRODUCTION_COMPANIES (COMPANY,MOVIE) values (comp_id,movie_v);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addCompany;
/

create or replace procedure addGenre(name_v IN GENRES.NAME%type,movie_v IN MOVIES.ID%type)
AS
genre_cnt INTEGER;
genre_id GENRES.ID%type;
BEGIN
  

  IF name_v IS NOT NULL THEN
    SELECT COUNT(NAME) into genre_cnt
    FROM GENRES
    WHERE name_v = NAME;
    
    IF genre_cnt = 0 THEN
    genre_id := genre_seq.nextval;
    INSERT INTO GENRES (ID,NAME) values (genre_id,name_v);
    COMMIT;
    ELSE
    SELECT ID into genre_id
    FROM GENRES
    WHERE name_v = NAME;
    END IF;
    
    SELECT GENRE into genre_cnt
    FROM MOVIE_GENRES
    WHERE movie_v = MOVIE
    AND genre_id = GENRE;
  END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO MOVIE_GENRES (GENRE,MOVIE) values (genre_id,movie_v);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addGenre;
/

create or replace procedure addLanguage(name_v IN LANGUAGES.NAME%type,code_v IN LANGUAGES.CODE%type,movie_v IN MOVIES.ID%type)
AS
lang_cnt INTEGER;
lang_ver SPOKEN_LANG.LANGUAGE%type;
BEGIN
  
  IF name_v IS NOT NULL THEN
    SELECT COUNT(CODE) into lang_cnt
    FROM LANGUAGES
    WHERE code_v = CODE;
    
    IF lang_cnt = 0 THEN
    INSERT INTO LANGUAGES (CODE,NAME) values (code_v,name_v);
    COMMIT;
    END IF;
    
    SELECT LANGUAGE into lang_ver
    FROM SPOKEN_LANG
    WHERE movie_v = MOVIE
    AND code_v = LANGUAGE;
  END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO SPOKEN_LANG (LANGUAGE,MOVIE) values (code_v,movie_v);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addLanguage;
/

create or replace procedure addMovie(movie_title IN MOVIES.title%type, movie_overview IN MOVIES.overview%type,
    movie_date IN MOVIES.released_date%type, movie_vAvg IN MOVIES.vote_average%type, 
    movie_vCount IN MOVIES.vote_count%type, movie_cert IN CERTIFICATIONS.NAME%type, 
    movie_run IN MOVIES.runtime%type) 
AS
cert CERTIFICATIONS.NAME%type;
cert_id CERTIFICATIONS.ID%type;
movie_id MOVIES.ID%type;
movie_cnt NUMBER(6);
  
BEGIN
  addCertification(movie_cert,'a description');

  SELECT ID into cert_id FROM CERTIFICATIONS WHERE movie_cert = NAME;

  --Movie insertion
  SELECT COUNT(ID) into movie_cnt
  FROM MOVIES
  WHERE TITLE = movie_title
  AND RELEASED_DATE = movie_date;

  IF movie_cnt = 0 THEN
    movie_id := movie_seq.nextval;
    INSERT INTO MOVIES 
    (ID,TITLE,OVERVIEW,
    RELEASED_DATE,VOTE_AVERAGE,VOTE_COUNT,CERTIFICATION,RUNTIME,NB_COPIES) 
    values 
    (movie_id,movie_title,movie_overview,
    movie_date,movie_vAvg,movie_vCount,cert_id
    ,movie_run,0);
    COMMIT;
  END IF;
    
EXCEPTION
  WHEN NO_DATA_FOUND THEN
  NULL;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
end addMovie;
/


CREATE OR REPLACE FUNCTION movieExist(title_v IN MOVIES.title%type, date_v IN MOVIES.released_date%type)
RETURN BOOLEAN

AS
movie_v MOVIES%rowtype;
BEGIN
  SELECT * INTO movie_v FROM MOVIES WHERE title = title_v AND released_date = date_v;
  RETURN TRUE;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RETURN FALSE;

END movieExist;
/

CREATE OR REPLACE FUNCTION getMovieId(title_v IN MOVIES.title%type, date_v IN MOVIES.released_date%type) RETURN MOVIES.ID%type
AS
movie_id MOVIES.ID%type;
BEGIN
  SELECT ID into movie_id
  FROM MOVIES
  WHERE title = title_v 
  AND released_date = date_v;

  RETURN movie_id;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RETURN NULL;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);

END getMovieId;
/
