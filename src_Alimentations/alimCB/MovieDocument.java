package alimCB;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.StructDescriptor;

public class MovieDocument implements SQLData{
	private String title = "";
	public String overview = "";
	private Date released_date = new Date(0);
	private double vote_average;
	private int vote_count;
	private String certification = "";
	private Vector<String> actors;
	private Vector<String> directors;
	private int runtime;
	private long nb_copies;
	private Vector<String> genres;
	private Vector<String> production_companies;
	private Vector<Language> spoken_languages;
	private Vector<Country> production_countries;
	private BufferedImage poster;
	
	
	
	SortedMap<String, Set<String>> certifEquiv;
	Connection connection;
	String typeName;
	
	public MovieDocument(Map<String, Object> rawDocument,Connection connection) {
		certifEquiv = new TreeMap<>();
		this.connection = connection;
		
		final String[] certifG = {"G"};
		final String[] certifPG = {"PG","TV-G"};
		final String[] certifPG13 = {"PG-13","TV PG"};
		final String[] certifR = {"R","M","MA"};
		final String[] certifNC17 = {"NC-17","X","XXX"};
		
		certifEquiv.put("G", new HashSet<>(Arrays.asList(certifG)));
		certifEquiv.put("PG", new HashSet<>(Arrays.asList(certifPG)));
		certifEquiv.put("PG-13", new HashSet<>(Arrays.asList(certifPG13)));
		certifEquiv.put("R", new HashSet<>(Arrays.asList(certifR)));
		certifEquiv.put("NC-17", new HashSet<>(Arrays.asList(certifNC17)));
		
		actors = new Vector<String>();
		directors = new Vector<String>();
		genres = new Vector<String>();
		production_companies = new Vector<String>();
		production_countries = new Vector<Country>();
		spoken_languages = new Vector<Language>();
		
		
		Object docRaw;
		
		docRaw = rawDocument.get("original_title");
		if(docRaw != null)
			title = String.valueOf(docRaw);
		
		docRaw = rawDocument.get("overview");
		if(docRaw != null)
			overview = String.valueOf(rawDocument.get("overview"));
		
		docRaw = rawDocument.get("release_date");
		if(docRaw != null)
			setReleasedDate(String.valueOf(docRaw));
		
		setVoteAverage(Double.valueOf(rawDocument.get("vote_average").toString()));
		vote_count = Integer.valueOf(rawDocument.get("vote_count").toString());
		setCertification(String.valueOf(rawDocument.get("certification")));
		
		
		ArrayList<Map<String, Object>> rawActors = (ArrayList<Map<String, Object>>) rawDocument.get("actors");
		for(int i = 0; i < rawActors.size(); i++) {
			actors.add(String.valueOf(rawActors.get(i).get("name")));
		}
		
		ArrayList<Map<String, Object>> rawDirectors = (ArrayList<Map<String, Object>>) rawDocument.get("directors");
		for(int i = 0; i < rawDirectors.size(); i++) {
			directors.add(String.valueOf(rawDirectors.get(i).get("name")));
		}
		
		docRaw = rawDocument.get("runtime");
		if(docRaw != null)
			runtime = Integer.valueOf(docRaw.toString());
		
		nb_copies = Math.round(GaussianRandom.random(7.5d, 2d));
		
		ArrayList<Map<String, Object>> rawGenres = (ArrayList<Map<String, Object>>) rawDocument.get("genres");
		for(int i = 0; i < rawGenres.size(); i++) {
			genres.add(String.valueOf(rawGenres.get(i).get("name")));
		}
		
		ArrayList<Map<String, Object>> rawProdCompanies = (ArrayList<Map<String, Object>>) rawDocument.get("production_companies");
		for(int i = 0; i < rawProdCompanies.size(); i++) {
			production_companies.add(String.valueOf(rawProdCompanies.get(i).get("name")));
		}
		
		ArrayList<Map<String, Object>> rawProdCountries = (ArrayList<Map<String, Object>>) rawDocument.get("production_countries");
		for(int i = 0; i < rawProdCountries.size(); i++) {
			Country c = new Country(String.valueOf(rawProdCountries.get(i).get("iso_3166_1")),
									String.valueOf(rawProdCountries.get(i).get("name")));
			production_countries.add(c);
		}
		
		ArrayList<Map<String, Object>> rawSpokenLang = (ArrayList<Map<String, Object>>) rawDocument.get("spoken_languages");
		for(int i = 0; i < rawSpokenLang.size(); i++) {
			Language lang = new Language(String.valueOf(rawSpokenLang.get(i).get("iso_639_1")),
										 String.valueOf(rawSpokenLang.get(i).get("name")));
			spoken_languages.add(lang);
		}
		
		setPoster(String.valueOf(rawDocument.get("poster_path")));
		
		
	}
	
