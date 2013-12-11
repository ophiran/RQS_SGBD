CREATE OR REPLACE TRIGGER AddToCC1
	BEFORE UPDATE ON Movies
	FOR EACH ROW
DECLARE
	PRAGMA AUTONOMOUS_TRANSACTION;
	numberCopiesInserted NUMBER;
	lang Languages%rowtype;
	cert_name CERTIFICATIONS.Name%type;

	CURSOR curLang is  SELECT Languages.Code, Languages.Name
	FROM Languages INNER JOIN Spoken_lang ON Spoken_lang.Language = Languages.Code
	WHERE Spoken_lang.Movie = :new.id;

	CURSOR curComp is SELECT name
	FROM companies
	WHERE id in (SELECT company
				 FROM production_companies
				 WHERE movie = :new.id);

	CURSOR curActor is SELECT name
	FROM artists
	WHERE id in (SELECT artist
				 FROM movie_play
				 WHERE movie = :new.id);

	CURSOR curDirect is SELECT name
	FROM artists
	WHERE id in (SELECT director
				 FROM movie_direct
				 WHERE movie = :new.id);

	CURSOR curGenre is SELECT name
	FROM genres
	WHERE id in (SELECT genre 
			 	 FROM movie_genres
			 	 WHERE movie = :new.id);

	CURSOR curCountry is SELECT *
	FROM countries
	WHERE code in (SELECT code
				   FROM production_countries
				   WHERE movies = :new.id);

	CURSOR curCopies is SELECT num_copy
	FROM COPIES WHERE movie = :new.id;
	--
	movie_id NUMBER;
	image_remote_id NUMBER;
	image_cnt NUMBER;
	country_v COUNTRIES%rowtype;
	param_var VARCHAR2(2400 char); 
	nb_copies_v NUMBER;
	--num_copy_v NUMBER;
BEGIN
	nb_copies_v := getRandom(0,:new.nb_copies/2);

	:new.nb_copies := :new.nb_copies - nb_copies_v;
	IF nb_copies_v > 0 THEN
		IF NOT movieExist@linkCB(:new.title, :new.released_date) THEN

			--Insert the movie in the database
			SELECT Name INTO cert_name FROM CERTIFICATIONS WHERE id = :new.certification;
			AddMovie@linkCB(:new.title, :new.overview, :new.released_date, :new.vote_average, 
				:new.vote_count, cert_name, :new.runtime);	

			--get the movie id from remote db
			movie_id := getMovieId@linkCB(:new.title,:new.released_date);

			--start adding every required table needed for the movie

			--adding poster (Cant use BLOB on a remote procedure call)
			sendImage(:new.id,movie_id);


			--adding languages
			OPEN curLang;
			FETCH curLang INTO lang;
			addLanguage@linkCB(lang.Name,lang.Code, movie_id);
			WHILE curLang%FOUND LOOP
				FETCH curLang INTO lang;
				addLanguage@linkCB(lang.Name,lang.Code, movie_id);
			END LOOP;
			CLOSE curLang;

			--adding countries
			OPEN curCountry;
			FETCH curCountry INTO country_v;
			addCountry@linkCB(country_v.CODE,country_v.NAME,movie_id);
			WHILE curCountry%FOUND LOOP
				FETCH curCountry INTO country_v;
				addCountry@linkCB(country_v.CODE,country_v.NAME,movie_id);
			END LOOP;
			CLOSE curCountry;

			--adding companies
			OPEN curComp;
			FETCH curComp INTO param_var;
			addCompany@linkCB(param_var,movie_id);
			WHILE curComp%FOUND LOOP
				FETCH curComp INTO param_var;
				addCompany@linkCB(param_var,movie_id);
			END LOOP;
			CLOSE curComp;

			--adding actors
			OPEN curActor;
			FETCH curActor INTO param_var;
			addArtist@linkCB(param_var,movie_id);
			WHILE curActor%FOUND LOOP
				FETCH curActor INTO param_var;
				addArtist@linkCB(param_var,movie_id);
			END LOOP;
			CLOSE curActor;

			--adding directors
			OPEN curDirect;
			FETCH curDirect INTO param_var;
			addDirector@linkCB(param_var,movie_id);
			WHILE curDirect%FOUND LOOP
				FETCH curDirect INTO param_var;
				addDirector@linkCB(param_var,movie_id);
			END LOOP;
			CLOSE curDirect;

			--adding genders
			OPEN curGenre;
			FETCH curGenre INTO param_var;
			addGenre@linkCB(param_var,movie_id);
			WHILE curGenre%FOUND LOOP
				FETCH curGenre INTO param_var;
				addGenre@linkCB(param_var,movie_id);
			END LOOP;
			CLOSE curGenre;

		END IF;


		sendCopies(:new.ID,nb_copies_v);
	END IF;
EXCEPTION
  WHEN OTHERS THEN
    addLogs(SQLCODE,SQLERRM);
END AddToCC1;
/

