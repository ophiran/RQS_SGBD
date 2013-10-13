package setupCI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ektorp.ViewResult;

import dbAccess.CouchDBAccess;

@Deprecated
public class IndexCreator {
	public static void createIndexes() {
		Properties dbInfo = new Properties();
		
		String dbIp = "127.0.0.1";
		String dbPort = "5984";
		String dbName = "movies";
		
		try {
			File dbInfoFile = new File(System.getProperty("user.home")
					+ System.getProperty("file.separator") + "dbInfo");
			if (dbInfoFile.exists()) {
				dbInfo.load(new FileInputStream(dbInfoFile));
				dbIp = dbInfo.getProperty("dbIp");
				dbPort = dbInfo.getProperty("dbPort");
				dbName = dbInfo.getProperty("dbName");
			} else {
				dbInfo.setProperty("dbIp", dbIp);
				dbInfo.setProperty("dbPort", dbPort);
				dbInfo.setProperty("dbName", dbName);
				dbInfo.store(new FileOutputStream(dbInfoFile), "data base informations");  
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		CouchDBAccess dataBase = new CouchDBAccess();
		
		dataBase.connect(dbIp, dbPort, dbName);
		
		SortedMap<Double, Set<Integer>> vote_averages = null;
		File f = new File("vote_averages.dump");
		if (f.exists()) {
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream in = new ObjectInputStream(fis);
				vote_averages = (SortedMap<Double, Set<Integer>>) in.readObject();
				in.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		} else {
            vote_averages = new TreeMap<Double, Set<Integer>>();
            ViewResult result = dataBase.sendQuery("_design/main", "vote_average");
            List<ViewResult.Row> rows = result.getRows();
           
            for (int i = 0; i < rows.size(); i++) {
                ViewResult.Row row = rows.get(i);
                Integer id = Integer.valueOf(row.getValue());
                Double rating = Double.valueOf(row.getKey());
                
                Set<Integer> ids = vote_averages.get(rating);
                 
                if (ids == null) {
                    ids = new HashSet<Integer>();
                    vote_averages.put(rating, ids);
                }
                
                ids.add(id);
            }
            try {
	            FileOutputStream fos = new FileOutputStream(f);
	            ObjectOutputStream out = new ObjectOutputStream(fos);
	            out.writeObject(vote_averages);
	            out.close();
            } catch (IOException ioe) {
            	ioe.printStackTrace();
            }
		}
		
	}
}
