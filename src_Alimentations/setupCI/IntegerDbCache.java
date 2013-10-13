package setupCI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;

public class IntegerDbCache extends DbCache {

	public IntegerDbCache(String cacheName, String viewName) {
		super(cacheName, viewName);
	}

	@Override
	public void insertResultsIntoIndex(List<Row> rows) {
		for (int i = 0; i < rows.size(); i++) {
            ViewResult.Row row = rows.get(i);
            Integer id = Integer.valueOf(row.getValue());
            Integer count = Integer.valueOf(row.getKey());
            
            Set<Integer> ids = index.get(count);
             
            if (ids == null) {
                ids = new HashSet<Integer>();
                index.put(count, ids);
            }
            
            ids.add(id);
        }
	}

	
	
}
