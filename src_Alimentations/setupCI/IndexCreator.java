package setupCI;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import dbAccess.CouchDBAccess;

import java.util.Properties;
import java.io.*;

import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;

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
		
		dataBase.connect(dbIp, dbPort);
		dataBase.bindDb(dbName);
		
		SortedMap<Double, Set<Integer>> vote_averages = null;
		File f = new File("vote_averages.dump");
		boolean readCache = false;
		if (f.exists()) {
			try {
				readCache = true;
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
           
            System.out.printf("%d vote_average to examine.\n", rows.size());
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
		
		System.out.printf("%d different ratings.\n", vote_averages.size());
        Double low = 7.0;
        Double high = 7.2;

        SortedMap<Double, Set<Integer>> sub_ratings = vote_averages.subMap(low, high);
        System.out.printf("%d different ratings in sub collection.\n", sub_ratings.size());
        

        Set<Integer> ids = new HashSet<Integer>();
        int total = 0;
        for (Set<Integer> s : sub_ratings.values()) {
            total += s.size();
            ids.addAll(s); // opération d'union
        }

        System.out.printf("%d elements processed.\n", total);
        System.out.printf("%d ids in the interval [%f, %f[ .\n", ids.size(), low, high);
	}
}
