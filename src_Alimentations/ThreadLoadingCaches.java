import interfaces.InterfaceCreaMovies;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import setupCI.ApplicationInfo;
import setupCI.DateDbCache;
import setupCI.DbCache;
import setupCI.DoubleDbCache;
import setupCI.IntegerDbCache;
import setupCI.StringDbCache;
import dbAccess.CouchDBAccess;


public class ThreadLoadingCaches extends Thread{
	private Logger logger;
	public Vector<DbCache> indexesMapping;
	
	public void run() {
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
	}
}
