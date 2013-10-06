import interfaces.InterfaceCreaMovies;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Start {

	private static Logger logger;
	private static InterfaceCreaMovies userInterface;
	
	public static void main(String[] args) {
		
		logger = Logger.getLogger("Main logger");
		IndexCreator.createIndexes();
		
		if(args.length > 0 && args[0].equalsIgnoreCase("-console")){
			logger.log(Level.INFO, "Entering in console mode");
		}
		else {
			logger.log(Level.INFO, "Entering in graphical interface mode");
			userInterface = new InterfaceCreaMovies();
			userInterface.setVisible(true);
		}
		
		
	}

}
