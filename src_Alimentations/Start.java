import interfaces.InterfaceCreaMovies;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import setupCI.DoubleDbCache;
import setupCI.IndexCreator;
import setupCI.ObjectDbCache;


public class Start {

	private static Logger logger;
	private static InterfaceCreaMovies userInterface;
	
	public static void main(String[] args) {
		
		logger = Logger.getLogger("Main logger");
		ObjectDbCache cacheVoteAverage = new ObjectDbCache("actors_name", "actors_name");
		cacheVoteAverage.loadIndex();
		System.out.println(cacheVoteAverage.index.values().size());
		
		if(args.length > 0 && args[0].equalsIgnoreCase("-console")){
			logger.log(Level.INFO, "Entering in console mode");
		}
		else {
			logger.log(Level.INFO, "Entering in graphical interface mode");
			userInterface = new InterfaceCreaMovies();
			userInterface.setVisible(true);
		}
		
		
	}
	
	public void createIndexes() {
		DoubleDbCache voteAverageCache = new DoubleDbCache("vote_average", "vote_average");
		voteAverageCache.loadIndex();
	}

}
