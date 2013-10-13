import interfaces.InterfaceCreaMovies;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import setupCI.DbCache;
import setupCI.DoubleDbCache;
import setupCI.IntegerDbCache;
import setupCI.StringDbCache;


public class Start {

	private static Logger logger;
	private static InterfaceCreaMovies userInterface;
	public static Vector<DbCache> indexesMapping;
	
	public static void main(String[] args) {
		
		logger = Logger.getLogger("Main logger");
		indexesMapping = new Vector<>();
		
		logger.log(Level.INFO, "Loading cache - vote_average");
		DbCache cacheMapping = new DoubleDbCache("vote_average", "vote_average");
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - vote_count");
		cacheMapping = new IntegerDbCache("vote_count", "vote_count");
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - genres_name");
		cacheMapping = new StringDbCache("genres_name", "genres_name");
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		logger.log(Level.INFO, "Loading cache - certification");
		cacheMapping = new StringDbCache("certification", "certification");
		cacheMapping.loadIndex();
		indexesMapping.add(cacheMapping);
		
		if(args.length > 0 && args[0].equalsIgnoreCase("-console")){
			logger.log(Level.INFO, "Entering in console mode");
		}
		else {
			logger.log(Level.INFO, "Entering in graphical interface mode");
			userInterface = new InterfaceCreaMovies(indexesMapping);
			userInterface.setVisible(true);
		}
		
		
	}
	
	public void createIndexes() {
		DoubleDbCache voteAverageCache = new DoubleDbCache("vote_average", "vote_average");
		voteAverageCache.loadIndex();
	}

}