	public void setReleasedDate(String date) {
		if(date != null)
			this.released_date = Date.valueOf(date);
	}
	
	public void setVoteAverage(double vote_average) {
		if (vote_average < 0.0d || vote_average > 10.0d) {
			this.vote_average = 5.0d;
		} else {
			this.vote_average = vote_average;
		}
	}
	

	public void setCertification(String certification) {
		this.certification = certification;
        boolean isFound = false;
        for(Map.Entry<String, Set<String>> entry : certifEquiv.entrySet()){
        	for(Object value:entry.getValue().toArray()) {
        		if(certification.equals((String)value)){
        			isFound = true;
        			this.certification = entry.getKey();
        		}
        	}
        }
        if(!isFound){
        	this.certification = "UNRATED";
        }
	}
	
	public void setPoster(String posterPath) {
		try {
		    URL url = new URL("http://cf2.imgobject.com/t/p/w185" + posterPath);
		    poster = ImageIO.read(url);
		} catch (IOException e) {
			poster = null;
		}
	}
	
	@Override
	public String toString() {
		String ls = System.getProperty("line.separator");
		String toReturn = "Title : " + title + ls + ls 
						+ "Overview : " + overview + ls 
						+ "Release date : " +  released_date.toString() + ls
						+ "Vote average : " +  String.valueOf(vote_average) + ls
						+ "Vote count : " +  String.valueOf(vote_count) + ls
						+ "Certification : " +  certification + ls 
						+ "Runtime : " +  String.valueOf(runtime) + ls 
						+ "Number of copies : " +  String.valueOf(nb_copies) + ls;
		
		toReturn += ls + "Actors : " + ls;
		for(String s: actors) {
			toReturn += s + ls;
		}
		
		toReturn += ls + "Directors : " + ls;
		for(String s: directors) {
			toReturn += s + ls;
		}
		
		toReturn += ls + "Production companies : " + ls;
		for(String s: production_companies) {
			toReturn += s + ls;
		}
		
		toReturn += ls + "Genres : " + ls;
		for(String s: genres) {
			toReturn += s + ls;
		}
		
		toReturn += ls + "Languages : " + ls;
		for(Language l: spoken_languages) {
			toReturn += l.toString() + ls;
		}
		
		toReturn += ls + "Production countries : " + ls;
		for(Country c: production_countries) {
			toReturn += c.toString() + ls;
		}
		
		return toReturn;
	}
	
	// DEBUG
	public void diplayImage() {
		if (poster != null) {
		JFrame frame = new JFrame();
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(poster)));
			frame.pack();
			frame.setVisible(true);
		}
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		return "MOVIE_T";
	}

	@Override
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		this.typeName = typeName;
		title = stream.readString();
		overview = stream.readString();
		released_date = stream.readDate();
		vote_average = stream.readFloat();
		vote_count = stream.readInt();
		certification = stream.readString();
		ResultSet rs = stream.readArray().getResultSet();
		while(rs.next()){
			spoken_languages.add((Language) rs.getObject("LANGUAGE_T"));
		}
		
		rs = stream.readArray().getResultSet();
		while(rs.next()){
			production_countries.add((Country) rs.getObject("country_t"));
		}
		runtime = stream.readInt();
		nb_copies = stream.readLong();
		
		rs = stream.readArray().getResultSet();
		while(rs.next()){
			actors.add((String) rs.getString(0));
		}
		
		rs = stream.readArray().getResultSet();
		while(rs.next()){
			genres.add((String) rs.getString(0));
		}
		
		rs = stream.readArray().getResultSet();
		while(rs.next()){
			production_companies.add((String) rs.getString(0));
		}
		
		rs = stream.readArray().getResultSet();
		while(rs.next()){
			directors.add((String) rs.getString(0));
		}
	}

	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString(title);
		stream.writeString(overview);
		stream.writeDate(released_date);
		stream.writeFloat((float)vote_average);
		stream.writeInt(vote_count);
		stream.writeString(certification);
		stream.writeArray(((OracleConnection)connection).createARRAY("LANGUAGES_T", spoken_languages.toArray()));
		stream.writeArray(((OracleConnection)connection).createARRAY("COUNTRIES_T", production_countries.toArray()));
		stream.writeInt(runtime);
		stream.writeInt((int) nb_copies);
		stream.writeArray(((OracleConnection)connection).createARRAY("ARRAY_STRING", actors.toArray()));
		stream.writeArray(((OracleConnection)connection).createARRAY("ARRAY_STRING", genres.toArray()));
		stream.writeArray(((OracleConnection)connection).createARRAY("ARRAY_STRING", production_companies.toArray()));
		stream.writeArray(((OracleConnection)connection).createARRAY("ARRAY_STRING", directors.toArray()));
	}
	
	public void setConnection(Connection connection){
		this.connection = connection;
	}
	
}
