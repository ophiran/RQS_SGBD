package setupCI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ektorp.ViewResult;

import dbAccess.CouchDBAccess;

public abstract class DbCache {
	public Map<Object, Set<Integer>> index;
	protected File dump;
	protected String viewName;
	protected CouchDBAccess dbConnection;
	
	public DbCache(String cacheName, String viewName, CouchDBAccess dbConnection) {
		ApplicationInfo dbInfo = ApplicationInfo.getInstance();
		dump = new File(dbInfo.getCacheDirPath() + System.getProperty("file.separator") + cacheName);
		this.viewName = viewName;
		this.dbConnection = dbConnection;
	}
	
	public void loadIndex() {
		if (cacheExists()) {
			loadFromCache();
		} else {
			fetchFromDb(viewName);
			serializeIndex();
		}
	}
	
	public void loadFromCache() {
		try {
			FileInputStream fis = new FileInputStream(dump);
			ObjectInputStream in = new ObjectInputStream(fis);
			index = (Map<Object, Set<Integer>>) in.readObject();
			in.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	
	public void fetchFromDb(String viewName) {
		index = new TreeMap<Object, Set<Integer>>();
        ViewResult result = dbConnection.sendQuery("_design/main", viewName);
        List<ViewResult.Row> rows = result.getRows();
       
        insertResultsIntoIndex(rows);
        
	}
	
	public abstract void insertResultsIntoIndex(List<ViewResult.Row> rows);
	
	public void serializeIndex() {
		try {
            FileOutputStream fos = new FileOutputStream(dump);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(index);
            out.close();
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
	}
	
	public boolean cacheExists() {
		return dump.exists();
	}
	
	@Override
	public String toString() {
		return viewName;
	}
}
