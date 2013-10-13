package setupCI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ektorp.ViewResult;

import dbAccess.CouchDBAccess;

public class DoubleDbCache extends DbCache{

	public DoubleDbCache(String cacheName, String viewName, CouchDBAccess dbConnection) {
		super(cacheName, viewName, dbConnection);
	}
	
	@Override
	public void insertResultsIntoIndex(List<ViewResult.Row> rows) {
       
        for (int i = 0; i < rows.size(); i++) {
            ViewResult.Row row = rows.get(i);
            Integer id = Integer.valueOf(row.getValue());
            Double rating = Double.valueOf(row.getKey());
            
            Set<Integer> ids = index.get(rating);
             
            if (ids == null) {
                ids = new HashSet<Integer>();
                index.put(rating, ids);
            }
            
            ids.add(id);
        }
	}

}
