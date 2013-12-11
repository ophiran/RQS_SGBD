--Procedure
create or replace procedure addMovies(array MOVIES_T) is
cert CERTIFICATIONS.NAME%type;
cert_id CERTIFICATIONS.ID%type;
movie_id MOVIES.ID%type;
movie_cnt NUMBER(6);
movie_max NUMBER(6);
begin
  for i in 1..array.count loop
  
    addCertification(array(i).certification,'a description');
    
    SELECT ID into cert_id FROM CERTIFICATIONS WHERE array(i).certification = NAME;
    
    --Movie insertion
    
    SELECT COUNT(ID) into movie_cnt
    FROM MOVIES
    WHERE TITLE = array(i).title
    AND RELEASED_DATE = array(i).released_date;
    
    IF movie_cnt = 0 THEN
      movie_id := movie_seq.nextval;

      INSERT INTO MOVIES 
      (ID,TITLE,OVERVIEW,
      RELEASED_DATE,VOTE_AVERAGE,VOTE_COUNT,CERTIFICATION,RUNTIME,NB_COPIES,COPIE_MAX) 
      values 
      (movie_id,array(i).title,array(i).overview,
      array(i).released_date,array(i).vote_average,array(i).vote_count,cert_id
      ,array(i).runtime,0,0);
      COMMIT;

      --Adding Country
      for j in 1..array(i).production_country.count loop
        addCountry(array(i).production_country(j).code,array(i).production_country(j).name_n);
        INSERT INTO PRODUCTION_COUNTRIES (CODE,MOVIES) values (array(i).production_country(j).code,movie_id);
        COMMIT;
      end loop;
      
      --Adding Actors
      for k in 1..array(i).artists.count loop
        addArtist(array(i).artists(k),movie_id);
      end loop;
      
      --Adding Directors
      for l in 1..array(i).directors.count loop
        addDirector(array(i).directors(l),movie_id);
      end loop;
      
      --Adding Production companies
      for m in 1..array(i).companies.count loop
        addCompany(array(i).companies(m),movie_id);
      end loop;

      --Adding Genders
      for n in 1..array(i).genre.count loop
        addGenre(array(i).genre(n),movie_id);
      end loop;

      --Adding Movie copies
      for o in 1..array(i).nb_copies loop
        INSERT INTO COPIES (NUM_COPY,MOVIE) values (o,movie_id);
      end loop;
      COMMIT;

      --Adding Languages
      for p in 1..array(i).language_n.count loop
        addLanguage(array(i).language_n(p).name_n,array(i).language_n(p).code,movie_id);
      end loop;

      IF array(i).poster IS NOT NULL THEN
      INSERT INTO IMAGES (ID,IMAGE,MOVIE) values (img_seq.nextval,array(i).poster,movie_id);
      COMMIT;
      END IF;
      
      --Provoke an update for trigger
      UPDATE MOVIES
      SET NB_COPIES = array(i).nb_copies, COPIE_MAX = array(i).nb_copies
      WHERE ID = movie_id;

    ELSE
      SELECT NB_COPIES into movie_cnt
      FROM MOVIES
      WHERE TITLE = array(i).title
      AND RELEASED_DATE = array(i).released_date;
      
      SELECT COPIE_MAX into movie_max
      FROM MOVIES
      WHERE TITLE = array(i).title
      AND RELEASED_DATE = array(i).released_date;

      SELECT ID into movie_id
      FROM MOVIES
      WHERE TITLE = array(i).title
      AND RELEASED_DATE = array(i).released_date;

      for o in movie_max+1..(array(i).nb_copies+movie_max) loop
        INSERT INTO COPIES (NUM_COPY,MOVIE) values (o,movie_id);
      end loop;

      movie_cnt := movie_cnt + array(i).nb_copies;
      movie_max := movie_max + array(i).nb_copies;

      --Provoke an update for trigger
      UPDATE MOVIES
      SET NB_COPIES = movie_cnt,COPIE_MAX = movie_max
      WHERE array(i).title = TITLE
      AND array(i).released_date = RELEASED_DATE;
    END IF;
    
    
    
  COMMIT;
  end loop;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
  NULL;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
