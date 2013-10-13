import interfaces.InterfaceCreaMovies;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import setupCI.ApplicationInfo;
import setupCI.DateDbCache;
import setupCI.DbCache;
import setupCI.DoubleDbCache;
import setupCI.IndexCreator;
import setupCI.IntegerDbCache;
import setupCI.StringDbCache;
import dbAccess.CouchDBAccess;



public class Start {

	private static Logger logger;
	private static InterfaceCreaMovies userInterface;
	public static Vector<DbCache> indexesMapping;
	
	public static void main(String[] args) {
		ApplicationInfo dbInfo = ApplicationInfo.getInstance();
		CouchDBAccess dbConnection = new CouchDBAccess();
		dbConnection.connect(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
		
		logger = Logger.getLogger("Main logger");
		indexesMapping = new Vector<>();
		
		logger.log(Level.INFO, "Loading cache - vote_average");
		DbCache cacheMapping = new DoubleDbCache("vote_average", "vote_average", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - vote_count");
		cacheMapping = new IntegerDbCache("vote_count", "vote_count", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - genres_name");
		cacheMapping = new StringDbCache("genres_name", "genres_name", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - certification");
		cacheMapping = new StringDbCache("certification", "certification", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - actors_name");
		cacheMapping = new StringDbCache("actors_name", "actors_name", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - directors_name");
		cacheMapping = new StringDbCache("directors_name", "directors_name", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - title");
		cacheMapping = new StringDbCache("title", "title", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - release_date");
		cacheMapping = new DateDbCache("release_date", "release_date", dbConnection);
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		dbConnection.close();
		
		if(args.length > 0 && args[0].equalsIgnoreCase("-console")){
			logger.log(Level.INFO, "Entering in console mode");
		}
		else {
			logger.log(Level.INFO, "Entering in graphical interface mode");
			userInterface = new InterfaceCreaMovies(indexesMapping);
			userInterface.setVisible(true);
		}
		
		
	}
	

}
