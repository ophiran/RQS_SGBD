package setupCI;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;

import dbAccess.CouchDBAccess;

public class CertifDbCache extends DbCache {

	SortedMap<String, Set<String>> certifEquiv = new TreeMap<>();
	
	public CertifDbCache(String cacheName, String viewName,
			CouchDBAccess dbConnection) {
		super(cacheName, viewName, dbConnection);
		initCertif();
	}

	private void initCertif() {
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
	}

	@Override
	public void insertResultsIntoIndex(List<Row> rows) {
		boolean isFound;
		for (int i = 0; i < rows.size(); i++) {
            ViewResult.Row row = rows.get(i);
            Integer id = Integer.valueOf(row.getValue());
            String key = String.valueOf(row.getKey());
            
            isFound = false;
            for(Map.Entry<String, Set<String>> entry : certifEquiv.entrySet()){
            	for(Object value:entry.getValue().toArray()) {
            		if(key.equals((String)value)){
            			isFound = true;
            			key = entry.getKey();
            		}
            	}
            }
            if(!isFound){
            	key = "UNRATED";
            }
            
            Set<Integer> ids = index.get(key);
            if (ids == null) {
                ids = new HashSet<Integer>();
                index.put(key, ids);
            }
            ids.add(id);
            
        }
	}

}
