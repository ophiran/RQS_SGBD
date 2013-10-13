package setupCI;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;

import dbAccess.CouchDBAccess;

public class DateDbCache extends DbCache{

	public DateDbCache(String cacheName, String viewName,
			CouchDBAccess dbConnection) {
		super(cacheName, viewName, dbConnection);
	}

	@Override
	public void insertResultsIntoIndex(List<Row> rows) {
		for (int i = 0; i < rows.size(); i++) {
            ViewResult.Row row = rows.get(i);
            Integer id = Integer.valueOf(row.getValue());
            String testKey = String.valueOf(row.getKey());
            Date key = Date.valueOf("2000-01-01");
            if (!testKey.equals("null") && !testKey.equals("")) {
	            key = Date.valueOf(testKey);
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
