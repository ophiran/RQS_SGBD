import interfaces.InterfaceCreaMovies;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import searchCI.StringSearch;
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
	
	public static void main(String[] args) {
		
		ThreadLoadingCaches cacheLoader = new ThreadLoadingCaches();
		cacheLoader.start();
		LoadingDialog ld = new LoadingDialog();
		ld.setVisible(true);
		try {
			cacheLoader.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ld.dispose();
		
		//Vector<Set<Integer>> test2 = new Vector<>();
		Set<Integer> test = new StringSearch().searchIndex("st", cacheLoader.indexesMapping.elementAt(5).index);
		
		//test.run();
		
		if(args.length > 0 && args[0].equalsIgnoreCase("-console")){
			logger.log(Level.INFO, "Entering in console mode");
		}
		else {
			//logger.log(Level.INFO, "Entering in graphical interface mode");
			userInterface = new InterfaceCreaMovies(cacheLoader.indexesMapping);
			userInterface.setVisible(true);
		}
		
		
	}
	

}
