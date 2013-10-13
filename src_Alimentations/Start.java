import interfaces.InterfaceCreaMovies;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import setupCI.DbCache;
import setupCI.DoubleDbCache;


public class Start {

	private static Logger logger;
	private static InterfaceCreaMovies userInterface;
	public static Vector<DbCache> indexesMapping;
	
	public static void main(String[] args) {
		
		logger = Logger.getLogger("Main logger");
		indexesMapping = new Vector<>();
		
		DbCache cacheVoteAverage = new DoubleDbCache("vote_average", "vote_average");
		cacheVoteAverage.loadIndex();
		indexesMapping.add(cacheVoteAverage);
		
		
		
		System.out.println(cacheVoteAverage.index.values().size());
		
		
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