end addMovies;
/

create or replace procedure addCountry(code_v IN COUNTRIES.CODE%type,name_v IN COUNTRIES.NAME%type)
AS
code_v2 COUNTRIES.CODE%type;
BEGIN
  SELECT CODE into code_v2
  FROM COUNTRIES WHERE CODE = code_v;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO COUNTRIES (CODE,NAME) VALUES (code_v,name_v);
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

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    INSERT INTO SPOKEN_LANG (LANGUAGE,MOVIE) values (code_v,movie_v);
    COMMIT;
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END addLanguage;
/


 

create or replace function getRandom(min_value IN FLOAT,max_value IN FLOAT) RETURN FLOAT
AS
BEGIN
  RETURN DBMS_RANDOM.VALUE(min_value,max_value);
END getRandom;
/ 

create or replace procedure sendJobCB
AS
BEGIN
      UPDATE MOVIES
      SET NB_COPIES = NB_COPIES
      WHERE ID = ID;
EXCEPTION
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END sendJobCB;
/

declare 
	jobno binary_integer; 
begin
  --dbms_scheduler.create_job('Scheduled_Job','STORED_PROCEDURE','BD_CB.sendJobCB;',sysdate,'FREQ=MINUTELY; INTERVAL=5',TRUE);
  --dbms_scheduler.create_job('Scheduled_Job','STORED_PROCEDURE','BD_CB.sendJobCB;',0,NULL,'FREQ=MINUTELY; INTERVAL=5',NULL,'DEFAULT_JOB_CLASS',TRUE,TRUE,'Job used to send copies to CC1');
	dbms_job.submit (jobno, 'BD_CB.sendJobCB;', sysdate, 'sysdate+1155'); 
	dbms_output.put_line ('New job number: ' || jobno); 
	commit; 
end;
/

create or replace procedure sendCopies(local_id IN MOVIES.ID%type,nb_copies_v IN NUMBER)
AS
  CURSOR curCopies is SELECT num_copy
  FROM COPIES WHERE movie = local_id;

  movie_id NUMBER;
  title_v MOVIES.TITLE%type;
  released_date_v MOVIES.released_date%type;
  num_copy_v NUMBER;

BEGIN

  SELECT title into title_v
  FROM MOVIES
  WHERE ID = local_id;

  SELECT released_date into released_date_v
  FROM MOVIES
  WHERE ID = local_id;

  movie_id := getMovieId@linkCB(title_v,released_date_v);

  UPDATE Movies@linkCB SET NB_COPIES = nb_copies_v + NB_COPIES
  WHERE ID = movie_id;

  OPEN curCopies;
  for i in 1..nb_copies_v LOOP
    FETCH curCopies INTO num_copy_v;
    INSERT INTO COPIES@linkCB (num_copy,movie) values (num_copy_v,movie_id);
    COMMIT;
    DELETE FROM COPIES WHERE num_copy = num_copy_v AND movie = local_id;
    COMMIT;
  END LOOP;
  CLOSE curCopies;

  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END;
/

create or replace procedure sendImage(local_id IN MOVIES.ID%type,remote_id IN NUMBER)
AS
image_cnt NUMBER;
image_remote_id NUMBER;
BEGIN
  SELECT COUNT(ID) into image_cnt FROM IMAGES@linkCB WHERE movie = remote_id;
    IF image_cnt = 0 THEN
      image_remote_id := img_seq.nextval@linkCB;
      INSERT INTO IMAGES@linkCB SELECT image_remote_id,IMAGE,remote_id FROM IMAGES WHERE movie = local_id;
      COMMIT;
    END IF;
EXCEPTION
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END;
/